package data.events;

import data.events.client.ClientEvent;
import data.user.User;

public class ClientLoginEvent extends ClientEvent{
	
	private boolean loggedIn;
	private User user;
	
	public ClientLoginEvent(boolean loggedIn, User user) {
		super();
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
