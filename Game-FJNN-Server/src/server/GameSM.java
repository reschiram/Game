package server;

public class GameSM {
	
	private ServerManager serverManager;
	private ServerUserManager serverUserManager;
	
	public GameSM(String genere) {
		this.serverManager = new ServerManager();
		this.serverUserManager = new ServerUserManager(serverManager, generalPassword)
	}

}
