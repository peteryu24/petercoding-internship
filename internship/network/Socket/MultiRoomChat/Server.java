package gmx.room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server {
	private static final int PORT = 12345;
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
		private PrintWriter out;
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
			if (out != null) {
				out.println(message);
			}
		}

		@Override
		public void run() {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
				out = new PrintWriter(socket.getOutputStream(), true);

				out.println("Enter your name: ");
				name = in.readLine();

				while (true) {
					out.println("1. Create a new room");
					out.println("2. Join an existing room");
					out.println("Enter your choice: ");
					int choice = Integer.parseInt(in.readLine());

					switch (choice) {
					case 1:
						currentRoom = server.createRoom();
						currentRoom.addClient(this);
						out.println("Created and joined room with ID: " + currentRoom.getId());
						break;
					case 2:
						out.println("Available rooms: " + server.getAllRoomIds());
						out.println("Enter room ID: ");
						int roomId = Integer.parseInt(in.readLine());
						currentRoom = server.getRoom(roomId);
						if (currentRoom != null) {
							currentRoom.addClient(this);
							out.println("Joined room with ID: " + roomId);
						} else {
							out.println("Room not found");
						}
						break;
					}

					while (currentRoom != null) {
						String message = in.readLine();
						if (message.equalsIgnoreCase("exit")) {
							currentRoom.removeClient(name);
							out.println("Left the room");
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
