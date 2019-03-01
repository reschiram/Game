package events.entity.drone;

import Data.Location;
import events.entity.EntityEvent;
import game.entity.player.playerDrone.Drone;

public abstract class DroneUpdateEvent extends EntityEvent<Drone>{

	public DroneUpdateEvent(Drone drone, Location currentPixelLoc) {
		super(drone, currentPixelLoc);
	}
	
	public DroneUpdateEvent(Drone drone) {
		super(drone, drone.getLocation());
	}
	

}
