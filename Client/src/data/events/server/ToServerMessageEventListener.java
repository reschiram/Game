package data.events.server;

public interface ToServerMessageEventListener {

	public void messageFromClient(ToServerMessageEvent event);

}
