package gmx.multichatroom.room;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;

import gmx.multiroomchat.server.Server;

public class ChatRoom {
	Server server = Server.getInstance();

	private ConcurrentMap<String, Helper> clients = new ConcurrentHashMap<>();

	public ChatRoom() { // 채팅방 이름 설정
	}

	public void addPerson(Helper helper) {
		clients.put(helper.getName(), helper); // 사용자의 이름과 helper 자체
	}

	public void removePerson(String name) {
		clients.remove(name);
	}

	public void sendToAll(String message, String sender) {
		for (Helper person : clients.values()) {
			if (!person.getName().equals(sender)) { // 자신일 경우 전송 X
				person.sendMessage(message);
			}
		}
	}

	public ChatRoom createRoom(String roomName) { // helper에서 받아온 이름
		if (server.roomManager.containsKey(roomName)) { // 이미 존재한다면
			return null;
		}
		ChatRoom room = new ChatRoom(); // 새로운 채팅방 생성
		server.roomManager.put(roomName, room); // 해시맵에 방 추가
		return room;
	}

	public ChatRoom enterRoom(String roomName) { // Helper클래스에서 방 입장을 위해 방 정보 return
		return server.roomManager.get(roomName);
	}

	public ArrayList<String> getRoomName() {
		return new ArrayList<>(server.roomManager.keySet());
	}
}
