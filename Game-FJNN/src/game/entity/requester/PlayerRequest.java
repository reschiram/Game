package game.entity.requester;

import Data.Location;
import game.entity.player.Player;
import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;
import game.inventory.equipment.tools.BuildTool;
import game.inventory.equipment.tools.DigTool;

public class PlayerRequest extends EntityRequest {

	public PlayerRequest(Location blockSpawn) {
		super(blockSpawn);
	}
	
	@Override
	public void spawnEntity(int entityID, String extraInfos) throws Exception {	
		boolean isOwnPlayer = EntityRequesterService.getEntityRequesterService().readIsOwnPlayer(extraInfos, 0);
		
		Inventory inv = null;
		inv = EntityRequesterService.getEntityRequesterService().readInventory(extraInfos, 1);
		
		inv.addItemFunktion(new DigTool());
		inv.addItemFunktion(new BuildTool());
		
		System.out.println("PlayerID: " + entityID);
		
		new Player(new Location(getPixelSpawn_X(), getPixelSpawn_Y()), entityID, (EquipmentInventory) inv, isOwnPlayer).show();
	}

}
