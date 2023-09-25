package gmx.multichatroom.room;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

import gmx.multiroomchat.server.Server;

public class ChatRoom {

	Server server = Server.getInstance(); // 싱글톤 패턴으로 Server의 인스턴스 가져오기

	private final ConcurrentHashMap<String, Helper> clients = new ConcurrentHashMap<>(); // 사람 이름과 사람(소켓을 가짐)

	public void addPerson(Helper helper) {
		clients.put(helper.getName(), helper); // 사용자의 이름과 helper 자체
	}

	public void removePerson(String name) {
		clients.remove(name);
		if (clients.isEmpty()) { // 아무도 없는 방일 경우
			Server.roomManager.remove(Helper.roomDelete); // 방 삭제
		}
	}

	public void sendToAll(String message, String sender) {
		for (Helper person : clients.values()) {
			if (!person.getName().equals(sender)) { // 자신일 경우 전송 X
				person.sendMessage(message);
			}
		}
	}

	public static ChatRoom createRoom(String roomName) { // helper에서 받아온 이름
		if (Server.roomManager.containsKey(roomName)) { // 이미 존재한다면
			return null;
		}
		ChatRoom room = new ChatRoom(); // 새로운 채팅방 생성
		Server.roomManager.put(roomName, room); // 해시맵에 방 추가
		return room;
	}

	public static ChatRoom enterRoom(String roomName) { // Helper클래스에서 사용자의 방 입장을 위해 방 정보 return
		return Server.roomManager.get(roomName);
	}

	public static ArrayList<String> getRoomName() { // ArrayList에 방 이름 넣기
		return new ArrayList<>(Server.roomManager.keySet());
	}
}
