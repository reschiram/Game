package events.entity;

import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.module.ELModule;

public class DroneEnergyLoadUpdateEvent extends EntityEvent<Drone> {

	private double energyLevel;
	private boolean isCharging;

	public DroneEnergyLoadUpdateEvent(ELModule module) {
		super(module.getDrone(), module.getDrone().getLocation());
		
		this.energyLevel = module.getEnergyLevel();
		this.isCharging = module.isCharging();
	}

	public double getEnergyLevel() {
		return energyLevel;
	}

	public boolean isCharging() {
		return isCharging;
	}

}
