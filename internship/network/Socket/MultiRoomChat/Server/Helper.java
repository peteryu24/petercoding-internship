package gmx.multichatroom.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import gmx.multiroomchat.server.Server;

public class Helper implements Runnable {
	private Socket socket;
	private Server server;
	private OutputStream out;
	private String name;
	private ChatRoom currentRoom;

	public Helper(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public void sendMessage(String message) {
		try {
			if (out != null) {
				out.write((message + "\n").getBytes());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			out = socket.getOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;

			sendMessage("이름을 입력하세요: ");
			bytesRead = in.read(buffer);
			name = new String(buffer, 0, bytesRead).trim();

			while (true) {
				sendMessage("1. 신규 방 생성");
				sendMessage("2. 기존 방 입장");
				sendMessage("입력: ");

				bytesRead = in.read(buffer);
				int choice = Integer.parseInt(new String(buffer, 0, bytesRead).trim());

				switch (choice) {
				case 1:
					sendMessage("방 이름을 입력하세요: ");
					bytesRead = in.read(buffer);
					String roomName = new String(buffer, 0, bytesRead).trim();

					currentRoom = server.createRoom(roomName);
					if (currentRoom != null) {
						currentRoom.addClient(this);
						sendMessage("방 생성 완료. 현재 입장한 방 이름: " + roomName);
					} else {
						sendMessage("해당 이름의 방이 이미 존재합니다.");
					}
					break;
				case 2:
					sendMessage("개설된 방 목록: " + server.getAllRoomNames());
					sendMessage("입장 희망하는 방 이름을 입력하세요: ");

					bytesRead = in.read(buffer);
					String selectedRoomName = new String(buffer, 0, bytesRead).trim();

					currentRoom = server.getRoom(selectedRoomName);
					if (currentRoom != null) {
						currentRoom.addClient(this);
						sendMessage("방 입장 완료. 현재 입장한 방 이름: " + selectedRoomName);
					} else {
						sendMessage("방 조회되지 않음.");
					}
					break;
				}

				while (currentRoom != null) {
					bytesRead = in.read(buffer);
					String message = new String(buffer, 0, bytesRead).trim();

					if (message.equalsIgnoreCase("exit")) {
						currentRoom.removeClient(name);
						sendMessage("방을 나갔습니다.");
						currentRoom = null;
					} else {
						currentRoom.broadcast(name + ": " + message, name);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
