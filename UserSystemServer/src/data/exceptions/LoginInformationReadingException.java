package data.exceptions;

import java.math.BigInteger;

import data.user.User;

public class LoginInformationReadingException extends CustomException {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String loginInformation;
	private BigInteger mod;
	
	public LoginInformationReadingException(Exception exception, User user, String loginInformation, BigInteger mod) {
		super(exception, "An error occured while reading the login-information: \""+loginInformation+"\" with the user: "+user+" and mod = "+mod.toString());
		this.user = user;
		this.loginInformation = loginInformation;
		this.mod = mod;
	}

	public User getUser() {
		return user;
	}

	public String getLoginInformation() {
		return loginInformation;
	}

	public BigInteger getMod() {
		return mod;
	}

}
