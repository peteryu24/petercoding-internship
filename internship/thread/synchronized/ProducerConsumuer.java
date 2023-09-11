package gmx.thread;

import java.io.InterruptedIOException;

class Buffer {
	private int contents;
	private boolean available = false; // 락을 위한 키워드

	public synchronized void put(int value) {
		while (available == true) { // 빵을 한 개 넣고 wait()한 뒤 소비자가 락을 포기할 때까지 기다림
			try {
				/*
				 * 현재 획득한 락을 포기하고 대기상태에 돌입(락이 없을시 에외발생)
				 * 다른 스레드가 동일한 객체의 락을 얻은 뒤 notify() 메서드를 호출해야 대기해제
				 * 동기화가 되어있다면 notify()가 호출되어도 호출한 쓰레드가 락을 버려야 락을 획득 가능
				 */
				wait();
			} catch (InterruptedException e) {
			}
		}
		contents = value;
		System.out.println("생산자########: 생산 " + contents);
		notify(); // wait() 해제
		available = true; 
	}

	public synchronized int get() {
		while (available == false) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		System.out.println("소비자########: 소비 " + contents);
		notify();
		available = false;
		return contents;
	}
}

class Producer extends Thread {
	private Buffer b;

	public Producer(Buffer blank) {
		b = blank;
	}

	public void run() {
		for (int i = 1; i <= 10; i++) {
			b.put(i); // 버퍼에 담기
		}
	}
}

class Consumer extends Thread {
	private Buffer b;

	public Consumer(Buffer blank) {
		b = blank;
	}

	public void run() {
		int value = 0;
		for (int i = 1; i <= 10; i++) {
			value = b.get(); // 버퍼에서 빼기
		}
	}
}

public class ProducerConsumer {
	public static void main(String[] args) {
		/*
		 * InputStream 과 InputStreamReader를 보완한 입출력 형태.
		 * 저장 공간은 고정 값이 아니라 가변값으로 효율적인 공간관리(기본값이 존재는 함)
		 * 버퍼는 내용을 한 번에 전송하는 특성(빠른 속도)
		 * Enter만 경계로 인식(Scanner는 띄어쓰기도 경계로 인식)
		 * 
		 * 데이터를 한 곳에서 다른 곳으로 보낼 때 임시 저장공간으로 활용
		 */

		Buffer buff = new Buffer();
		Producer p1 = new Producer(buff);
		Consumer c1 = new Consumer(buff);
		p1.start();
		c1.start();
	}

}
