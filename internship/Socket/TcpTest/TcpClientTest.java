package gmx.server;

import java.net.*;
import java.io.*;

public class TcpClientTest {

	public static void main(String args[]) {
		try {
			Socket s;
			InputStream is;
			DataInputStream dis;
			OutputStream os;
			DataOutputStream dos;
			String sendString = "I love JEJUDO!(client -> server)";
			s = new Socket("127.0.0.1", 5432);
			is = s.getInputStream();
			dis = new DataInputStream(is);
			String str = new String(dis.readUTF()); // 문자열을 읽음
			System.out.println(str); // 23번 라인 다음 출력
			os = s.getOutputStream();
			dos = new DataOutputStream(os);
			dos.writeUTF("<전송 시작>" + sendString + "<전송 마침>");
			os.close();
			is.close();
			dis.close();
			dos.close();
			// s.close();
		} catch (ConnectException connExc) {
			System.err.println("서버 연결 실패");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
