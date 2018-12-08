package data.events;

import data.events.server.ServerEvent;
import data.user.User;

public class ClientLogoutEvent extends ServerEvent{

	private User user; 
	
	public ClientLogoutEvent(long clientID, User user) {
		super(clientID);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
