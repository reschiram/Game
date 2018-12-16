package client;

import data.SecurityPackageManager;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;

public class ClientSecurityManager implements ToClientMessageEventListener {
	
	private ClientSecurityEventManager clientSecurityEventManager;
	
	public ClientSecurityManager(ClientManager clientManager){
		this.clientSecurityEventManager = new ClientSecurityEventManager();
		
		clientManager.getEventManager().registerClientMessageEventListener(this, 1);
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if(event.getMessage().getId() == SecurityPackageManager.DataPackage_NoPermission){
			event.setActive(false);
			this.clientSecurityEventManager.publishEvent(SecurityPackageManager.getPackageFromAccessDeniedMessage(event.getMessage()));
		}
	}
	
	public ClientSecurityEventManager geClientSecurityEventManager(){
		return this.clientSecurityEventManager;
	}
	
	public void tick(){
		this.clientSecurityEventManager.tick();
	}

}
