package data.events;

import data.events.server.ServerEvent;
import server.ValidatedUser;

public class ClientConnectionValidationEvent extends ServerEvent{
	
	private boolean loggedIn;
	private ValidatedUser user;
	
	public ClientConnectionValidationEvent(boolean loggedIn, ValidatedUser user) {
		super(user.getServerClientID());
		this.loggedIn = loggedIn;
		this.user = user;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public ValidatedUser getValidatedUser() {
		return user;
	}
}
