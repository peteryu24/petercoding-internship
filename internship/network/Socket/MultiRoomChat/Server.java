package gmx.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server {
	private static final int PORT = 7777;
	private Map<Integer, Room> rooms = new ConcurrentHashMap<>();
	private static int nextRoomId = 1;

	public static void main(String[] args) {
		new Server().startServer();
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server started on port " + PORT);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket, this);
				new Thread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Room createRoom() {
		Room room = new Room(nextRoomId++);
		rooms.put(room.getId(), room);
		return room;
	}

	public Room getRoom(int id) {
		return rooms.get(id);
	}

	public Set<Integer> getAllRoomIds() {
		return rooms.keySet();
	}

	static class Room {
		private int id;
		private ConcurrentMap<String, Client> clients = new ConcurrentHashMap<>();

		public Room(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void addClient(Client client) {
			clients.put(client.getName(), client);
		}

		public void removeClient(String name) {
			clients.remove(name);
		}

		public void broadcast(String message, String excludeClient) {
			for (Client client : clients.values()) {
				if (!client.getName().equals(excludeClient)) {
					client.sendMessage(message);
				}
			}
		}
	}

	static class Client implements Runnable {
		private Socket socket;
		private Server server;
		private OutputStream out;
		private String name;
		private Room currentRoom;

		public Client(Socket socket, Server server) {
			this.socket = socket;
			this.server = server;
		}

		public String getName() {
			return name;
		}

		public void sendMessage(String message) {
			try {
				if (out != null) {
					out.write((message + "\n").getBytes());
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				InputStream in = socket.getInputStream();
				out = socket.getOutputStream();
				byte[] buffer = new byte[1024];
				int bytesRead;

				// Get the name
				sendMessage("Enter your name: ");
				bytesRead = in.read(buffer);
				name = new String(buffer, 0, bytesRead).trim();

				while (true) {
					sendMessage("1. Create a new room");
					sendMessage("2. Join an existing room");
					sendMessage("Enter your choice: ");

					bytesRead = in.read(buffer);
					int choice = Integer.parseInt(new String(buffer, 0, bytesRead).trim());

					switch (choice) {
					case 1:
						currentRoom = server.createRoom();
						currentRoom.addClient(this);
						sendMessage("Created and joined room with ID: " + currentRoom.getId());
						break;
					case 2:
						sendMessage("Available rooms: " + server.getAllRoomIds());
						sendMessage("Enter room ID: ");

						bytesRead = in.read(buffer);
						int roomId = Integer.parseInt(new String(buffer, 0, bytesRead).trim());

						currentRoom = server.getRoom(roomId);
						if (currentRoom != null) {
							currentRoom.addClient(this);
							sendMessage("Joined room with ID: " + roomId);
						} else {
							sendMessage("Room not found");
						}
						break;
					}

					while (currentRoom != null) {
						bytesRead = in.read(buffer);
						String message = new String(buffer, 0, bytesRead).trim();

						if (message.equalsIgnoreCase("exit")) {
							currentRoom.removeClient(name);
							sendMessage("Left the room");
							currentRoom = null;
						} else {
							currentRoom.broadcast(name + ": " + message, name);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
