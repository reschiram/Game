package data.events.server;

public class NewClientConnectionEvent extends ServerEvent{

	public NewClientConnectionEvent(long clientID) {
		super(clientID);
	}

}
