package data.exceptions;

import data.user.User;

public class UserAlreadyKnwonException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private User user;

	public UserAlreadyKnwonException(User user) {
		super(null, "Error while adding new User: \""+user.getUsername()+"\". User is already known.");
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
