package data.exceptions;

public class UserDatabaseReadingException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private int line;

	public UserDatabaseReadingException(Exception exception, int line) {
		super(exception, "An error occured wile trying to read line: "+line+" in the user database");
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}
