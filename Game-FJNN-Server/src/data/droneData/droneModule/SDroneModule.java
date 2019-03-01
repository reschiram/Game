package data.droneData.droneModule;

import data.entities.ServerDroneEntity;

public abstract class SDroneModule{

	protected ServerDroneEntity drone;
	
	public abstract void tick();

	public SDroneModule register(ServerDroneEntity drone){
		this.drone = drone;
		return this;
	}
	
}
