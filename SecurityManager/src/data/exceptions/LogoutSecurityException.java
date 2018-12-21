package data.exceptions;

import server.ValidatedUser;

public class LogoutSecurityException extends CustomException {
	
	private static final long serialVersionUID = 1L;

	private ValidatedUser loginUser;

	public LogoutSecurityException(Exception exception, ValidatedUser loginUser) {
		super(exception, "Could not remove security context for "+loginUser.toString()+". Reason: No SecurityContext found.");
		this.loginUser = loginUser;
	}
	
	public ValidatedUser getLoginUser() {
		return loginUser;
	}
}
