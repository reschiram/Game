package data.droneData.actionTarget;

import Data.Location;
import data.map.ServerMap;

public abstract class ActionTargetType {
	
	private int typeId;	
	
	public ActionTargetType(int typeId) {
		this.typeId = typeId;
	}

	public static ActionTargetType getBuildType(int resID) {
		return new BuildTargetType(resID);
	}
	
	public static ActionTargetType getDestroyType() {
		return new DestroyTargetType();
	}

	public int getTypeId() {
		return typeId;
	}

	public abstract boolean canInteract(Location blockTarget, ServerMap serverMap);

}
