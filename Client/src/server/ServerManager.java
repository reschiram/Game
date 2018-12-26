package server;

import java.util.ArrayList;

import data.DataPackage;
import data.Queue;
import data.exceptions.server.InvalidServerClientIDException;
import data.exceptions.server.ServerPortException;

public class ServerManager {
	
	private Server server;
	private ServerEventManager eventManager;
	
	public ServerManager(){
		DataPackage.loadInternalPackageIds();
		this.server = new Server(this);
		this.eventManager = new ServerEventManager();
	}
	
	public void openConnection() throws ServerPortException{
		this.server.openConnection();		
	}

	public ServerEventManager getEventManager() {
		return this.eventManager;
	}
	
	public void sendMessage(long id, Queue<DataPackage> message) throws InvalidServerClientIDException{
		if(server.getConnectedClients().contains(id)) this.server.sendToServerClient(new ServerMessage(id, message));
		else throw new InvalidServerClientIDException(id);
	}

	public boolean isConnected() {
		return server.isConnected();
	}
	
	public void tick(){
		this.server.tick();
		this.eventManager.tick();
	}
	
	public ArrayList<Long> getConnectedClients(){
		return server.getConnectedClients();
	}

	public void addServerTask(ServerTask serverTask) throws InvalidServerClientIDException {
		if(server.getConnectedClients().contains(serverTask.getClientID())) this.server.addServerTask(serverTask);
		else throw new InvalidServerClientIDException(serverTask.getClientID());
	}

}
