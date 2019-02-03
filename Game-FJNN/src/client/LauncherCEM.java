package client;

import data.events.AccessDeniedEvent;
import data.events.AccessDeniedEventListner;
import data.events.ClientLoginEvent;
import data.events.ClientLoginEventListener;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import launcher.Launcher;

public class LauncherCEM implements ClientLostConnectionToServerEventListener, ToClientMessageEventListener, ClientLoginEventListener, AccessDeniedEventListner{
	
	private Launcher launcher;
	
	public LauncherCEM(Launcher launcher) {
		this.launcher = launcher;
	}

	@Override
	public void connectionLost(ClientLostConnectionToServerEvent event) {
	}

	@Override
	public void handleNoPermissionException(AccessDeniedEvent event) {
	}

	@Override
	public void ClientLogin(ClientLoginEvent event) {
		if(event.isLoggedIn()) {
			launcher.getGUI().println("You were succesfully logged in as: " + event.getUser().getUsername());
			launcher.connectToLobby(event.getUser().getUsername());
			event.setActive(false);
		} else {
			launcher.getGUI().println("Login falied for username: " + event.getUser().getUsername() +". Password and or username are wrong!");	
			event.setActive(false);		
		}
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
	}

}
