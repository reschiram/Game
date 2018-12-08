package server;

import data.UserPackageManager;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;

public class ServerListener implements NewClientConnectionEventListener, ToServerMessageEventListener, ServerLostConnectionToClientEventListener{
	
	private ServerUserManager serverUserManager;
	
	ServerListener(ServerUserManager serverUserManager, ServerManager serverManager){
		this.serverUserManager = serverUserManager;
		
		serverManager.getEventManager().registerNewClientConnectionEventListener(this, 1);
		serverManager.getEventManager().registerServerMessageEventListener(this, 1);
	}
	
	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		if(event.getMessage().getId() == UserPackageManager.DataPackage_UserLogin){
			event.setActive(false);
			String loginInfo = UserPackageManager.getUserInfoFromData(event.getMessage());
			new Thread(new Runnable() {				
				@Override
				public void run() {
					serverUserManager.login(event.getClientID(), loginInfo);
				}
			}).start();
		}
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		this.serverUserManager.getConnectionManager().registerNewUnknownConnection(event.getClientID());
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		this.serverUserManager.getConnectionManager().logout(event.getClientID());
	}

}
