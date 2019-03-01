package data.droneData.droneModule;

import client.ComsData;
import data.SEPathManager;

public class SELModule extends SDroneModule{
	
	private double maxEnergyLoad;
	private double energyLoad = 0.0;
	
	private boolean isCharging = false;

	public SELModule(double maxEnergyLoad) {
		this.maxEnergyLoad = maxEnergyLoad;
	}

	@Override
	public void tick() {		
		SEPathManager dronePathManager = this.drone.getSEPathManager();
		
		if (isCharging) {			
			if ((!dronePathManager.hasTarget() || !dronePathManager.getCurrentBlockTarget().isEqual(this.drone.getDroneHost().getBlockLocation()))
						&& !this.drone.getPixelHitbox().overlaps(this.drone.getDroneHost().getPixelHitbox()))
			{
				dronePathManager.setBlockTarget(this.drone.getDroneHost().getBlockLocation(),
						ComsData.TargetLevel_Drone_Energy);
			}
		}

		if (energyLoad > 0.8 * maxEnergyLoad && isCharging) {
			
			if (dronePathManager.getCurrentTargetLevel() == ComsData.TargetLevel_Drone_Energy) {
				dronePathManager.setCurrentTargetLevel(ComsData.TargetLevel_Min);
			}
			
			this.isCharging = false;
			
			this.drone.getCurrentSMap().getMapSM().publishDroneEnergyUpdate(this.drone.getId(), energyLoad, isCharging);
		}
	}

	public void update(double energyLoad, boolean isCharging) {
		this.energyLoad = energyLoad;
		if(this.isCharging != isCharging) {
			this.isCharging = isCharging;
			
			this.drone.getCurrentSMap().getMapSM().publishDroneEnergyUpdate(this.drone.getId(), energyLoad, isCharging);
		}
	}

	public boolean isCharging() {
		return isCharging;
	}
}
