package data.droneData.actionTarget;

import Data.Location;
import client.ComsData;
import data.map.ServerMap;

public class BuildTargetType extends ActionTargetType {

	private int resID;

	BuildTargetType(int resID) {
		super(ComsData.ActionTarget_Type_Build);
		this.resID = resID;
	}

	public int getResID() {
		return resID;
	}

	@Override
	public boolean canInteract(Location blockTarget, ServerMap serverMap) {
		return !serverMap.hasBuildingBlock(blockTarget);
	}

}
