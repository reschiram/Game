package data.exceptions.client;

import client.Client;

public class ServerNotFoundException extends ClientException{

	private static final long serialVersionUID = 1L;

	public ServerNotFoundException(Exception exception, Client client, String ip, int port) {
		super(exception, "No Server found with the IP-Adress:"+ip+" at the Port: "+port, client);
	}

}
