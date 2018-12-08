package data.events;

import data.events.server.ServerEvent;
import data.user.User;

public class ClientConnectionValidationEvent extends ServerEvent{
	
	private boolean loggedIn;
	private User user;
	
	public ClientConnectionValidationEvent(long clientID, boolean loggedIn, User user) {
		super(clientID);
		this.loggedIn = loggedIn;
		this.user = user;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public User getUser() {
		return user;
	}
}
