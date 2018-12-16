package data.events;

public interface AccessDeniedEventListner {
	
	public void handleNoPermissionException(AccessDeniedEvent event);

}
