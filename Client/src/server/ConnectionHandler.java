package server;

import java.net.Socket;
import java.util.HashMap;

public class ConnectionHandler {
	
	private Server server;
	
	private HashMap<Long, ServerClient> connectedConnections = new HashMap<>();
	private long lastID = 1l;

	public ConnectionHandler(Server server) {
		this.server = server;
	}
	
	public ServerClient registerNewConnection(Socket client){
		Long id = generateNewClientID();	
		ServerClient sc = new ServerClient(this.server, client, id);
		this.connectedConnections.put(id, sc);
		return sc;
	}

	private long generateNewClientID() {
		lastID++;
		return lastID-1;
	}
	
	public ServerClient getServerClient(long id){
		return this.connectedConnections.get(id);
	}

}
