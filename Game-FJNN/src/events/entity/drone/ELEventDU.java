package events.entity.drone;

import game.entity.player.playerDrone.module.ELModule;

public class ELEventDU extends DroneUpdateEvent {

	private double energyLevel;
	private boolean isCharging;

	public ELEventDU(ELModule module, double energyLevel, boolean isCharging) {
		super(module.getDrone());
		
		this.energyLevel = energyLevel;
		this.isCharging = isCharging;
	}

	public ELEventDU(ELModule module) {
		super(module.getDrone());
		
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
