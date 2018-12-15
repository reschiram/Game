package data.exceptions.client;

import data.exceptions.CustomException;

public class ClientException extends CustomException{

	private static final long serialVersionUID = 1L;

	public ClientException(Exception exception, String message) {
		super(exception, message);
	}

}
