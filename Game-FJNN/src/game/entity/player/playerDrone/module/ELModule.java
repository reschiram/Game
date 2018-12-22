package game.entity.player.playerDrone.module;

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
//		System.out.println("Overlaps: "+this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox()));
		if(this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox())){
			this.recharge();
		}
		if((energyLoad == 0 || isLoading)){
			if((!this.drone.getPathController().hasTarget() || !this.drone.getPathController().getTarget().isEqual(this.drone.getHost().getCenterLocation())) && !this.drone.getHitbox().overlaps(this.drone.getHost().getHitbox())){
				this.drone.getPathController().setBlocked(false);
				this.drone.getPathController().setTarget(this.drone.getHost().getCenterLocation());
				this.drone.getPathController().setBlocked(true);
			}
			this.isLoading = true;
		}
		if(energyLoad > 0.8*maxEnergyLoad){
			this.drone.getPathController().setBlocked(false);
			this.isLoading = false;
		}
	}

	private void useEnergy() {
		this.energyLoad-=energyConsumption;
		if(this.energyLoad<0)this.energyLoad = 0;
	}

	private void recharge() {
//		System.out.println("Recharge: "+this.energyLoad+" + "+rechargeSpeed+" > "+maxEnergyLoad);
		this.energyLoad+=rechargeSpeed;
		if(this.energyLoad>maxEnergyLoad)this.energyLoad = maxEnergyLoad;
	}

}
