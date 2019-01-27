package client;

import launcher.Launcher;

public class GameCM {
	
	private ClientManager cm;
	private ClientUserManager cum;
	private ClientSecurityManager csm;
	
	private LauncherCEM launcherCEM;
	private GameCPM gameCPM;
	
	public GameCM(Launcher launcher, String ip, int port) {
		this.cm = new ClientManager(ip, port);
		this.cum = new ClientUserManager(cm);
		this.csm = new ClientSecurityManager(cm);
		
		this.launcherCEM = new LauncherCEM(launcher);
		this.cm.getEventManager().registerClientLostConnectionToServerEventListener(this.launcherCEM, 4);
		this.cm.getEventManager().registerClientMessageEventListener(this.launcherCEM, 4);
		this.cum.getUserEventManager().registerClientLoginEventListener(this.launcherCEM, 4);
		this.csm.geClientSecurityEventManager().registerAccessDeniedEventListener(this.launcherCEM, 4);
		
		this.gameCPM = new GameCPM();
		this.gameCPM.loadPackages();
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

	public GameCPM getClientPackageManager() {
		return gameCPM;
	}

	public void tick() {
		this.cm.getEventManager().tick();
		this.cum.getUserEventManager().tick();
		this.csm.tick();
	}

}
