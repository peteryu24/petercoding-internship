package gmx.thread;

public class MyselfJoin {
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + " start");
		Runnable r = new TestRunnable2();
		Thread t = new Thread(r);
		t.start(); // run() 실행 
		//new Thread(r).start(); 스레드 객체 바로 생성하여 시작 이때 join은 어떻게?

		try {
			t.join(); // t.join()이기 때문에 t의 쓰레드 가 실행이 끝날 때까지 대기 또한 해당 쓰레드가 실행이 끝나야 다음 쓰레드를 start()한다.
		       		// (왜 꼭 에러처리? InterruptedException) - Checked Exception이라 예외 처리 코드가 들어가야한다.(Exception 상속)     
			        // RuntimeException을 상속하면 Unchecked Exception이고, 예외처리 안 해도 됌
			
		} catch (Exception e) {

		}
		System.out.println(Thread.currentThread().getName() + " end");
	}
}

class TestRunnable2 implements Runnable {
	@Override
	public void run() { // start() 로 인해 실행
		System.out.println("쓰레드 11단계");
		thread2();
	}

	public void thread2() {
		System.out.println("쓰레드 22단계");
		thread3();
	}

	public void thread3() {
		System.out.println("쓰레드 33단계");
	}
}



// static void sleep(long msec, int nsec) throws InterruptedException
// msec 밀리초(천분의 1) + nsec 나노초(10억분의 1) 동안 대기


// void suspend - void resume 일시정시 와 다시 살리기
