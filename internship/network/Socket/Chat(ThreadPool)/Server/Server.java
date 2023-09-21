package gmx.chat.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private Map<String, DataOutputStream> clients;
	private ServerSocket serverSocket;
	private int chatPort = 7777;
	private ExecutorService threadPool;

	public Server() {
		clients = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(clients);
		/*
		clients = Collections.synchronizedMap(new HashMap<String,DataOutputStream>());
		*/
		// clients = new ConcurrentHashMap<>();
		threadPool = Executors.newCachedThreadPool();
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(chatPort);
			System.out.println("포트번호 ( " + chatPort + " )에서 서버 시작.");

			while (true) {
				try {
					Socket socket = serverSocket.accept();
					ServerReceiver receiver = new ServerReceiver(socket, this);
					threadPool.execute(receiver); // 쓰레드 풀 시작
				} catch (BindException e) {
					System.out.println("포트번호 ( " + chatPort + " ) 이미 사용 중입니다.");
					chatPort++;
				} catch (IOException e) {
					showError("서버 에러", e);
					break;
				}
			}
		} catch (IOException e) {
			showError("서버소켓 생성 에러", e);
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				showError("서버 소켓 종료 중 오류 발생", e);
			}
			threadPool.shutdown(); // 쓰레드 풀 닫기
		}
	}

	void showError(String str, Exception e) {
		System.out.println(str + ": " + e.getMessage());
	}

	public Map<String, DataOutputStream> getClient() {
		return this.clients;
	}

	public void setClient(String name, DataOutputStream dos) {
		clients.put(name, dos);
	}

	public void removeClient(String name) {
		clients.remove(name);
	}

	public int getSize() {
		return this.clients.size();
	}

	public void sendToAll(String message) { // iterator 말고
		for (DataOutputStream dos : clients.values()) {
			try {
				dos.writeUTF(message);
			} catch (Exception e) {
				showError("메시지 전송 중 오류 발생", e);
			}
		}

	}
}
