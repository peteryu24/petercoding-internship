package gmx.test;

public class Main {

	public static void main(String[] args) {
		// 서버 실행
		ServerThread serverThread = new ServerThread();
		serverThread.start();

		// 클라이언트 실행
		ClientThread clientThread = new ClientThread();
		clientThread.start();
	}

	private static class ServerThread extends Thread {
		@Override
		public void run() {
			TcpServerTest.serverRun();
		}
	}

	private static class ClientThread extends Thread {
		@Override
		public void run() {
			TcpClientTest.clientRun();
		}
	}
}

/*
 * stream은 일방향의 특징을 가지는 데이터 흐름이다. (데이터가 이동하는 통로)
 * 외부에서 데이터를 읽거나 외부로 데이터를 출력하는 작업에 사용
 * 다양한 데이터 소스를 표준화된 방법으로 다루기 위한 것
 * 하나의 스트림으로 동시에 입출력 불가능
 * 역방향으로 데이터가 전송될 수 없기 때문에 input, output을 위한 코드가 따로 존재해야함.
 * 서버 소켓을 사용하여 서버의 특정 포트에서 클라이언트의 연결요청을 처리할 준비 
 * 클라리언트의 연결 요청을 받으면 새로운 소켓을 생성하여 클라이언트의 소켓과 연결되도록 한다
 * 연결이 성공하면 서버소켓과 상관없이 1:1 통신을 한다
 * 
 * 단방향 통신인 Http통신과 다르게 소켓은 양방향.(소켓으로 동시에 수행은 불가)
 * Http통신은  client가 요청을 보내는 경우에만 server가 응답(역순은 불가)
 */
