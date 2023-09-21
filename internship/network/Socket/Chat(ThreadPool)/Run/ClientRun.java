package gmx.chat;

import gmx.chat.client.ClientServer;

public class ClientRun {
	public static void main(String[] args) {
		ClientServer cs = new ClientServer();
		try {
			cs.start();
		} catch (Exception e) {
			System.out.println("클라이언트 서버 시작 에러");
		}
	}
}
