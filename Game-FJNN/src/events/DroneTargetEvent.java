package events;

import Data.Location;
import game.entity.player.playerDrone.DroneHost;

public class DroneTargetEvent extends GameEvent {
	
	private DroneHost host;
	private Location blockLoc;
	private boolean isBuild;
	private boolean isAdd;
	private int resID;
		
	public DroneTargetEvent(DroneHost host, Location blockLoc, boolean isBuild, boolean isAdd,int resID) {
		super();
		this.host = host;
		this.blockLoc = blockLoc;
		this.isBuild = isBuild;
		this.isAdd = isAdd;
		this.resID = resID;
	}

	public DroneHost getHost() {
		return host;
	}
	
	public Location getBlockLoc() {
		return blockLoc;
	}
	
	public boolean isBuild() {
		return isBuild;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public int getResID() {
		return isAdd ? resID : -1;
	}
}
