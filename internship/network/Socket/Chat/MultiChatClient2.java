package gmx.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultiChatClient2 {
	private String name;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String serverIp = "127.0.0.1"; // 루프백 주소
	Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		new MultiChatClient1().start();
	}

	public void start() {
		try {
			connectServer();
			setReceiverSender();
		} catch (IOException e) { // 연결 실패시
			System.out.print("서버에 연결할 수 없습니다. \n재시작을 원하면 1 종료시 2: ");
			endCheck(); // 1 입력시 재귀호출
		}
	}

	void connectServer() throws IOException {
		socket = new Socket(serverIp, 7777); // 접속할 서버의 ip주소와 포트 정보로 소켓 생성하여 서버에 연결 요청
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());

		System.out.print("서버와 연결되었습니다. 대화명을 입력하세요 : ");
		name = scan.nextLine();
		output.writeUTF(name); // 대화명 서버로 전송
	}

	void setReceiverSender() {
		ClientReceiver clientReceiver = new ClientReceiver(); // 메시지 수신 스레드 객체 생성
		ClientSender clientSender = new ClientSender();

		clientReceiver.start();
		clientSender.start(); // 메시지 전송 스레드 시작
	}

	class ClientReceiver extends Thread { // 서버에서 메세지를 수신하고 출력
		@Override
		public void run() {
			while (true) {
				try {
					String message = input.readUTF(); // 데이터 가져오기
					System.out.println(message);
				} catch (IOException e) { // DataInputStream 오류
					System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
					endCheck();
					break;
				}
			}
		}
	}

	class ClientSender extends Thread { // 사용자가 입력한 메세지를 서버로 전송
		@Override
		public void run() {
			while (true) {
				try {
					String message = scan.nextLine();
					output.writeUTF("[" + name + "]" + message);
				} catch (IOException e) {
					System.out.println("메세지 전송 과정 오류. \n재시작을 원하면 1 종료 희망시 2: ");
					endCheck();
					break;
				}
			}
		}
	}

	void endCheck() {
		int i = scan.nextInt();
		if (i == 1) {
			System.out.println("재시도......");
			start();
		} else if (i == 2) {
			close();
			// 메모리 확보를 위해 항상 IO들은 close() 해주기
			// flush()는 일정 크기를 넘어섰지만, 계속 사용해야 할 때
			System.exit(1);
		} else {
			System.out.println("잘못된 숫자 입력입니다. \n 프로그램을 강제 종료합니다.");
			System.exit(1);
		}
	}

	void close() {
		try {
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			// 예외 처리
		}finally {
			output = null;
			input = null;
			socket = null;
		}
	}
}
