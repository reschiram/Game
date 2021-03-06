package game.entity.player.playerDrone.module;

import game.entity.player.playerDrone.Drone;

public abstract class DroneModule {
	
	public static final boolean publishPathToServer = false;
	
	protected Drone drone;
	
	public abstract void tick();

	public void register(Drone drone){
		this.drone = drone;
	}
}
