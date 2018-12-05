package data.events.client;

import java.net.Socket;

public class ClientLostConnectionToServerEvent extends ClientEvent{
	
	private boolean connected;
	private boolean closed;
	private boolean ended;

	public ClientLostConnectionToServerEvent(Socket connection) {
		super();
		if(connection == null){
			this.connected = false;
			this.closed = false;
			this.ended = true;
		}else{
			this.connected = connection.isConnected();
			this.closed = connection.isClosed();
			this.ended = false;
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean isEnded() {
		return ended;
	}

}
