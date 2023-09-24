package gmx.multiroomchat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private static final String CHATIP = "localhost";
	private static final int CHATPORT = 7777;

	public void startClient() {
		Socket socket = null;
		DataInputStream inputStream = null;
		DataOutputStream outputStream = null;

		try {
			socket = new Socket(CHATIP, CHATPORT);
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());

			Receiver receiver = new Receiver(inputStream);
			Thread readerThread = new Thread(receiver);
			readerThread.start();

			Scanner scan = new Scanner(System.in);
			String userInput;
			while (receiver.isRunning() && !(userInput = scan.nextLine()).isEmpty()) {
				outputStream.writeUTF(userInput);
			}

			receiver.stop();
			readerThread.join();
		} catch (UnknownHostException e) {
			System.err.println("호스트 에러");
		} catch (IOException e) {
			System.err.println("클라이언트 에러");
		} catch (Exception e) {
			System.err.println("에러 발생: ");
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.err.println("outputStream Close 에러");
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.err.println("inputStream Close 에러");
				}
			}
			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println("Socket Close 에러");
				}
			}
		}
	}

}
