package client;

import data.USPackageManager;
import data.exceptions.client.ServerNotFoundException;

public class USCManager {
	
	private ClientManager clientManager;
	private USCEventListener eventListener;	
	private USCEventManager eventManager;
	
	public USCManager(String ip, int port) throws ServerNotFoundException {
		this.eventManager = new USCEventManager();		
		
		this.clientManager = new ClientManager(ip, port);		
		this.clientManager.connectToServer(true);
		
		this.eventListener = new USCEventListener(this);
		this.clientManager.getEventManager().registerClientLostConnectionToServerEventListener(eventListener, 2);
		this.clientManager.getEventManager().registerClientMessageEventListener(eventListener, 2);
	}
	
	public void askForUser(long clientID, String userInfo) {		
		this.clientManager.sendToServer(USPackageManager.createUserRequestPackage(userInfo, clientID));
	}

	public USCEventManager getUSCEventManager() {
		return eventManager;
	}
	
	public void tick() {
		this.eventManager.tick();
		this.clientManager.getEventManager().tick();
		this.eventManager.tick();
	}

}
