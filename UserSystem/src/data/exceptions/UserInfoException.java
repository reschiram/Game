package data.exceptions;

import data.user.User;

public class UserInfoException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private User user;

	public UserInfoException(User user) {
		super(null, "Error while reading user info: "+user.toStringWithPassword());
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
