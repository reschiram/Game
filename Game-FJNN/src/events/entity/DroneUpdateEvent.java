package events.entity;

import Data.Location;
import client.commData.DroneTargetInfos;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.DroneModule;
import game.entity.player.playerDrone.module.ELModule;

public class DroneUpdateEvent extends EntityEvent<Drone>{

	private DroneTargetInfos currentDDroneTarget;
	private DroneTargetInfos currentBDroneTarget;
	private DroneTargetInfos droneTargetInfosChange;
	private double energyLevel;
	private boolean isCharging;

	public DroneUpdateEvent(Drone drone, DroneTargetInfos droneTargetInfosChange) {
		super(drone, drone.getLocation());
		this.droneTargetInfosChange = droneTargetInfosChange;
		
		DroneModule module = this.getEntity().getModule(ELModule.class);
		if(module != null) {
			ELModule elModule = (ELModule) module;
			energyLevel = elModule.getEnergyLevel();
			isCharging = elModule.isCharging();
		} else {
			energyLevel = -1;
			isCharging = false;
		}
		
		module = this.getEntity().getModule(CTDModule.class);
		if(module != null) {
			CTDModule ctdModule = (CTDModule) module;
			this.currentDDroneTarget = ctdModule.getCurrentTarget();
		} else this.currentDDroneTarget = new DroneTargetInfos(null, false); 
		module = this.getEntity().getModule(CTBModule.class);
		if(module != null) {
			CTBModule ctbModule = (CTBModule) module;
			this.currentBDroneTarget = ctbModule.getCurrentTarget();
		} else this.currentBDroneTarget = new DroneTargetInfos(null, false);
	}

	public DroneUpdateEvent(Drone drone, double energyLevel, boolean isCharging,
			DroneTargetInfos currentDDroneTarget, DroneTargetInfos currentBDroneTarget, DroneTargetInfos droneTargetInfosChange, Location location) {
		super(drone, location);
		
		this.energyLevel = energyLevel;
		this.isCharging = isCharging;
		this.droneTargetInfosChange = droneTargetInfosChange;
		this.currentDDroneTarget = currentDDroneTarget;
		this.currentBDroneTarget = currentBDroneTarget;
	}

	public DroneTargetInfos getDroneTargetInfosChange() {
		return droneTargetInfosChange;
	}

	public double getDroneEnergy() {
		return energyLevel;
	}

	public boolean isDroneCharging() {
		return isCharging;
	}

	public DroneTargetInfos getCurrentDDroneTarget() {
		return currentDDroneTarget;
	}

	public DroneTargetInfos getCurrentBDroneTarget() {
		return currentBDroneTarget;
	}

}
