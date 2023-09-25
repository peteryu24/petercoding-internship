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
	Receiver receiver;
	Thread receiverThread;

	public void startClient() {
		Socket socket = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		try {
			socket = new Socket(CHATIP, CHATPORT);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			receiver = new Receiver(dis); // dis를 넣은 receiver
			receiverThread = new Thread(receiver); // dis를 넣은 receiver로 쓰레드 선언
			receiverThread.start(); // 쓰레드 시작

			Scanner scan = new Scanner(System.in);
			while (true) { // 에러 발생 전까지
				String userInput = scan.nextLine();
				dos.writeUTF(userInput); // 메세지 전송
			}
		} catch (UnknownHostException e) {
			System.err.println("호스트 에러");
		} catch (IOException e) {
			System.err.println("클라이언트 에러");
		} catch (Exception e) {
			System.err.println("에러 발생: ");
		} finally { // 에러 발생시 쓰레드 종료
			if (receiver != null) {
				receiver.isFlag(); // false flag
			}

			if (receiverThread != null) {
				try {
					receiverThread.join(); // 종료될 때까지 기다리기
				} catch (InterruptedException e) {
					System.err.println("thread Join 에러");
				}
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					System.err.println("outputStream Close 에러");
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					System.err.println("inputStream Close 에러");
				}
			}
			if (socket != null && !socket.isClosed()) { // 소켓을 닫을 때는 연결 상태와 닫힌 상태를 동시에 체크
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println("Socket Close 에러");
				}
			}
		}
	}

}
