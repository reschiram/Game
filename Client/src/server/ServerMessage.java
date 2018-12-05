package server;

import data.DataPackage;
import data.Queue;

public class ServerMessage {
	
	private long id;
	private Queue<DataPackage> message;
	
	public ServerMessage(long id, Queue<DataPackage> message) {
		this.id = id;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public Queue<DataPackage> getMessage() {
		return message;
	}

}
