package data;

import Data.Location;
import client.ComsData;
import data.entities.ServerEntity;

public class SEPathManager {
	
	private Location currentBlockTarget;
	private int targetLevel = ComsData.TargetLevel_Min;
	
	private ServerEntity entity;
	
	public SEPathManager(ServerEntity entity) {
		this.entity = entity;
	}

	public Location getCurrentBlockTarget() {
		return currentBlockTarget.clone();
	}

	public boolean hasTarget() {
		return this.currentBlockTarget != null;
	}

	public boolean setBlockTarget(Location blockLocation, int nextTargetLevel) {
		if(nextTargetLevel >= this.targetLevel) {
			this.currentBlockTarget = blockLocation;
			this.targetLevel = nextTargetLevel;
			
			entity.getCurrentSMap().getMapSM().publishDroneTargetUpdate(this);
			return true;
		} else if (nextTargetLevel == ComsData.TargetLevel_Done) {
			this.currentBlockTarget = null;
			this.targetLevel = ComsData.TargetLevel_Min;	
			return true;
		}
		return false;
	}
	
	public boolean setBlockTargetSilent(Location blockLocation, int nextTargetLevel) {
		if(nextTargetLevel >= this.targetLevel) {
			this.currentBlockTarget = blockLocation;
			this.targetLevel = nextTargetLevel;
			
			return true;
		}
		return false;
	}
	
	public int getCurrentTargetLevel() {
		return targetLevel;
	}
	
	public void setCurrentTargetLevel(int targetLevel) {
		this.targetLevel = targetLevel;
	}

	public ServerEntity getEntity() {
		return entity;
	}
}
