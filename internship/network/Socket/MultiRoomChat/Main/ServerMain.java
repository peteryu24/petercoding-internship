package gmx.multiroomchat.main;

import gmx.multiroomchat.server.Server;

public class ServerMain {
	public static void main(String[] args) {
		Server server = Server.getInstance(); // 싱글톤 인스턴스를 가져옴
		server.startServer(); // 서버 시작

	}
}
