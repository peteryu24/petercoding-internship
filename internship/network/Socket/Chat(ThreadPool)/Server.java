package gmx.chat;

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
				System.out.println("서버가 포트 " + chatPort + "에서 시작되었습니다.");

				while (true) {
					Socket socket = serverSocket.accept();
					ServerReceiver receiver = new ServerReceiver(socket, this);
					threadPool.execute(receiver);
				}
			} catch (BindException e) {
				System.out.println("포트 " + chatPort + "이(가) 사용 중입니다. 다른 포트로 시도합니다.");
				chatPort++;
			} catch (IOException e) {
				displayError("서버 시작 중 오류 발생", e);
				break;
			}
		}
	}

	void displayError(String message, Exception e) {
		System.out.println(message + ": " + e.getMessage());
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
					displayError("메시지 전송 중 오류 발생", e);
				}
			}
		}
	}
}
