package game.entity.player.playerDrone.module;

import game.map.Map;

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
		if(this.drone.getLocation().distance_Math(this.drone.getHost().getLocation()) < Math.sqrt(2)*Map.DEFAULT_SQUARESIZE){
			this.recharge();
		}
		if(energyLoad == 0 || isLoading ){
			if(!this.drone.getPathFinder().hasTarget() || !this.drone.getPathFinder().getBlockTarget().isEqual(this.drone.getHost().getBlockLocation())){
				this.drone.getPathFinder().setBlocked(false);
				this.drone.getPathFinder().setBlockTarget(this.drone.getHost().getBlockLocation());
				this.drone.getPathFinder().setBlocked(true);
			}
			this.isLoading = true;
		}
		if(energyLoad > 0.8*maxEnergyLoad){
			this.drone.getPathFinder().setBlocked(false);
			this.isLoading = false;
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

}
