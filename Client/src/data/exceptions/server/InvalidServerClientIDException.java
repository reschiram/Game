package data.exceptions.server;

import data.exceptions.CustomException;

public class InvalidServerClientIDException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private long id;

	public InvalidServerClientIDException(long id) {
		super(null, "No client was found with the clientID: "+id);
		this.id = id;
	}

	public long getId() {
		return id;
	}

}
