package client;

public class GameCM {
	
	private ClientManager cm;
	private ClientUserManager cum;
	private ClientSecurityManager csm;
	
	public GameCM(String ip, int port) {
		this.cm = new ClientManager(ip, port);
		this.cum = new ClientUserManager(cm);
		this.csm = new ClientSecurityManager(cm);
	}
	
	public ClientManager getClientManager() {
		return cm;
	}

	public ClientUserManager getClientUserManager() {
		return cum;
	}

	public ClientSecurityManager getClientSecurityManager() {
		return csm;
	}

	public void tick() {
		this.cm.getEventManager().tick();
		this.cum.getUserEventManager().tick();
		this.csm.tick();
	}

}
