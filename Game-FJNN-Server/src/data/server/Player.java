package data.server;

import data.droneData.actionTarget.ActionTargetManager;
import data.map.ServerMap;
import server.ValidatedUser;

public class Player{
	
	private boolean isHost;
	private ValidatedUser user;
	
	private ActionTargetManager actionTargetManager;
	private ServerMap map;
	
	public Player(ValidatedUser user, boolean isHost) {
		this.user = user;
		this.isHost = isHost;
		
		this.actionTargetManager = new ActionTargetManager(this);
	}

	public boolean isHost() {
		return isHost;
	}

	public ValidatedUser getUser() {
		return user;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public String getID() {
		return user.getID();
	}

	public long getServerClientID() {
		return user.getServerClientID();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public ActionTargetManager getActionTargetManager() {
		return actionTargetManager;
	}

	public ServerMap getCurrentSMap() {
		return map;
	}

	public void setCurrentSMap(ServerMap map) {
		this.map = map;
	}

}
