package data.exceptions;

public class UnknownUserException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private String userID;

	public UnknownUserException(String userID) {
		super(null, "Error while searching for user: \""+userID+"\". User is not known.");
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}
	
}
