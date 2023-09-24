package gmx.multiroomchat.client;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver implements Runnable {
	private DataInputStream dis;
	private boolean runningFlag = true; // 스레드 실행 flag

	public Receiver(DataInputStream dis) {
		this.dis = dis;
	}

	@Override
	public void run() {
		try {
			while (isRunning()) { // 스레드가 실행 중일때
				String received = dis.readUTF(); // 메세지 받아오기
				System.out.println(received);
			}
		} catch (IOException e) {
			if (isRunning()) {
				System.err.println("서버에서 메시지를 가져오지 못 함");
			}
		}
	}

	public synchronized boolean isRunning() {
		return runningFlag;
	}

	public synchronized void stop() {
		runningFlag = false;
	}
}
