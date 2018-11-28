package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerClient {
	
	private Socket connection;
	private OutputStream out;
	private InputStream in;
	private Long id;

	public ServerClient(Socket client, Long id) {
		this.connection = client;
		this.id = id;
		
		try {
			this.out = client.getOutputStream();
			this.in = client.getInputStream();
		} catch (IOException e) {e.printStackTrace();}
	}

	public void acceptConnection() {
	}

}
