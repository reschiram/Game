package game.entity.requester;

import Data.Location;
import game.entity.player.Player;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneConstructor;
import game.inventory.Inventory;

public class DroneRequest extends EntityRequest{
	
	private Player player;
	private int droneType;

	public DroneRequest(Location blockSpawn, int droneType, Player player) {
		super(blockSpawn);
		this.droneType = droneType;
		this.player = player;
	}

	public int getDroneType() {
		return droneType;
	}

	public Player getPlayer() {
		return player;
	}
	
	@Override
	public void spawnEntity(int entityID, String extraInfos) throws Exception {
		Inventory inv = null;
		inv = EntityRequesterService.getEntityRequesterService().readInventory(extraInfos, 0);
		
		Drone drone = new Drone(player, entityID);
		DroneConstructor.constructDrone(drone, droneType, inv);
		drone.show();
		player.addDrone(drone);
	}
}
