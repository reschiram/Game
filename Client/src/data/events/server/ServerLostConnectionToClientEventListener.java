package data.events.server;

public interface ServerLostConnectionToClientEventListener {

	public void connectionLost(ServerLostConnectionToClientEvent event);
}
