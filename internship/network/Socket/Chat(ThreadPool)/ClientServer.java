package gmx.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientServer {
	public String name;
	private Socket socket;
	private String serverIp = "127.0.0.1"; // 루프백 주소
	DataInputStream input;
	DataOutputStream output;
	private ExecutorService threadPool;

	Scanner scan = new Scanner(System.in);

	public void start() {
		threadPool = Executors.newFixedThreadPool(2);
		try {
			connectServer(); // 소켓 연결
			setReceiverSender(); // 스트림 연결
		} catch (IOException e) { // 연결 실패시
			System.out.print("서버에 연결할 수 없습니다. \n재시작을 원하면 1 종료시 2: ");
			endCheck(); // 1 입력시 start 재귀호출
		}
	}

	private void connectServer() throws IOException {
		socket = new Socket(serverIp, 7777); // 접속할 서버의 ip주소와 포트 정보로 소켓 생성하여 서버에 연결 요청
		System.out.print("서버와 연결되었습니다. 대화명을 입력하세요 : ");
		name = scan.nextLine();
	}

	private void setReceiverSender() {
		ClientReceiver clientReceiver = new ClientReceiver(socket);
		ClientSender clientSender = new ClientSender(socket, this); // ClientServer 자체를 던져줌

		threadPool.execute(clientReceiver);
		threadPool.execute(clientSender);
	}

	public void endCheck() {
		int i = scan.nextInt();
		if (i == 1) {
			System.out.println("재시도......");
			start();
		} else if (i == 2) {
			System.out.println("포로그램을 종료합니다.");
			if (threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown(); // 쓰레드 풀 종료
			}
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
