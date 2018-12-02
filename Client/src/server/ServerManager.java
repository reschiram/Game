package server;

import data.DataPackage;
import data.PackageType;
import data.events.server.NewClientConnectionEvent;
import data.events.server.ToServerMessageEvent;

public class ServerManager {
	
	private Server server;
	private ServerEventManager eventManager;
	
	public ServerManager(){
		DataPackage.loadInternalPackageIds();
		this.server = new Server(this);
		this.eventManager = new ServerEventManager();
	}

	public ServerEventManager getEventManager() {
		return this.eventManager;
	}

	public void publishNewMessageEvent(PackageType message) {
		ToServerMessageEvent event = new ToServerMessageEvent(message);
		this.eventManager.publishNewToServerMessageEvent(event);
	}

	public void publishNewClientConnectionEvent(ServerClient serverClient) {
		NewClientConnectionEvent event = new NewClientConnectionEvent(serverClient);
		this.eventManager.publishNewClientConnectionEvent(event);
	}

}
