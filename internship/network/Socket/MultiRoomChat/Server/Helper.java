package gmx.multiroomchat.room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import gmx.multiroomchat.server.Server;

public class Helper implements Runnable {
	private Socket socket;
	private Server server;
	private DataOutputStream dos;
	private String name;
	private ChatRoom chatRoom;

	public Helper(Socket socket, Server server) {
		this.socket = socket; // 서버에서 가져온 소켓
		this.server = server;
	}

	public String getName() { // 방에 입장시키거나 broadcast할 때
		return name; // clients 해시맵에 Key로 추가될 이름, broadcast할 때 나 빼고 전송 할 때
	}

	public void sendMessage(String message) { // 클라이언트한테 보냄
		try {
			if (dos != null) {
				dos.writeUTF(message); // 전송
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			sendMessage("이름을 입력하세요: ");
			name = dis.readUTF(); // 이름 받아오기

			while (true) {
				sendMessage("1. 신규 방 생성");
				sendMessage("2. 기존 방 입장");
				sendMessage("입력: ");

				String chooseAction = dis.readUTF(); // 선택 받아오기

				int choice;
				try {
					choice = Integer.parseInt(chooseAction);

					if (choice < 1 || choice > 2) {
						sendMessage("올바른 번호를 선택하세요 (1 또는 2).");
						continue;
					}
				} catch (NumberFormatException e) {
					sendMessage("올바르지 않은 입력입니다. 숫자를 입력해 주세요.");
					continue;
				}

				switch (choice) {
				case 1: // 신규 방
					sendMessage("방 이름을 입력하세요: ");
					String roomName = dis.readUTF(); // 방 이름 받아오기

					chatRoom = server.createRoom(roomName); // 받아온 이름으로 방 생성
					if (chatRoom != null) {
						chatRoom.addPerson(this); // helper객체로 방에 입장
						sendMessage("방 생성 완료. 현재 입장한 방 이름: " + roomName);
					} else {
						sendMessage("해당 이름의 방이 이미 존재합니다.");
					}
					break;
				case 2: // 기존 방
					sendMessage("개설된 방 목록: " + server.getRoomName());
					sendMessage("입장 희망하는 방 이름을 입력하세요: ");

					String chooseRoom = dis.readUTF(); // 원하는 방 이름 받아오기

					chatRoom = server.enterRoom(chooseRoom); // 입력했던 이름을 가진 방으로 입장
					if (chatRoom != null) {
						chatRoom.addPerson(this);
						sendMessage("방 입장 완료. 현재 입장한 방 이름: " + chooseRoom);
					} else {
						sendMessage("방 조회되지 않음.");
					}
					break;
				}

				while (chatRoom != null) {
					String roomMessage = dis.readUTF(); // 메세지 받아오기

					if (roomMessage.equalsIgnoreCase("exit")) {
						chatRoom.removePerson(name);
						chatRoom = null;
						sendMessage("방에서 나갑니다.");
					} else {
						chatRoom.sendToAll(name + ": " + roomMessage, name); // 보낸 사람: 메세지 , 나 빼고 수신할 수 있게

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
