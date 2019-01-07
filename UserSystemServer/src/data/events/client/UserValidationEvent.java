package data.events.client;

import data.events.Event;
import data.user.ValidatedUser;

public class UserValidationEvent extends Event {
	
	private ValidatedUser user;

	public UserValidationEvent(ValidatedUser user) {
		super();
		this.user = user;
	}

	public ValidatedUser getUser() {
		return user;
	}
	
	public boolean isSuccessful(){
		return !this.user.getID().equals(ValidatedUser.NoUserID);
	}

}
