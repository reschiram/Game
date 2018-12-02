package data.events.client;

public interface ToClientMessageEventListener {
	
	public void messageFromServer(ToClientMessageEvent event);

}
