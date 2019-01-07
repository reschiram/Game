package data.exceptions;

public class NoSecurityKeyException extends CustomException {

	private static final long serialVersionUID = 1L;

	public NoSecurityKeyException() {
		super(null, "Error while trying to request user validation from server: no server key");
	}

}
