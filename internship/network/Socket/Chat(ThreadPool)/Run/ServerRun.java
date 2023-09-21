package gmx.chat;

import gmx.chat.server.Server;

public class ServerRun {
	public static void main(String[] args) {
		Server s = new Server();
		try {		
			s.start();
		} catch (Exception e) {
			System.out.println("서버 시작 에러");
		}
	}
}
