package gmx.tcptest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class TcpClientTest {

	public static void clientRun() {
		Socket socket = null;

		try {
			// 서버에 연결
			socket = new Socket("localhost", 5432);
			System.out.println("서버에 연결되었습니다.");
			// 서버와 통신 처리
			withServer(socket);
		} catch (ConnectException connExc) { // 연결 실패시
			System.err.println("서버 연결 실패"); // err 사용. 표준 출력 스트림이 아니라 표준 오류 스트림에 출력
		} catch (IOException e) {
		} finally {
			closeSocket(socket);
		}
	}

	// 서버와의 통신 처리 메서드
	private static void withServer(Socket socket) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			DataInputStream dis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);

			// 서버로부터 인사 메시지 수신
			String serverMessage = dis.readUTF(); // server에서 메세지 수신
			System.out.println(serverMessage);

			// 클라이언트에서 서버로 메시지 전송
			String sendString = "I love JEJUDO! (client -> server)";
			dos.writeUTF("<전송 시작>" + sendString + "<전송 마침>"); // server에 메세지 전송
		} catch (IOException e) {
			throw new RuntimeException("데이터 송수신 과정 에러 발생");
		} finally {
			try {
				if (os != null)
					os.close();
				if (is != null)
					is.close();
			} catch (IOException e) {

			}
		}
	}

	// 소켓 닫기 메서드
	private static void closeSocket(Socket socket) {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}

/*
 * 서버 소켓을 사용하여 서버의 특정 포트에서 클라이언트의 연결요청을 처리할 준비 
 * 클라리언트의 연결 요청을 받으면 새로운 소켓을 생성하여 클라이언트의 소켓과 연결되도록 한다
 * 연결이 성공하면 서버소켓과 상관없이 1:1 통신을 한다
 * 단방향 통신인 Http통신과 다르게 소켓은 양방향.(소켓으로 동시에 수행은 불가)
 * 
 * Http통신은  client가 요청을 보내는 경우에만 server가 응답(역순은 불가)
 * stream은 일방향의 특징을 가지는 데이터 흐름이다. (데이터가 이동하는 통로)
 * 외부에서 데이터를 읽거나 외부로 데이터를 출력하는 작업에 사용
 * 다양한 데이터 소스를 표준화된 방법으로 다루기 위한 것
 * 하나의 스트림으로 동시에 입출력 불가능
 * 역방향으로 데이터가 전송될 수 없기 때문에 input, output을 위한 코드가 따로 존재해야함.
 */
