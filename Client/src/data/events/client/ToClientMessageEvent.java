package data.events.client;

import data.PackageType;

public class ToClientMessageEvent extends ClientEvent{
	
	private PackageType message;	

	public ToClientMessageEvent(PackageType message) {
		super();
		this.message = message;
	}

	public PackageType getMessage() {
		return message;
	}
	
}
