package events.entity.drone;

import Data.Location;
import game.entity.player.playerDrone.Drone;

public class TargetEventDU extends DroneUpdateEvent{

	private Location blockTarget;
	private int targetLevel;

	public TargetEventDU(Drone drone, Location blockTarget, int targetLevel) {
		super(drone);
		
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
