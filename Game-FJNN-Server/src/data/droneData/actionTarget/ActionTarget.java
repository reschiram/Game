package data.droneData.actionTarget;

import Data.Location;
import data.map.ServerMap;
import data.server.Player;

public class ActionTarget {
	
	private Location blockLocation;
	private Player targetMaster;	
	private ActionTargetType type;	
	
	public ActionTarget(Location blockLocation, Player targetMaster, ActionTargetType type) {
		this.blockLocation = blockLocation;
		this.targetMaster = targetMaster;
		this.type = type;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	public Player getTargetMaster() {
		return targetMaster;
	}

	public ActionTargetType getType() {
		return type;
	}

	public int getKey(ServerMap sm) {
		return this.blockLocation.getX() + (this.blockLocation.getY() * sm.getWidth());
	}
}
