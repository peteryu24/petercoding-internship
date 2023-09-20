package gmx.chat;

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
		initStreams();
	}

	private void initStreams() {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			server.displayError("통신 스트림 생성 중 오류 발생", e);
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
			clientDisconnect(name);
		}
	}

	private void clientDisconnect(String name) {
		server.getClients().remove(name);
		server.sendToAll(name + "님이 대화방에서 나갔습니다.");
		System.out.println("현재 " + server.getClients().size() + "명이 대화방에 접속 중입니다.");

		closeResource(input, "입력 스트림 해제 중 오류 발생");
		closeResource(output, "출력 스트림 해제 중 오류 발생");
		closeResource(socket, "소켓 해제 중 오류 발생");
	}

	private void closeResource(Closeable resource, String errorMsg) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				server.displayError(errorMsg, e);
			}
		}
	}
}
