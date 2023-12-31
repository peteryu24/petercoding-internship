package gmx.multiroomchat.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import gmx.multichatroom.room.ChatRoom;
import gmx.multichatroom.room.Helper;

public class Server { // 하나의 roomManager를 위한 싱글톤 패턴
	private final int PORT = 7777; // 채팅 프로그램을 사용할 포트번호
	public static ConcurrentHashMap<String, ChatRoom> roomManager = new ConcurrentHashMap<>(); // 방 이름과 방
	private ServerSocket serverSocket;
	private static Server serverInstance = null; // 싱글톤 인스턴스

	private Server() { // 외부에서 생성 불가(싱글톤 패턴)
	}

	public static Server getInstance() { // 싱글톤 인스턴스 얻기(객체 생성 없이 호출하기 위해 static)
		if (serverInstance == null) {
			serverInstance = new Server();
		}
		return serverInstance;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT); // 해당 포트로 서버소켓 생성
			System.out.println(PORT + " 포트번호 로 서버 시작.");

			while (true) {
				Socket socket = serverSocket.accept(); // 서버소켓으로 연결을 수립하고 클라이언트 기다림
				Helper person = new Helper(socket);
				new Thread(person).start(); // Helper를 쓰레드로 시작
			}
		} catch (BindException e) {
			System.err.println(PORT + "가 이미 사용 중입니다.");
		} catch (IOException e) {
			System.err.println("서버 시작 에러");
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				System.err.println("서버 소켓 닫기 에러");
			}
		}
	}
}
