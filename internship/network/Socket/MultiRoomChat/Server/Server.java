package gmx.multiroomchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gmx.multichatroom.room.ChatRoom;
import gmx.multichatroom.room.Helper;

public class Server {
	private static final int PORT = 7777;
	private Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

	

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server started on port " + PORT);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				Helper client = new Helper(clientSocket, this);
				new Thread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ChatRoom createRoom(String roomName) {
		if (rooms.containsKey(roomName)) {
			return null; // 이미 해당 이름의 방이 존재하면 null 반환
		}
		ChatRoom room = new ChatRoom(roomName);
		rooms.put(roomName, room);
		return room;
	}

	public ChatRoom getRoom(String name) {
		return rooms.get(name);
	}

	public Set<String> getAllRoomNames() {
		return rooms.keySet();
	}
}
