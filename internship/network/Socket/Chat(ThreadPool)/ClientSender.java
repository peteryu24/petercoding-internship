package gmx.chat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread { // 사용자가 입력한 메세지를 서버로 전송

	Socket socket;
	DataOutputStream output;
	ClientServer cs = new ClientServer();

	public ClientSender(Socket socket) {
		this.socket = socket;
		try {
			setDataOutputStream(socket);
		} catch (Exception e) { // DataOutputStream 오류
			System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
			cs.endCheck();
		}
	}

	private void setDataOutputStream(Socket s) throws IOException {
		output = new DataOutputStream(s.getOutputStream());
		output.writeUTF(cs.name);
		System.out.println(cs.name);
	}

	@Override
	public void run() {

		String msg = "";
		while (output != null) {
			try {
				sendMessage(msg);
			} catch (IOException e) { // 메세지 전송 과정 오류
				System.out.println("메세지 수신 과정 오류. \n재시작을 원하면 1 종료 희망시 2: ");
				cs.endCheck();
			}
		}
	}

	private void sendMessage(String msg) throws IOException {
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

		output.writeUTF("[" + cs.name + "]" + msg); // 이름과 함께 메세지 출력
	}

}
