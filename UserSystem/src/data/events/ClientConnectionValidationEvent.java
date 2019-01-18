package data.events;

import data.events.server.ServerEvent;
import data.user.User;
import server.ValidatedUser;

public class ClientConnectionValidationEvent extends ServerEvent{
	
	private boolean loggedIn;
	private User user;
	private long clientID;
	
	public ClientConnectionValidationEvent(long clientID, boolean loggedIn, User user) {
		super(clientID);
		this.loggedIn = loggedIn;
		this.user = user;
		this.clientID = clientID;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public User getUser() {
		return user;
	}

	public ValidatedUser getValidatedUser() {
		return new ValidatedUser(clientID, user);
	}

	public long getClientID() {
		return clientID;
	}
}
