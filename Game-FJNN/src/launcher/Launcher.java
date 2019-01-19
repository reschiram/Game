package launcher;

import client.GameCM;
import data.Tickable;
import data.exceptions.LoginInformationCreationException;
import data.exceptions.UnsupportedPackageCreationException;
import data.exceptions.client.ServerNotFoundException;

public class Launcher implements Tickable{
	
	public static void main(String args[]) {
		new Launcher(args[0], 12345);
	}
	
	private LauncherGUI gui;	
	private GameCM gameCM;
	
	public Launcher(String ip, int port) {
		this.gui = new LauncherGUI(this);
		this.gameCM = new GameCM(ip, port);
		
		try {
			this.gameCM.getClientManager().connectToServer();
		} catch (ServerNotFoundException e) {
			e.printStackTrace();
		}
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
	

}
