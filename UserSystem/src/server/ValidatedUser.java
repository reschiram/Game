package server;

import data.user.User;

public class ValidatedUser {
	
	private long serverClientID;
	private User user;	
	
	public ValidatedUser(long serverClientID, User user) {
		this.serverClientID = serverClientID;
		this.user = user;
	}
	
	public long getServerClientID() {
		return serverClientID;
	}
	public User getUser() {
		return user;
	}

}
