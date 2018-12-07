package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionHandler {
	
	private Server server;
	
	private HashMap<Long, ServerClient> connectedConnections = new HashMap<>();
	private long lastID = 1l;

	public ConnectionHandler(Server server) {
		this.server = server;
	}
	
	ServerClient registerNewConnection(Socket client){
		Long id = generateNewClientID();	
		ServerClient sc = new ServerClient(this.server, client, id);
		this.connectedConnections.put(id, sc);
		return sc;
	}

	long generateNewClientID() {
		lastID++;
		return lastID-1;
	}
	
	ServerClient getServerClient(long id){
		return this.connectedConnections.get(id);
	}

	@SuppressWarnings("unchecked")
	public void tick() {
		ArrayList<ServerClient> clients = (ArrayList<ServerClient>) new ArrayList<>(connectedConnections.values()).clone();
		for(int i = 0; i<clients.size(); i++){
			ServerClient client = clients.get(i);
			if(client.isConnected())client.tick();
			else{
				this.connectedConnections.remove(client.getID());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Long> getConnectedClients() {
		return (ArrayList<Long>) new ArrayList<>(connectedConnections.keySet()).clone();
	}

}
