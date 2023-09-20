package gmx.chat.server;

import java.io.*;
import java.net.*;

public class ServerReceiver implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private Server server;

	public ServerReceiver(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		makeStream();
	}

	private void makeStream() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			server.showError("스트림 생성 에러", e);
		}
	}

	@Override
	public void run() {
		String name = "";
		try {
			name = input.readUTF();
			server.sendToAll(name + "님이 대화방에 접속하였습니다.");
			server.getClients().put(name, output);
			System.out.println("현재 " + server.getClients().size() + "명이 대화방에 접속 중입니다.");

			while (true) {
				server.sendToAll(input.readUTF());
			}
		} catch (IOException e) {
			System.out.println(name + "님의 연결이 끊어졌습니다: " + e.getMessage());
		} finally {
			disconnect(name);
		}
	}

	private void disconnect(String name) {
		server.getClients().remove(name);
		server.sendToAll(name + "님이 대화방에서 나갔습니다.");
		System.out.println("현재 " + server.getClients().size() + "명이 대화방에 접속 중입니다.");

		closeIO(input, "입력 스트림 해제 에러");
		closeIO(output, "출력 스트림 해제 에러");
		closeIO(socket, "소켓 해제 에러");
	}

	private void closeIO(Closeable io, String error) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				server.showError(error, e);
			}
		}
	}

}
