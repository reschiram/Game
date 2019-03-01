package data.droneData;

import client.ComsData;
import data.droneData.droneModule.SCTModule;
import data.droneData.droneModule.SDroneModule;
import data.droneData.droneModule.SELModule;
import data.entities.ServerDroneEntity;

public class SDroneConstructor {

//	public static final int DefaultDrone_StarterDrone = 1;
	public static final int DefaultDrone_BuildDrone = 2;
	public static final int DefaultDrone_DestructionDrone = 3;

//	public static SDroneModule[] constructStarterDrone(ServerDroneEntity drone) {
//		return new SDroneModule[] {new SCTModule().register(drone), new SELModule(100.0).register(drone)};
//	}

	public static SDroneModule[] constructStarterBuildDrone(ServerDroneEntity drone) {
		return new SDroneModule[] {new SCTModule(ComsData.ActionTarget_Type_Build).register(drone), new SELModule(100.0).register(drone)};
	}

	public static SDroneModule[] constructStarterDestructionDrone(ServerDroneEntity drone) {
		return new SDroneModule[] {new SCTModule(ComsData.ActionTarget_Type_Destroy).register(drone), new SELModule(100.0).register(drone)};
	}

	public static SDroneModule[] constructDrone(ServerDroneEntity drone, int droneType) {
//		if(droneType == DefaultDrone_StarterDrone) return constructStarterDrone(drone);
		if(droneType == DefaultDrone_BuildDrone) return constructStarterBuildDrone(drone);
		else if(droneType == DefaultDrone_DestructionDrone) return constructStarterDestructionDrone(drone);
		return new SDroneModule[0];
	}

}
