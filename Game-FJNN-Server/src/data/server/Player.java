package data.server;

import server.ValidatedUser;

public class Player{
	
	private boolean isHost;
	private ValidatedUser user;

	public Player(ValidatedUser user, boolean isHost) {
		this.user = user;
		this.isHost = isHost;
	}

	public boolean isHost() {
		return isHost;
	}

	public ValidatedUser getUser() {
		return user;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public String getID() {
		return user.getID();
	}

	public long getServerClientID() {
		return user.getServerClientID();
	}

	public String getUsername() {
		return user.getUsername();
	}

}
