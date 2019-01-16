package server;

import data.exceptions.server.ServerPortException;

public class GameSM {
	
	private ServerManager serverManager;
	private ServerUserManager serverUserManager;
	private ServerSecurityManager securityManager;
	
	public GameSM(String generalPassword) {
		this.serverManager = new ServerManager();
		this.serverUserManager = new ServerUserManager(serverManager, generalPassword);
		this.securityManager = new ServerSecurityManager(serverManager, serverUserManager);
	}
	
	public void tick() {
		this.serverManager.tick();
		this.serverUserManager.tick();
	}

	public ServerManager getServerManager() {
		return serverManager;
	}

	public ServerUserManager getServerUserManager() {
		return serverUserManager;
	}

	public ServerSecurityManager getSecurityManager() {
		return securityManager;
	}
	
	public void openConnection() throws ServerPortException {
		this.serverManager.openConnection();
	}
}
