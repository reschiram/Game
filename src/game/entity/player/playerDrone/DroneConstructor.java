package game.entity.player.playerDrone;

import game.entity.player.Player;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.DroneModule;
import game.entity.player.playerDrone.module.ELModule;
import game.entity.player.playerDrone.module.ICModule;
import game.entity.player.playerDrone.module.IRModule;
import game.entity.player.playerDrone.module.InvModule;

public class DroneConstructor {
	
	public static Drone constructDrone(Player player, DroneModule... modules){
		Drone drone = new Drone(player);
		for(DroneModule module : modules){
			drone.addModule(module);
		}
		return drone;
	}
	
	public static Drone constructStarterDrone(Player player){
		Drone drone = new Drone(player);
		
		drone.addModule(new InvModule(9));
		drone.addModule(new IRModule());
		drone.addModule(new ICModule(2));
		drone.addModule(new ELModule(100.0, 1.0, 0.1));
		drone.addModule(new CTBModule());
		drone.addModule(new CTDModule());
		
		return drone;
	}
	
	public static Drone constructStarterBuildDrone(Player player){
		Drone drone = new Drone(player);
		
		drone.addModule(new InvModule(9));
		drone.addModule(new IRModule());
		drone.addModule(new ELModule(100.0, 1.0, 0.1));
		drone.addModule(new CTBModule());
		
		return drone;
	}
	
	public static Drone constructStarterDestructionDrone(Player player){
		Drone drone = new Drone(player);
		
		drone.addModule(new InvModule(9));
		drone.addModule(new ICModule(2));
		drone.addModule(new ELModule(100.0, 1.0, 0.1));
		drone.addModule(new CTDModule());
		
		return drone;
	}

}
