package gmx.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultiChatClient1 {
	private String name;
	private Socket socket;
	private String serverIp = "127.0.0.1"; // 루프백 주소
	DataInputStream input;
	DataOutputStream output;
	Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		new MultiChatClient1().start();
	}

	public void start() {
		try {
			// throw new IOException(); // catch문 실행 확인용
			connectServer();
			setReceiverSender();
		} catch (IOException e) { // 연결 실패시
			System.out.print("서버에 연결할 수 없습니다. \n재시작을 원하면 1 종료시 2: ");
			endCheck(); // 1 입력시 start 재귀호출
		}
	}

	void connectServer() throws IOException {
		socket = new Socket(serverIp, 7777); // 접속할 서버의 ip주소와 포트 정보로 소켓 생성하여 서버에 연결 요청
		System.out.print("서버와 연결되었습니다. 대화명을 입력하세요 : ");
		name = scan.nextLine();
	}

	void setReceiverSender() {
		ClientReceiver clientReceiver = new ClientReceiver(socket);
		ClientSender clientSender = new ClientSender(socket);

		clientReceiver.start();
		clientSender.start();
	}

	class ClientReceiver extends Thread { // 서버에서 메세지를 수신하고 출력
		Socket socket;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				input = new DataInputStream(socket.getInputStream()); // 데이터 가져오기
			} catch (IOException e) { // DataInputStream 오류
				System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
				endCheck(); // 2 입력시 ClientReceiver 재귀호출
			}
		}

		@Override
		public void run() {
			while (input != null) { // 입력이 없을 때까지
				try {
					System.out.println(input.readUTF()); // 서버로부터 메세지 읽고 출력
				} catch (IOException e) { // 메세지 수신 과정 오류
					System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
					endCheck(); // 2 입력시 ClientReceiver 재귀호출
				}
			}
		}

	}

	class ClientSender extends Thread { // 사용자가 입력한 메세지를 서버로 전송

		Socket socket;

		public ClientSender(Socket socket) {
			this.socket = socket;
			try {
				setDataOutputStream();
			} catch (Exception e) { // DataOutputStream 오류
				System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
				endCheck(); // 2 입력시 ClientSender 재귀호출
			}
		}

		void setDataOutputStream() throws IOException {
			output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(name);
			System.out.println(name + "님이 대화방에 입장하였습니다.\n채팅을 입력하세요:  ");
		}

		@Override
		public void run() {

			String msg = "";
			while (output != null) {
				try {
					sendMessage(msg);
				} catch (IOException e) { // 메세지 전송 과정 오류
					System.out.println("메세지 수신 과정 오류. \n재시작을 원하면 1 종료 희망시 2: ");
					endCheck(); // 2 입력시 ClientSender 재귀호출
				}
			}
		}

		void sendMessage(String msg) throws IOException {
			Scanner sc = new Scanner(System.in);
			msg = sc.nextLine(); // 메세지 입력
			if (msg.equals("exit")) { // exit을 입력하면 클라이언트 종료
				try {
					if (output != null) {
						output.close(); // output.flush();
						// 메모리 확보를 위해 항상 IO들은 close() 해주기
						// flush()는 일정 크기를 넘어섰지만, 계속 사용해야 할 때
					}
					if (socket != null && !socket.isClosed()) {
						socket.close();
					}
				} catch (IOException e) {
				}
				System.exit(0); // 프로그램 정상 종료
			}

			output.writeUTF("[" + name + "]" + msg); // 이름과 함께 메세지 출력
		}

	}

	void endCheck() {
		int i = scan.nextInt();
		if (i == 1) {
			System.out.println("재시도......");
			start();
		} else if (i == 2) {
			System.out.println("포로그램을 종료합니다.");
			// output close
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				System.out.println("output close 오류");
			} finally {
				output = null;
			}
			// input close
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				System.out.println("input close 오류");
			} finally {
				input = null;
			}
			// socket close
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				System.out.println("socket close 오류");
			} finally {
				socket = null;
			}
			System.exit(0); // 포로그램 정상종료
		} else {
			System.out.println("잘못된 숫자 입력입니다. \n 프로그램을 강제 종료합니다.");
			System.exit(1); // 포로그램 비정상종료
		}
	}
}
