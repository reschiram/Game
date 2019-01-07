package client;

import data.USPackageManager;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.events.client.UserValidationEvent;

public class USCEventListener implements ClientLostConnectionToServerEventListener, ToClientMessageEventListener{

	private USCManager USCManager;

	public USCEventListener(USCManager USCManager) {
		this.USCManager = USCManager;
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if(event.getMessage().getId() == USPackageManager.DataPackage_UserResponse) {
			this.USCManager.getUSCEventManager().publishEvent(new UserValidationEvent(USPackageManager.readUserResponse(event.getMessage())));
		}
	}

	@Override
	public void connectionLost(ClientLostConnectionToServerEvent event) {
		System.out.println("Lost connection to user-server.");
	}

}
