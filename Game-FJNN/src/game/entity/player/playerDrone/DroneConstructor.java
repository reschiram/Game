package game.entity.player.playerDrone;

import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.ELModule;
import game.entity.player.playerDrone.module.ICModule;
import game.entity.player.playerDrone.module.IRModule;
import game.entity.player.playerDrone.module.InvModule;

public class DroneConstructor {

	public static final int DefaultDrone_StarterDrone = 1;
	public static final int DefaultDrone_BuildDrone = 2;
	public static final int DefaultDrone_DestructionDrone = 3;

	public static Drone constructStarterDrone(Drone drone) {
		drone.addModule(new InvModule(9));
		drone.addModule(new IRModule());
		drone.addModule(new ICModule(2));
		drone.addModule(new ELModule(100.0, 0.1, 0.04));
		drone.addModule(new CTBModule());
		drone.addModule(new CTDModule());

		return drone;
	}

	public static Drone constructStarterBuildDrone(Drone drone) {
		drone.addModule(new InvModule(9));
		drone.addModule(new IRModule());
		drone.addModule(new ELModule(100.0, 0.1, 0.03));
		drone.addModule(new CTBModule());

		return drone;
	}

	public static Drone constructStarterDestructionDrone(Drone drone) {
		drone.addModule(new InvModule(9));
		drone.addModule(new ICModule(2));
		drone.addModule(new ELModule(100.0, 0.1, 0.03));
		drone.addModule(new CTDModule());

		return drone;
	}

	public static void constructDrone(Drone drone, int droneType) {
		if(droneType == DefaultDrone_StarterDrone) constructStarterDrone(drone);
		else if(droneType == DefaultDrone_BuildDrone) constructStarterBuildDrone(drone);
		else if(droneType == DefaultDrone_DestructionDrone) constructStarterDestructionDrone(drone);
	}

}
