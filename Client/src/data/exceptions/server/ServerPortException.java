package data.exceptions.server;

import data.exceptions.CustomException;

public class ServerPortException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private int port;

	public ServerPortException(Exception exception, int port) {
		super(exception, "Server could not open Connection on Port:"+port+". Maybe a nother server is already running on that Port.");
		this.port = port;
	}

	public int getPort() {
		return port;
	}

}
