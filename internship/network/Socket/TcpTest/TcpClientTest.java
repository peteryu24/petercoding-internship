package gmx.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class TcpClientTest {

	public static void main(String args[]) {
		try {
			Socket s;
			InputStream is;
			DataInputStream dis;
			OutputStream os;
			DataOutputStream dos;
			String sendString = "I love JEJUDO!(client -> server)";
			s = new Socket("localhost", 5432); // 접속할 서버의 ip주소와 포트 정보로 소켓을 생성하여 서버에 연결 요청 (localhost = 127.0.0.1)
			is = s.getInputStream();
			dis = new DataInputStream(is); // 데이터 가져오기 ("Welcome to connect to TCP Server!(server -> ent)")
			String str = new String(dis.readUTF()); // 문자열을 읽음(stream을 이용할 때 사용)
			System.out.println(str); // "Welcome to connect to TCP Server!(server -> ent)"
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
