package game.entity.player.playerDrone.module;

import events.GameEventManager;
import events.entity.DroneUpdateEvent;

public class ELModule extends DroneModule{
	
	private double maxEnergyLoad;
	private double energyLoad;
	private double rechargeSpeed;
	private double energyConsumption;
	
	private boolean isLoading;
	
	public ELModule(double energyLoad, double rechargeSpeed, double energyConsumption){
		this.maxEnergyLoad = energyLoad;
		this.rechargeSpeed = rechargeSpeed;
		this.energyConsumption = energyConsumption;
	}

	@Override
	public void tick() {
		useEnergy();
		if(this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox())){
			this.recharge();
		}
		if((energyLoad == 0 || isLoading)){
			if((!this.drone.getPathController().hasTarget() || !this.drone.getPathController().getBlockTarget().isEqual(this.drone.getHost().getBlockLocation()))
					&& !this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox())){
				
				this.drone.getPathController().setBlocked(false);
				this.drone.getPathController().setBlockTarget(this.drone.getHost().getBlockLocation(), DroneModule.publishPathToServer);
				this.drone.getPathController().setBlocked(true);
			}
			this.isLoading = true;
			
			GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, null));
		}
		if(energyLoad > 0.8*maxEnergyLoad){
			this.drone.getPathController().setBlocked(false);
			this.isLoading = false;
			
			GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, null));
		}
	}

	private void useEnergy() {
		this.energyLoad-=energyConsumption;
		if(this.energyLoad<0)this.energyLoad = 0;
	}

	private void recharge() {
		this.energyLoad+=rechargeSpeed;
		if(this.energyLoad>maxEnergyLoad)this.energyLoad = maxEnergyLoad;
	}

	public double getEnergyLevel() {
		return energyLoad;
	}
	
	public boolean isCharging() {
		return isLoading;
	}

	public void updateEnergyLoad(double droneEnergy, boolean droneCharging) {
		this.energyLoad = droneEnergy;
		this.isLoading = droneCharging;
		this.tick();
	}

}
