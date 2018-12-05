package server;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.server.NewClientConnectionEvent;
import data.events.server.ToServerMessageEvent;
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

	public void publishNewMessageEvent(PackageType message, long clientID) {
		ToServerMessageEvent event = new ToServerMessageEvent(clientID, message);
		this.eventManager.publishNewToServerMessageEvent(event);
	}

	public void publishNewClientConnectionEvent(ServerClient serverClient) {
		NewClientConnectionEvent event = new NewClientConnectionEvent(serverClient);
		this.eventManager.publishNewClientConnectionEvent(event);
	}
	
	public void sendMessage(long id, Queue<DataPackage> message){
		this.server.sendToServerClient(new ServerMessage(id, message));
	}

}
