package data.exceptions;

public class LoginInformationCreationException extends CustomException{
	
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	public LoginInformationCreationException(Exception exception, String username, String password) {
		super(exception, "Failed to create user login-information from: username = \""+username+"\", password = \""+password.charAt(0)+"****** \"");
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
