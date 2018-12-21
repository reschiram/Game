package server;

import data.DefaultUserExceptionHandler;
import data.UserPackageManager;
import data.events.ClientLogoutEvent;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.ClientLoginException;

public class ServerListener implements NewClientConnectionEventListener, ToServerMessageEventListener, ServerLostConnectionToClientEventListener{
	
	private ServerUserManager serverUserManager;
	
	ServerListener(ServerUserManager serverUserManager, ServerManager serverManager){
		this.serverUserManager = serverUserManager;
		
		serverManager.getEventManager().registerNewClientConnectionEventListener(this, 1);
		serverManager.getEventManager().registerServerMessageEventListener(this, 1);
		serverManager.getEventManager().registerServerLostConnectionToClientEventListener(this, 1);
	}
	
	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		if(event.getMessage().getId() == UserPackageManager.DataPackage_UserLogin){
			event.setActive(false);
			String loginInfo = UserPackageManager.getUserInfoFromData(event.getMessage());
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try {
						serverUserManager.login(event.getClientID(), loginInfo);
					} catch (ClientLoginException e) {
						DefaultUserExceptionHandler.getDefaultUserExceptionHandler().getDefaultHandler_ClientLoginException().handleError(e);
					}
				}
			}).start();
		}else if(event.getMessage().getId() == UserPackageManager.DataPackage_UserLogout){
			event.setActive(false);
			ValidatedUser currentUser = serverUserManager.getValidatedUser(event.getClientID());
			if(currentUser != null){
				serverUserManager.getConnectionManager().logout(event.getClientID());
				serverUserManager.getUserEventManager().publishEvent(new ClientLogoutEvent(event.getClientID(), currentUser));
			}
		}
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		this.serverUserManager.getConnectionManager().registerNewUnknownConnection(event.getClientID());
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		ValidatedUser currentUser = serverUserManager.getValidatedUser(event.getClientID());
		if(currentUser != null){
			this.serverUserManager.getConnectionManager().disconnect(event.getClientID());
			serverUserManager.getUserEventManager().publishEvent(new ClientLogoutEvent(event.getClientID(), currentUser));
		}
	}

}
