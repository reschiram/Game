package server;

import java.util.ArrayList;

import data.DataPackage;
import data.Queue;
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
	
	public void sendMessage(long id, Queue<DataPackage> message){
		this.server.sendToServerClient(new ServerMessage(id, message));
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

}
