package launcher;

import client.GameCM;
import data.Tickable;
import data.exceptions.LoginInformationCreationException;
import data.exceptions.UnsupportedPackageCreationException;
import data.exceptions.client.ServerNotFoundException;
import launcher.lobby.Lobby;
import tick.TickManager;

public class Launcher implements Tickable{
	
	public static void main(String args[]) {	    
		new Launcher(args[0], 12345);
	}
	
	private LauncherGUI gui;	
	private GameCM gameCM;
	
	private TickManager launcherTickManager;
	
	public Launcher(String ip, int port) {
		this.gui = new LauncherGUI(this);
		this.gameCM = new GameCM(this, ip, port);
		
		try {
			this.gameCM.getClientManager().connectToServer();
		} catch (ServerNotFoundException e) {
			e.printStackTrace();
		}
		
		launcherTickManager = new TickManager(this);
	}

	public void login(String username, String password) {
		try {
			this.gameCM.getClientUserManager().login(username, password);
		} catch (UnsupportedPackageCreationException | LoginInformationCreationException e) {
			this.gui.println("Login failed");
		}		
	}

	@Override
	public void tick() {				
		this.gameCM.tick();
	}

	public LauncherGUI getGUI() {
		return gui;
	}

	public GameCM getGameCM() {
		return gameCM;
	}
	
	public void connectToLobby(String userName) {
		launcherTickManager.kill();
		this.gui.destroy();
		new Lobby(gameCM, userName);
	}

}
