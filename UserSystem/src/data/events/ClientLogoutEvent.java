package data.events;

import data.events.server.ServerEvent;
import server.ValidatedUser;

public class ClientLogoutEvent extends ServerEvent{

	private ValidatedUser user; 
	
	public ClientLogoutEvent(long clientID, ValidatedUser user) {
		super(clientID);
		this.user = user;
	}

	public ValidatedUser getValidatedUser() {
		return user;
	}

}
