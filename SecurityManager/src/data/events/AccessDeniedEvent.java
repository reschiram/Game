package data.events;

import data.events.client.ClientEvent;

public class AccessDeniedEvent extends ClientEvent {
	
	private int packageID;
	private String noPermissionExceptionInformation;
	
	public AccessDeniedEvent(int packageID, String noPermissionExceptionInformation) {
		this.packageID = packageID;
		this.noPermissionExceptionInformation = noPermissionExceptionInformation;
	}
	
	public int getPackageID() {
		return packageID;
	}
	public String getNoPermissionExceptionInformation() {
		return noPermissionExceptionInformation;
	}

}
