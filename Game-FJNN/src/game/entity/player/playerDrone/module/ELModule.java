package game.entity.player.playerDrone.module;

import client.SCUpdateManager;

public class ELModule extends DroneModule{
	
	private double maxEnergyLoad;
	private double energyLoad;
	private double rechargeSpeed;
	private double energyConsumption;
	
	private boolean isLoading;
	
	private SCUpdateManager scum;
	
	public ELModule(double energyLoad, double rechargeSpeed, double energyConsumption){
		this.maxEnergyLoad = energyLoad;
		this.rechargeSpeed = rechargeSpeed;
		this.energyConsumption = energyConsumption;
		
		this.scum = new SCUpdateManager(SCUpdateManager.Update_Type_Drone_EnergyLoad, this);
	}

	@Override
	public void tick() {
		useEnergy();
		
		if(this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox())){
			this.recharge();
		}
		
		if (this.energyLoad < 0.01) {
			this.isLoading = true;
			scum.update(true);
		}else scum.update(false);
	}

	private void useEnergy() {
		this.energyLoad -= energyConsumption;
		if (this.energyLoad < 0) this.energyLoad = 0;
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
	}

}
