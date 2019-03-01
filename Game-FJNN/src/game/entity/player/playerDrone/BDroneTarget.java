package game.entity.player.playerDrone;

import Data.Location;
import client.ComsData;
import data.MapResource;
import game.entity.Entity;
import game.entity.player.playerDrone.module.InvModule;
import game.gridData.map.Mapdata;
import game.inventory.crafting.Recipe;
import game.map.Map;

public class BDroneTarget extends DroneTarget{

	private MapResource res;
	
	public BDroneTarget(Location location, MapResource res, DroneHost master) {
		super(location, master, ComsData.ActionTarget_Type_Build);
		this.res = res;
	}
	
	@Override
	public boolean interact() {
		Mapdata data = Map.getMap().getChunks()[blockLocation.x/Map.DEFAULT_CHUNKSIZE][blockLocation.y/Map.DEFAULT_CHUNKSIZE].getMapData(blockLocation, false)[Entity.DEFAULT_ENTITY_UP];
		if (data == null) {
			Recipe recipe = Recipe.getRecipe(res.getID());
			for (Drone drone : this.master.getPlayerDrones()) {
				InvModule invModule = (InvModule) drone.getModule(InvModule.class);
				if (invModule != null && recipe != null && recipe.craft(invModule.getInventory())) {
					Map.getMap().add(res.getID(), blockLocation, res.isGround(), true);
					this.done = true;
					break;
				}
			}
		}
		return super.interact();
	}

	public int getID() {
		return res.getID();
	}

	public MapResource getResource() {
		return res;
	}

}
