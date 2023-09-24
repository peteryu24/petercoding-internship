package gmx.multiroomchat.client;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver implements Runnable {
	private DataInputStream dis;
	private boolean runFlag = true; // 스레드 실행 flag

	public Receiver(DataInputStream dis) {
		this.dis = dis;
	}

	@Override
	public void run() {
		try {
			while (isFlag()) { // 스레드가 실행 중일때
				String received = dis.readUTF(); // 메세지 받아오기
				System.out.println(received);
			}
		} catch (IOException e) {
			if (isFlag()) {
				System.err.println("서버에서 메시지를 가져오지 못 함");
			}
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
			} catch (IOException e) {
				System.err.println("inputStream Close 에러");
			}
		}
	}

	public synchronized boolean isFlag() {
		return runFlag;
	}

	public synchronized void stop() {
		runFlag = false;
	}
}
