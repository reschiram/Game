package game.entity.player.playerDrone;

import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.ELModule;
import game.entity.player.playerDrone.module.ICModule;
import game.entity.player.playerDrone.module.IRModule;
import game.entity.player.playerDrone.module.InvModule;
import game.inventory.Inventory;

public class DroneConstructor {

//	public static final int DefaultDrone_StarterDrone = 1;
	public static final int DefaultDrone_BuildDrone = 2;
	public static final int DefaultDrone_DestructionDrone = 3;

//	public static Drone constructStarterDrone(Drone drone, Inventory inv) {
//		drone.addModule(new InvModule(inv));
//		drone.addModule(new IRModule());
//		drone.addModule(new ICModule(2));
//		drone.addModule(new ELModule(100.0, 0.1, 0.04));
//		drone.addModule(new CTBModule());
//		drone.addModule(new CTDModule());
//
//		return drone;
//	}

	public static Drone constructStarterBuildDrone(Drone drone, Inventory inv) {
		drone.addModule(new InvModule(inv));
		drone.addModule(new IRModule());
		drone.addModule(new ELModule(100.0, 0.1, 0.03, drone.getSCUpdateManager().isOwnObject()));
		drone.addModule(new CTBModule());

		return drone;
	}

	public static Drone constructStarterDestructionDrone(Drone drone, Inventory inv) {
		drone.addModule(new InvModule(inv));
		drone.addModule(new ICModule(2));
		drone.addModule(new ELModule(100.0, 0.1, 0.03, drone.getSCUpdateManager().isOwnObject()));
		drone.addModule(new CTDModule());

		return drone;
	}

	public static void constructDrone(Drone drone, int droneType, Inventory inv) {
//		if(droneType == DefaultDrone_StarterDrone) constructStarterDrone(drone, inv);
		if(droneType == DefaultDrone_BuildDrone) constructStarterBuildDrone(drone, inv);
		else if(droneType == DefaultDrone_DestructionDrone) constructStarterDestructionDrone(drone, inv);
	}

}
