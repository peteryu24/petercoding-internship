package gmx.room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 12345;
	private volatile boolean running = true;

	public static void main(String[] args) {
		new Client().startClient();
	}

	public void startClient() {
		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

			// 메시지를 수신하고 출력하는 별도의 스레드 시작
			Thread readerThread = new Thread(() -> {
				try {
					String message;
					while (running && (message = in.readLine()) != null) {
						System.out.println(message);
					}
				} catch (IOException e) {
					if (running) {
						e.printStackTrace();
					}
				}
			});
			readerThread.start();

			// 사용자 입력을 읽고 서버에 전송
			String userInput;
			while ((userInput = consoleInput.readLine()) != null) {
				out.println(userInput);
			}

			running = false;
			readerThread.join();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
