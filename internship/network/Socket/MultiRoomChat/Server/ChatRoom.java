package gmx.multiroomchat.room;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {

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
}
