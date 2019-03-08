package game.entity.requester;

import Data.Location;
import game.entity.player.Player;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneConstructor;
import game.entity.player.playerDrone.DroneHost;
import game.inventory.Inventory;

public class DroneRequest extends EntityRequest{
	
	private DroneHost host;
	private int droneType;

	public DroneRequest(Location blockSpawn, int droneType, DroneHost host) {
		super(blockSpawn);
		this.droneType = droneType;
		this.host = host;
	}

	public int getDroneType() {
		return droneType;
	}

	public DroneHost getPlayer() {
		return host;
	}
	
	@Override
	public void spawnEntity(int entityID, String extraInfos) throws Exception {
		Inventory inv = null;
		inv = EntityRequesterService.getEntityRequesterService().readInventory(extraInfos, 0);
		
		boolean isOwnEntity = host instanceof Player;
		
		Drone drone = new Drone(host, entityID, isOwnEntity);
		DroneConstructor.constructDrone(drone, droneType, inv);
		drone.show();
		host.addDrone(drone);
	}
}
