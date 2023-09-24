package gmx.multiroomchat.client;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver implements Runnable {
	private DataInputStream dis;
	private boolean running = true;

	public Receiver(DataInputStream dis) {
		this.dis = dis;
	}

	@Override
	public void run() {
		try {
			while (isRunning()) {
				String received = dis.readUTF();
				System.out.println(received);
			}
		} catch (IOException e) {
			if (isRunning()) {
				System.err.println("서버에서 메시지를 가져오지 못 함");
			}
		}
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void stop() {
		running = false;
	}
}
