package server;

import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import main.ServerMain;

public class GameSEM implements ServerLostConnectionToClientEventListener, NewClientConnectionEventListener, ToServerMessageEventListener, ClientConnectionValidationEventListener, ClientLogoutEventListener {
	
	private ServerMain serverMain;
	
	public GameSEM(ServerMain serverMain) {
		this.serverMain = serverMain;
		
		this.serverMain.getGameSM().getServerManager().getEventManager().registerNewClientConnectionEventListener(this, 4);
		this.serverMain.getGameSM().getServerManager().getEventManager().registerServerLostConnectionToClientEventListener(this, 4);
		this.serverMain.getGameSM().getServerManager().getEventManager().registerServerMessageEventListener(this, 4);
		
		this.serverMain.getGameSM().getServerUserManager().getUserEventManager().registerClientConnectionValidationEventListener(this, 4);
		this.serverMain.getGameSM().getServerUserManager().getUserEventManager().registerClientLogoutEventListener(this, 4);
	}
	
	@Override
	public void clientLogout(ClientLogoutEvent event) {
		this.serverMain.getServerStructureManager().logOut(event.getValidatedUser());
		
		this.serverMain.getConsoleManager().getGUI().println("Client: " + event.getClientID()
				+ " has logged out. Client has been validated as User: " + event.getValidatedUser().toString());
	}

	@Override
	public void handleClientLogginIn(ClientConnectionValidationEvent event) {
		if(!event.isLoggedIn()) {
			this.serverMain.getConsoleManager().getGUI().println("Client: " + event.getClientID()
					+ " has tried to validate as "+event.getValidatedUser().toString()+".");
			return;
		}
		this.serverMain.getServerStructureManager().logIn(event.getValidatedUser());
		
		this.serverMain.getConsoleManager().getGUI().println("Client: " + event.getClientID()
				+ " has been validated. Client has been validated as User: " + event.getValidatedUser().toString());
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		this.serverMain.getConsoleManager().getGUI().println("Client: " + event.getClientID()
			+ " has connected to the server.");
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		this.serverMain.getConsoleManager().getGUI().println("Client: " + event.getClientID()
		+ " has lost connection to the server.");
		
	}

}
