package gmx.tcptest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerTest {

	public static void serverRun() {
		ServerSocket serverSocket = createSs(5432); // 5432 포트

		while (true) { // 무한 루프 소켓 종료 후 또 접속 가능
			Socket socket = connectClient(serverSocket);

			if (socket != null) { // 소켓이 반환되면
				withClient(socket); // client 와 송수신
				closeSocket(socket); // 소켓 닫기
			}
		}
	}

	private static ServerSocket createSs(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port); // 5432 포트로 서버 소켓 생성
			System.out.println("서버가 시작되었습니다.");
			return serverSocket;
		} catch (IOException e) { // 서버 소켓 생성 에러
			e.printStackTrace();
			throw new RuntimeException("서버 소켓 생성 중 오류 발생");
		}
	}

	private static Socket connectClient(ServerSocket serverSocket) {
		try {
			System.out.println("클라이언트 대기중 ....");
			Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 대기
			System.out.println("클라이언트 접속 성공!");
			return clientSocket;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("클라이언트 접속 중 오류 발생");
		}
	}

	private static void withClient(Socket clientSocket) {
		OutputStream os = null;
		InputStream is = null;
		try {
			os = clientSocket.getOutputStream();
			is = clientSocket.getInputStream();
			DataOutputStream dos = new DataOutputStream(os);
			DataInputStream dis = new DataInputStream(is);
			dos.writeUTF("Welcome to connect to TCP Server!(server -> client)"); // 메세지 전송
			String fromClient = dis.readUTF(); // 클라이언트로부터 메시지 수신
			System.out.println(fromClient);
		} catch (IOException e) {
		} finally {
			try {
				if (os != null)
					os.flush();
					os.close();
				if (is != null)
					is.close();
			} catch (IOException e) {

			}
		}
	}

	private static void closeSocket(Socket socket) {
		if (socket != null && !socket.isClosed()) { // 소켓이 존재하고 닫혀있지 않으면
			try {
				socket.close();
				System.out.println("성공적으로 소켓이 닫혔습니다.");
			} catch (IOException e) {
			}
		}
	}

}
