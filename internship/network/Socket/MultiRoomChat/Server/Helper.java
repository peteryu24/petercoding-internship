package gmx.multiroomchat.room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import gmx.multiroomchat.server.Server;

public class Helper implements Runnable {
	private Socket socket;
	private Server server;
	private DataOutputStream out;
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
				out.writeUTF(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		DataInputStream in = null;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			sendMessage("이름을 입력하세요: ");
			name = in.readUTF();

			while (true) {
				sendMessage("1. 신규 방 생성");
				sendMessage("2. 기존 방 입장");
				sendMessage("입력: ");

				String userInput = in.readUTF();

				int choice;
				try {
					choice = Integer.parseInt(userInput);

					if (choice < 1 || choice > 2) {
						sendMessage("올바른 번호를 선택하세요 (1 또는 2).");
						continue;
					}
				} catch (NumberFormatException e) {
					sendMessage("올바르지 않은 입력입니다. 숫자를 입력해 주세요.");
					continue;
				}

				switch (choice) {
				case 1:
					sendMessage("방 이름을 입력하세요: ");
					String roomName = in.readUTF();

					currentRoom = server.createRoom(roomName);
					if (currentRoom != null) {
						currentRoom.addClient(this);
						sendMessage("방 생성 완료. 현재 입장한 방 이름: " + roomName);
					} else {
						sendMessage("해당 이름의 방이 이미 존재합니다.");
					}
					break;
				case 2:
					sendMessage("개설된 방 목록: " + server.getRoomName());
					sendMessage("입장 희망하는 방 이름을 입력하세요: ");

					String selectedRoomName = in.readUTF();

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
					String roomMessage = in.readUTF();

					if (roomMessage.equalsIgnoreCase("exit")) {
						currentRoom.removeClient(name);
						currentRoom = null;
						sendMessage("방에서 나갑니다.");
					} else {
						currentRoom.broadcast(name + ": " + roomMessage, name);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
