package data.events.server;

import data.PackageType;

public class ToServerMessageEvent extends ServerEvent{
		
	private PackageType message;	

	public ToServerMessageEvent(long clientID, PackageType message) {
		super(clientID);
		this.message = message;
	}

	public PackageType getMessage() {
		return message;
	}
}
