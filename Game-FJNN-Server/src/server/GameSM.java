package server;

import java.util.ArrayList;

import data.DataPackage;
import data.PackageType;
import data.ServerMap;
import data.exceptions.server.InvalidServerClientIDException;
import data.exceptions.server.ServerPortException;

public class GameSM {
	
	private ServerManager serverManager;
	private ServerUserManager serverUserManager;
	private ServerSecurityManager securityManager;
	
	private GameSPM gameSPM;
	
	public GameSM(String generalPassword) {
		this.serverManager = new ServerManager();
		this.serverUserManager = new ServerUserManager(serverManager, generalPassword);
		this.securityManager = new ServerSecurityManager(serverManager, serverUserManager);
		
		this.gameSPM = new GameSPM();
		this.gameSPM.loadPackages();
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

	public ArrayList<Long> getAllConnectedClients() {
		return this.serverManager.getConnectedClients();
	}

	public void sendMapToClient(long clientID, ServerMap serverMap) {
		try {
			System.out.println("Start sending Map");			
			long time = System.currentTimeMillis();
			PackageType[] messages = this.gameSPM.getMapMessages(serverMap.getGenerationData());
			for(int i = 0; i < messages.length; i++) {
				this.serverManager.sendMessage(clientID, DataPackage.getPackage(messages[i]));
			}
			System.out.println("Sending completed. Time: "+(System.currentTimeMillis() - time)+"ms");
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}		
	}
}
