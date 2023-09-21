package gmx.chat.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver extends Thread { // 서버에서 메세지를 수신하고 출력
	Socket socket;
	DataInputStream input;
	ClientServer cs = new ClientServer();

	public ClientReceiver(Socket socket) {
		this.socket = socket;
		try {
			input = createDataInputStream(socket); // 메소드를 사용하여 초기화
		} catch (IOException e) { // DataInputStream 오류
			System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
			cs.endCheck();
		}
	}

	@Override
	public void run() {
		while (input != null) { // 입력이 없을 때까지
			try {
				System.out.println(input.readUTF()); // 서버로부터 메세지 읽고 출력
			} catch (IOException e) { // 메세지 수신 과정 오류
				System.out.print("데이터를 가져올 수 없습니다. \n재시작을 원하면 1 종료 희망시 2: ");
				cs.endCheck();
			}
		}
	}

	private DataInputStream createDataInputStream(Socket s) throws IOException {
		return new DataInputStream(s.getInputStream());
	}

}
