package data.user;

import data.user.User;

public class ValidatedUser extends User{
	
	public static final String NoUserID = "000000000000000000000000"; 
	
	private long serverClientID;
	
	public ValidatedUser(long serverClientID, String id, String name) {
		super(id, name, "*****");
		this.serverClientID = serverClientID;
	}
	
	public ValidatedUser(long serverClientID, User user) {
		super(user.getID(), user.getUsername(), "*****");
		this.serverClientID = serverClientID;
	}

	public long getServerClientID() {
		return serverClientID;
	}

}
