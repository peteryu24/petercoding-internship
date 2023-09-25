package gmx.multichatroom.room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Helper implements Runnable {
	private Socket socket;
	private DataOutputStream dos;
	private String name;
	private ChatRoom chatRoom;

	public Helper(Socket socket) {
		this.socket = socket; // 서버에서 가져온 소켓

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

					choice = Integer.parseInt(chooseAction); // String으로 입력했을 때를 대비해서 String으로 받고 int로 형변환

					if (choice < 1 || choice > 2) {
						sendMessage("올바른 번호를 선택하세요 (1 또는 2).");
						continue;
					}
				} catch (NumberFormatException e) { // String,float 입력시
					sendMessage("올바르지 않은 입력입니다. 숫자를 입력해 주세요.");
					continue;
				}

				switch (choice) {
				case 1: // 신규 방
					sendMessage("방 이름을 입력하세요: ");
					String roomName = dis.readUTF(); // 방 이름 받아오기

					chatRoom = ChatRoom.createRoom(roomName); // 받아온 이름으로 방 생성
					if (chatRoom != null) {
						chatRoom.addPerson(this); // helper객체로 방에 입장
						sendMessage("방 생성 완료. 현재 입장한 방 이름: " + roomName + "\n(exit 입력시 퇴장)");
					} else { // createRoom()에서 return null; 일 때
						sendMessage("해당 이름의 방이 이미 존재합니다.");
					}
					break;
				case 2: // 기존 방
					sendMessage("개설된 방 목록: " + ChatRoom.getRoomName());
					sendMessage("입장 희망하는 방 이름을 입력하세요: ");

					String chooseRoom = dis.readUTF(); // 원하는 방 이름 받아오기

					chatRoom = ChatRoom.enterRoom(chooseRoom); // 입력했던 이름을 가진 방으로 입장
					if (chatRoom != null) {
						chatRoom.addPerson(this);
						sendMessage("방 입장 완료. 현재 입장한 방 이름: " + chooseRoom);
					} else {
						sendMessage("방 조회되지 않음.");
					}
					break;
				}

				while (chatRoom != null) { // exit 하기까지
					String roomMessage = dis.readUTF(); // 메세지 받아오기

					if (roomMessage.equalsIgnoreCase("exit")) { // 대소문자 구별 없는 equals
						chatRoom.removePerson(name);
						chatRoom = null; // while문 탈출
						sendMessage("방에서 나왔습니다.");
					} else {
						chatRoom.sendToAll(name + ": " + roomMessage, name); // 보낸 사람: 메세지 , 나 빼고 수신할 수 있게

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					System.err.println("outputStream Close 에러");
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					System.err.println("inputStream Close 에러");
				}
			}
			if (socket != null && !socket.isClosed()) { // 소켓을 닫을 때는 연결 상태와 닫힌 상태를 동시에 체크
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println("Socket Close 에러");
				}
			}
		}
	}
}
