package client.commData;

import Data.Location;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;
import game.entity.player.playerDrone.DroneTarget;

public class DroneTargetInfos {
	
	private int resID;
	private Location blockLoc;
	private boolean isDone;
	private boolean isBuild;
	private boolean isNull;
	private boolean doAdd;
	
	public DroneTargetInfos(int resID, Location blockLoc, boolean isDone, boolean isBuild, boolean isNull,
			boolean doAdd) {
		super();
		this.resID = resID;
		this.blockLoc = blockLoc;
		this.isDone = isDone;
		this.isBuild = isBuild;
		this.isNull = isNull;
		this.doAdd = doAdd;
	}

	public DroneTargetInfos(DroneTarget droneTarget, boolean add) {
		if(droneTarget instanceof BDroneTarget) {
			BDroneTarget target = (BDroneTarget) droneTarget;
			this.resID = target.getID();
			this.blockLoc = target.getBlockLocation().clone();
			this.isDone = target.isDone();
			this.isBuild = true;
			this.isNull = false;
			this.doAdd = add;
		} else if(droneTarget instanceof DDroneTarget) {
			DDroneTarget target = (DDroneTarget) droneTarget;
			this.resID = -1;
			this.blockLoc = target.getBlockLocation().clone();
			this.isDone = target.isDone();
			this.isBuild = false;
			this.isNull = false;
			this.doAdd = add;
		} else if(droneTarget == null) {
			this.resID = 0;
			this.blockLoc = new Location(0, 0);
			this.isDone = true;
			this.isBuild = false;
			this.isNull = true;
			this.doAdd = add;
		}
	}

	public int getResID() {
		return resID;
	}

	public Location getBlockLocation() {
		return blockLoc;
	}

	public boolean isDone() {
		return isDone;
	}

	public boolean isBuild() {
		return isBuild;
	}

	public boolean isNull() {
		return isNull;
	}

	public boolean doAdd() {
		return doAdd;
	}

}
