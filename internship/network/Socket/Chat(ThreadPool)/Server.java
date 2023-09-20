package gmx.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private Map<String, DataOutputStream> clients;
	private ServerSocket serverSocket;
	private int chatPort = 7777;
	private ExecutorService threadPool;

	public static void main(String[] args) {
		new Server().start();
	}

	public Server() {
		clients = Collections.synchronizedMap(new HashMap<String, DataOutputStream>());
		threadPool = Executors.newCachedThreadPool();
	}

	public void start() {
		while (true) {
			try {
				serverSocket = new ServerSocket(chatPort);
				System.out.println("포트번호 (  " + chatPort + " )에서 서버 시작.");

				while (true) {
					Socket socket = serverSocket.accept();
					ServerReceiver receiver = new ServerReceiver(socket, this);
					threadPool.execute(receiver); // 쓰레드 풀 시작
				}
			} catch (BindException e) {
				System.out.println("포트번호 ( " + chatPort + " ) 이미 사용 중입니다.");
				chatPort++;
			} catch (IOException e) {
				showError("서버 에러", e);
				break;
			}
		}
	}

	void showError(String str, Exception e) {
		System.out.println(str + ": " + e.getMessage());
	}

	public Map<String, DataOutputStream> getClients() {
		return this.clients;
	}

	public void sendToAll(String message) {
		synchronized (clients) {
			for (DataOutputStream dos : clients.values()) {
				try {
					dos.writeUTF(message);
				} catch (Exception e) {
					showError("메시지 전송 중 오류 발생", e);
				}
			}
		}
	}
}
