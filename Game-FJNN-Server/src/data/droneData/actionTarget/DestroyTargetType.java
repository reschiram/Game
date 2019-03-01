package data.droneData.actionTarget;

import Data.Location;
import client.ComsData;
import data.map.ServerMap;

public class DestroyTargetType extends ActionTargetType {

	DestroyTargetType() {
		super(ComsData.ActionTarget_Type_Destroy);		
	}

	@Override
	public boolean canInteract(Location blockTarget, ServerMap serverMap) {
		return serverMap.hasBuildingBlock(blockTarget);
	}
}
