package gmx.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MultiChatServer {
	private HashMap<String, DataOutputStream> clients; // <클라이언트 이름, 출력 스트림>
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		new MultiChatServer().start();
	}

	public MultiChatServer() {
		clients = new HashMap<String, DataOutputStream>();
		// 여러 스레드에서 접근할 것이므로 동기화
		Collections.synchronizedMap(clients); // 여러 스레드에서 접근하기 때문에 해시맵 동기화
		/*
		 * ConcurrentHashMap()
		 * key와 value에 null을 허용하지 않음(TreeMap에 반환된 value값은 null 허용)
		 * 읽기 작업에는 여러 쓰레드가 동시 접근 가능, 쓰기 작업에서는 특정 버킷에 대한 lock
		 * SynchronizedMap()은 데이터 일관성이 중요할 때 사용해야 하며 
		 * ConcurrentHashMap 읽기 작업보다 쓰기 작업에서
		 * 맵에 훨씬 더 많은 업데이트 작업이 있는 성능이 중요한 시스템에서 사용
		 * 
		 * synchronizedMap vs ConcurrentHashMap 사용 적절 시기?
		 */
	}

	public void start() {
		try {
			Socket socket;
			// 리스너 소켓 생성
			serverSocket = new ServerSocket(7777); // 루프백 주소와 포트 7777
			System.out.println("서버가 시작되었습니다.");
			// 클라이언트와 연결되면
			while (true) { // 통신이 종료되기 전까지 연결
				// 서버 소켓이 클라이언트의 연결 요청을 처리할 수 있도록 대기 상태로 만든다
				socket = serverSocket.accept(); // 연결이 수립되면 소켓 생성(receiver)
				ServerReceiver receiver = new ServerReceiver(socket); // 소켓으로 스트림 통신 연로 개통
				receiver.start(); // run 메소드
			}
		} catch (IOException e) {
			System.out.println("서버 소켓 생성 중 예외 발생");
		}
	}

	class ServerReceiver extends Thread { // client와 통신
		Socket socket;
		DataInputStream input;
		DataOutputStream output;

		public ServerReceiver(Socket socket) { // 연결된 소켓이 인자
			this.socket = socket;
			try { // 통신할 스트림 생성
				input = new DataInputStream(socket.getInputStream()); // 읽기
				output = new DataOutputStream(socket.getOutputStream()); // 쓰기
			} catch (IOException e) {
				System.out.println("client 소켓 생성 중 에러 발생.");
			}
		}

		@Override
		public void run() { // 쓰레드를 상속받은 start메소드에서 start()로 실행
			String name = "";
			try {
				// 클라이언트가 서버에 접속하면 대화방에 알린다.
				name = input.readUTF();
				/*
				sendToAll("#" + name + "[ip:" + socket.getInetAddress() + " port:" + socket.getPort() + "]"
						+ "님이 대화방에 접속하였습니다.");
				*/
				sendToAll(name + "님이 대화방에 접속하였습니다."); // 서버에서 출력
				clients.put(name, output); // 해시맵에 추가
				/*
				System.out.println(name + "[ip:" + socket.getInetAddress() + " port:" + socket.getPort() + "]"
						+ "님이 대화방에 접속하였습니다.");
				*/
				System.out.println(name + "님이 대화방에 접속하였습니다.qqq");
				System.out.println("현재 " + clients.size() + "명이 대화방에 접속 중입니다."); // 메세지 전송
				while (input != null) { // 클라이언트로부터 지속적으로 메세지 수신 (input - DataInputStream의 객체)
					/*
					 * DataInputStream 클래스에서 제공하는 메소드 
					 * 바이트스트림에서 UTF-8형식의 문자열을 String 형식으로 변환
					 */
					sendToAll(input.readUTF());
				}
			} catch (IOException e) {
				System.out.println("클라이언트와의 연결이 끊김.");
			} finally {

				// 접속이 종료되면
				clients.remove(name); // 소켓을 닫고 맵에서 제거
				/*
				sendToAll(
						"#" + name + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "님이 대화방에서 나갔습니다.");
				System.out.println(
						name + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "님이 대화방에서 나갔습니다.");
				*/
				sendToAll(name + "님이 대화방에서 나갔습니다.");
				System.out.println(name + "님이 대화방에서 나갔습니다.");
				System.out.println("현재 " + clients.size() + "명이 대화방에 접속 중입니다.");
				try { // 스트림 통신과 소켓 전부 close
					if (input != null) {
						input.close();
					}
					if (output != null) {
						output.close(); // output.flush();

					}
					if (socket != null && !socket.isClosed()) {
						socket.close();
					}
				} catch (IOException e) {

				}
				// System.exit(0); // 채팅 프로그램 종료
			}
		}

		public void sendToAll(String message) { // 모든 client에게 알림
			/*
			 * 처음부터 끝까지 단방향 반복만 가능
			 * 값을 변경하거나 추가 불가능
			 * 대량의 데이터를 처리할 때 속도가 느리다
			 */
			Iterator<String> itr = clients.keySet().iterator(); // 컬렉션 프레임워크에서(List, Set, Map, Queue)반복할 때 사용가능한 객체
			while (itr.hasNext()) { // 다음 값이 있을 때까지
				try {
					DataOutputStream dos = clients.get(itr.next()); // next()는 다음 값을 가져오는 메소드
					dos.writeUTF(message); // 입장했다는 메세지
				} catch (Exception e) {
				}
			}
		}
	}
}
