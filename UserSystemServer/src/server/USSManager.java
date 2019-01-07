package server;

import data.exceptions.server.ServerPortException;

public class USSManager {
	
	private ServerManager serverManager;
	
	public USSManager(String generallPassword, String adminKey) {
		this.serverManager = new ServerManager();
		Server.Port = 12346;
		
		try {
			this.serverManager.openConnection(true);
		} catch (ServerPortException e) {
			e.printStackTrace();
		}
	}

}
