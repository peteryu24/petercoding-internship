package gmx.multiroomchat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 7777;
	private volatile boolean running = true;



	public void startClient() {
		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream()) {

			// 메시지를 수신하고 출력하는 별도의 스레드 시작
			Thread readerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] buffer = new byte[1024];
					int bytesRead;
					try {
						while (running && (bytesRead = in.read(buffer)) != -1) {
							String received = new String(buffer, 0, bytesRead);
							System.out.print(received);
						}
					} catch (IOException e) {
						if (running) {
							e.printStackTrace();
						}
					}
				}
			});
			readerThread.start();

			// 사용자 입력을 읽고 서버에 전송
			byte[] userInputBuffer = new byte[1024];
			int bytesRead;
			while (running && (bytesRead = System.in.read(userInputBuffer)) != -1) {
				out.write(userInputBuffer, 0, bytesRead);
			}

			running = false;
			readerThread.join();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
