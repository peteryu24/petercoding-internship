package gmx.multichatroom.room;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {
	private String name;
	private ConcurrentMap<String, Helper> clients = new ConcurrentHashMap<>();

	public ChatRoom(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addClient(Helper client) {
		clients.put(client.getName(), client);
	}

	public void removeClient(String name) {
		clients.remove(name);
	}

	public void broadcast(String message, String excludeClient) {
		for (Helper client : clients.values()) {
			if (!client.getName().equals(excludeClient)) {
				client.sendMessage(message);
			}
		}
	}
}
