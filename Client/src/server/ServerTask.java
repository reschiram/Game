package server;

public interface ServerTask {
	
	public void act(ServerClient sc, ServerManager serverManager);
	
	public long getClientID();

}
