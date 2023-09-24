package gmx.multiroomchat.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gmx.multiroomchat.room.ChatRoom;
import gmx.multiroomchat.room.Helper;

public class Server {
	private static final int PORT = 7777; // 채팅 프로그램을 사용할 포트번호
	private ConcurrentHashMap<String, ChatRoom> roomManager = new ConcurrentHashMap<>();
	ServerSocket serverSocket;

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT); // 해당 포트로 서버소켓 생성
			System.out.println(PORT + "로 서버 시작.");

			while (true) {
				Socket socket = serverSocket.accept(); // 서버소켓으로 연결을 수립하고 클라이언트 기다림
				Helper client = new Helper(socket, this);
				new Thread(client).start(); // Helper를 쓰레드로 시작
			}
		} catch (BindException e) {
			System.err.println(PORT + "가 이미 사용 중입니다.");
		} catch (IOException e) {
			System.err.println("서버 시작 에러");
		}
	}

	public ChatRoom createRoom(String roomName) { // helper에서 받아온 이름
		if (roomManager.containsKey(roomName)) { // 이미 존재한다면
			return null;
		}
		ChatRoom room = new ChatRoom(roomName); // 새로운 채팅방 생성
		roomManager.put(roomName, room); // 해시맵에 방 추가
		return room;
	}

	public ChatRoom enterRoom(String roomName) { // Helper클래스에서 방 입장을 위해 방 정보 return
		return roomManager.get(roomName);
	}

	public Set<String> getRoomName() { // 생성시기 오름차순으로 변경 희망 ConcurrentHashMap은 순서가 없음.
		return roomManager.keySet();
	}

}
