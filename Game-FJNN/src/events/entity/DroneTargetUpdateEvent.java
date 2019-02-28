package events.entity;

import Data.Location;
import game.entity.player.playerDrone.Drone;

public class DroneTargetUpdateEvent extends EntityEvent<Drone> {

	private Location blockTarget;
	private int targetLevel;

	public DroneTargetUpdateEvent(Drone entity, Location blockTarget, int targetLevel) {
		super(entity, entity.getLocation());
		
		this.blockTarget = blockTarget;
		this.targetLevel = targetLevel;
	}

	public Location getBlockTarget() {
		return blockTarget;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

}
