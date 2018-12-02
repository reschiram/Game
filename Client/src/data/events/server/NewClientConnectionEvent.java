package data.events.server;

import server.ServerClient;

public class NewClientConnectionEvent extends ServerEvent{
	
	ServerClient serverClient;

	public NewClientConnectionEvent(ServerClient serverClient) {
		super();
		this.serverClient = serverClient;
	}

	public ServerClient getServerClient() {
		return serverClient;
	}

}
