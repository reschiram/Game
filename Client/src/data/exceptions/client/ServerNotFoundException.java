package data.exceptions.client;

public class ServerNotFoundException extends ClientException{

	private static final long serialVersionUID = 1L;

	public ServerNotFoundException(Exception exception, String ip, int port) {
		super(exception, "No Server found with the IP-Adress:"+ip+" at the Port: "+port);
	}

}
