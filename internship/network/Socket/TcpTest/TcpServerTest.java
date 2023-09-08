package gmx.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerTest { // 서버가 먼저 실행되어 클라이언트의 연결 요청을 기다림

	public static void main(String args[]) {
		/*
		 * 서버 소켓을 사용하여 서버의 특정 포트에서 클라이언트의 연결요청을 처리할 준비 
		 * 클라리언트의 연결 요청을 받으면 새로운 소켓을 생성하여 클라이언트의 소켓과 연결되도록 한다
		 * 연결이 성공하면 서버소켓과 상관없이 1:1 통신을 한다
		 * 
		 * 단방향 통신인 Http통신과 다르게 소켓은 양방향.(소켓으로 동시에 수행은 불가)
		 * Http통신은  client가 요청을 보내는 경우에만 server가 응답(역순은 불가)
		 */
		ServerSocket ss = null;
		Socket s;
		OutputStream os; // 소켓에 대한 출력 스트림 반환 (데이터를 출력할 때 - 최상위 클래스)
		DataOutputStream dos; // (데이터를 출력할 때 - 하위 스트림 클래스)
		InputStream is; // 소켓 객체로부터 입력할 수 있는 InputStream객체를 반환 (데이터를 입력할 때)
		DataInputStream dis; // (데이터를 입력할 때 - 하위 스트림 클래스)
		try {
			ss = new ServerSocket(5432); // 이름 null 루프백 주소(자신을 나타내는 가상의 주소)로 저장. 포트 번호 5001
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				System.out.println("클라이언트 대기중 ....");
				/*
				 * 서버소켓이 클라이언트의 연결요청을 처리할 수 있도록 대기 상태로 만든다 
				 */
				s = ss.accept();
				System.out.println("클라이언트 접송 성공!");
				os = s.getOutputStream(); // 쓰기
				dos = new DataOutputStream(os); // DataOutputStream으로 데이터 내보내기
				dos.writeUTF("Welcome to connect to TCP Server!(server -> ent)"); // 문자열 쓰기
				is = s.getInputStream(); // 소켓에 대한 입력 스트림 반환 (읽기)
				dis = new DataInputStream(is); // 데이터 가져오기 ("<전송 시작>" + sendString + "<전송 마침>")
				String str = new String(dis.readUTF()); // 문자열 읽기
				System.out.println(str); // "<전송 시작>" + sendString + "<전송 마침>"

				// client와의 접속을 끝는 메소드 close()

				is.close(); // 소켓 객체를 닫음
				dis.close();
				os.close();
				dos.close();
				s.close();  // inputstream 과 outputstream이 close()되면 소켓도 함께 close()되는데 굳이 이유 
				// ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
/*
 * stream은 일방향의 특징을 가지는 데이터 흐름이다. (데이터가 이동하는 통로)
 * 외부에서 데이터를 읽거나 외부로 데이터를 출력하는 작업에 사용
 * 다양한 데이터 소스를 표준화된 방법으로 다루기 위한 것
 * 하나의 스트림으로 동시에 입출력 불가능
 * 역방향으로 데이터가 전송될 수 없기 때문에 input, output을 위한 코드가 따로 존재해야함.
 */

// netstat -ano
// netstat -ano / find "1234"
// taskkill /f /pid 1234

//socket에서 serversocket 으로 넘어가서 client가 접속했는지 확인 후, 재빨리 socket.accept()를 실행

// Cannot invoke "java.net.ServerSocket.isClosed()" because "s1" is null
