package gmx.multiroomchat.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gmx.multiroomchat.room.ChatRoom;
import gmx.multiroomchat.room.Helper;

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
		} catch (BindException e) {
			System.err.println("오류: 포트 " + PORT + "가 이미 사용 중입니다. 다른 포트를 시도해주세요.");
		} catch (IOException e) {
			System.err.println("오류: 서버 시작 중 문제 발생. 원인: " + e.getMessage());
		}
	}

	public ChatRoom createRoom(String roomName) {
		if (rooms.containsKey(roomName)) {
			return null;
		}
		ChatRoom room = new ChatRoom(roomName);
		rooms.put(roomName, room);
		return room;
	}

	public ChatRoom getRoom(String name) {
		return rooms.get(name);
	}

	public Set<String> getRoomName() { // 생성시기 오름차순으로 변경 희망 ConcurrentHashMap은 순서가 없음.
		return rooms.keySet();
	}

}
