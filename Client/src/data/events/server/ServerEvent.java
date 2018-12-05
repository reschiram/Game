package data.events.server;

import data.events.Event;

public class ServerEvent extends Event{
	
	private long clientID;
	
	public ServerEvent(long clientID){
		super();
		this.clientID = clientID;
	}

	public long getClientID() {
		return clientID;
	}

}
