package data.events.client;

public interface ClientLostConnectionToServerEventListener {
	
	public void connectionLost(ClientLostConnectionToServerEvent event);

}
