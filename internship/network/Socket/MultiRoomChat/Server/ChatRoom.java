package gmx.multiroomchat.room;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {
	private String name;
	private ConcurrentMap<String, Helper> clients = new ConcurrentHashMap<>();

	public ChatRoom(String name) { // 채팅방 이름 설정
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addClient(Helper helper) {
		clients.put(helper.getName(), helper); // 사용자의 이름과 helper 자체
	}

	public void removeClient(String name) {
		clients.remove(name);
	}

	public void broadcast(String message, String sender) {
		for (Helper client : clients.values()) {
			if (!client.getName().equals(sender)) { // 자신일 경우 전송 X
				client.sendMessage(message);
			}
		}
	}
}
