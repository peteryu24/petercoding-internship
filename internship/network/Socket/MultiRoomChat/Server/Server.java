package gmx.multiroomchat.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import gmx.multiroomchat.room.ChatRoom;
import gmx.multiroomchat.room.Helper;

public class Server {
	private static final int PORT = 7777; // 채팅 프로그램을 사용할 포트번호
	public ConcurrentHashMap<String, ChatRoom> roomManager = new ConcurrentHashMap<>();
	private ServerSocket serverSocket;
	private static Server instance = null; // 싱글톤 인스턴스

	private Server() { // 외부에서 생성 불가(싱글톤 패턴)
	}

	public static Server getInstance() { // 싱글톤 인스턴스 얻기
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public void startServer() {
		try {
			serverSocket = new ServerSocket(PORT); // 해당 포트로 서버소켓 생성
			System.out.println(PORT + " 포트번호 로 서버 시작.");

			while (true) {
				Socket socket = serverSocket.accept(); // 서버소켓으로 연결을 수립하고 클라이언트 기다림
				Helper client = new Helper(socket);
				new Thread(client).start(); // Helper를 쓰레드로 시작
			}
		} catch (BindException e) {
			System.err.println(PORT + "가 이미 사용 중입니다.");
		} catch (IOException e) {
			System.err.println("서버 시작 에러");
		}
	}
}
