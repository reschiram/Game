package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionHandler {
	
	private Server server;
	
	private HashMap<Long, ServerClient> connectedConnections = new HashMap<>();
	private ArrayList<Long> connections = new ArrayList<>();
	private long lastID = 1l;
	
	private boolean inUse = false;

	public ConnectionHandler(Server server) {
		this.server = server;
	}
	
	ServerClient registerNewConnection(Socket client){
		Long id = generateNewClientID();	
		ServerClient sc = new ServerClient(this.server, client, id);
		
		waitForInUse();
		
		this.connections.add(id);
		this.connectedConnections.put(id, sc);
		
		endInUse();
		return sc;

	}

	private void waitForInUse() {
		if(inUse){
			synchronized (server) {
				try {
					server.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}		
		
		inUse = true;
	}
	
	private void endInUse(){
		inUse = false;		
		
		synchronized (server) {
			server.notify();
		}		
	}

	long generateNewClientID() {
		lastID++;
		return lastID-1;
	}
	
	ServerClient getServerClient(long id){		
		waitForInUse();
		
		ServerClient sc = this.connectedConnections.get(id);
		
		endInUse();
		return sc;
	}

	public void tick() {		
		waitForInUse();
		
		for(int i = 0; i<connections.size(); i++){
			ServerClient client = connectedConnections.get(connections.get(i));
			if(client.isConnected())client.tick();
			else{
				this.connections.remove(client.getID());
				this.connectedConnections.remove(client.getID());
			}
		}
		
		endInUse();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Long> getConnectedClients() {		
		waitForInUse();
		
		ArrayList<Long> cc = (ArrayList<Long>) connections.clone();
		
		endInUse();
		return cc;
	}
	
	void flushAll() {
		waitForInUse();
		
		for(long id : this.connectedConnections.keySet()) {
			this.connectedConnections.get(id).flush();
		}

		endInUse();
	}

}
