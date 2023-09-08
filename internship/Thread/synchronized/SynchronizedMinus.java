
package gmx.thread;

import java.time.LocalDateTime;

public class MySynchronized {

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + " start");
		Runnable r31 = new TestRunnable3();
		Runnable r32 = new TestRunnable3();
		Runnable r41 = new TestRunnable4();
		Runnable r5 = new TestRunnable5();
		Thread t1 = new Thread(r31);
		Thread t2 = new Thread(r32);
		Thread t3 = new Thread(r41);
		Thread t4 = new Thread(r5);
		Thread t5 = new Thread(r5);
		// t1.start(); // run() 실행
		// t2.start();
		// t3.start();
		t4.start();
		t5.start();
		try {
			t1.join();

		} catch (Exception e) {

		}
		System.out.println(Thread.currentThread().getName() + " end");
	}
}

class TestRunnable3 implements Runnable {

	@Override
	public void run() { // start() 로 인해 실행
		System.out.println("쓰레드 11단계 " + LocalDateTime.now());
		thread2();

	}

	public synchronized void thread2() {
		System.out.println("쓰레드 22단계 " + LocalDateTime.now());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		thread3();
	}

	public void thread3() {

		System.out.println("쓰레드 33단계 " + LocalDateTime.now());

		thread4();
		try {
			Thread.sleep(1000, 10000);
		} catch (InterruptedException e) {
		}

	}

	public void thread4() {
		System.out.println("a");
		System.out.println("쓰레드 44단계 " + LocalDateTime.now());
	}

}

class TestRunnable4 implements Runnable {
	@Override
	public void run() {
		System.out.println("쓰레드 테스트 4");
	}
}

class TestRunnable5 implements Runnable {
	int i = 100;

	@Override
	public void run() {
		synchronized (this) { // 뺄셈 메소드에 무분별하게 접근하기 때문에 같은 숫자가 반복해서 나올 수도, 음수가 나올 수도, 내림차순이 아닐 수도
			minus(5);
		}
		System.out.println();
	}

	public void minus(int m) {
		while (i > 0) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			i -= m;
			System.out.println(i);
		}
	}

}
