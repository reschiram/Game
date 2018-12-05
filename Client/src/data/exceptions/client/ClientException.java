package data.exceptions.client;

import client.Client;
import data.exceptions.CustomException;

public class ClientException extends CustomException{

	private static final long serialVersionUID = 1L;
	
	private Client client;

	public ClientException(Exception exception, String message, Client client) {
		super(exception, message);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
