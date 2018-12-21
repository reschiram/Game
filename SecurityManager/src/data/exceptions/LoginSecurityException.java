package data.exceptions;

import server.ValidatedUser;

public class LoginSecurityException extends CustomException {

	private static final long serialVersionUID = 1L;

	private ValidatedUser loginUser;
	private ValidatedUser validatedUser;

	public LoginSecurityException(Exception exception, ValidatedUser loginUser, ValidatedUser validatedUser) {
		super(exception, "Could not create a security context for "+loginUser.toString()+". Reason: User already has a security context: "+validatedUser.toString());
		this.loginUser = loginUser;
		this.validatedUser = validatedUser;
	}
	
	public ValidatedUser getLoginUser() {
		return loginUser;
	}

	public ValidatedUser getValidatedUser() {
		return validatedUser;
	}

}
