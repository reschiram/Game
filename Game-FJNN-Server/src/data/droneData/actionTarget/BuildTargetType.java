package data.droneData.actionTarget;

import client.ComsData;

public class BuildTargetType extends ActionTargetType {

	private int resID;

	BuildTargetType(int resID) {
		super(ComsData.ActionTarget_Type_Build);
		this.resID = resID;
	}

	public int getResID() {
		return resID;
	}

}
