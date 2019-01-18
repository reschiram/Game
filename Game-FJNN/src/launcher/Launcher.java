package launcher;

import client.GameCM;
import data.exceptions.client.ServerNotFoundException;

public class Launcher {
	
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
		
		
	}
	

}
