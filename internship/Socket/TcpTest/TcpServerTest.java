package gmx.server;

import java.net.*;
import java.io.*;

public class TcpServerTest {

	public static void main(String args[]) {
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
				s = ss.accept(); // 새끼 socket 넘겨줌 (socket에서 serversocket으로 넘어와서 client 접속 유무 확인 후 빠르게 실행)
				System.out.println("클라이언트 접송 성공!");
				os = s.getOutputStream();
				dos = new DataOutputStream(os);
				dos.writeUTF("Welcom to connect to TCP Server!(server -> ent)");
				is = s.getInputStream(); // 소켓에 대한 입력 스트림 반환
				dis = new DataInputStream(is);
				String str = new String(dis.readUTF());
				System.out.println(str); // 27번 라인 다음 출력
				is.close(); // 소켓 객체를 닫음
				dis.close();
				os.close();
				dos.close();
				s.close();
				//ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

// netstat -ano
// netstat -ano / find "1234"
// taskkill /f /pid 1234

//socket에서 serversocket 으로 넘어가서 client가 접속했는지 확인 후, 재빨리 socket.accept()를 실행

// Cannot invoke "java.net.ServerSocket.isClosed()" because "s1" is null
