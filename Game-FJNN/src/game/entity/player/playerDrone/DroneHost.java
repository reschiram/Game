package game.entity.player.playerDrone;

import java.util.ArrayList;

import Data.Hitbox;
import Data.Location;
import data.MapResource;
import game.entity.Entity;
import game.entity.player.PlayerDummie;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.gridData.map.Mapdata;
import game.inventory.Inventory;
import game.map.Map;

public interface DroneHost {

	public Location getLocation();

	public Location getBlockLocation();

	public Inventory getInventory();

	public Hitbox getHitbox();

	public ArrayList<Drone> getPlayerDrones();

	public void addDrone(Drone drone);

	public int getID();
	
	public default void addBuildTarget(Location loc, int resID) {		
		//find BDrones|DDrones
		ArrayList<Drone> BDrones = new ArrayList<>();
		ArrayList<Drone> DDrones = new ArrayList<>();
		for(Drone drone: getPlayerDrones()){
			CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
			if(module!=null){
				BDrones.add(drone);
			}
			CTDModule dmodule = (CTDModule) drone.getModule(CTDModule.class);
			if(dmodule!=null){
				DDrones.add(drone);
			}
		}
		
		//add|remove BDroneTarget
		if(resID == -1){
			for(Drone drone: BDrones){
				CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
				module.removeTarget(loc);
			}
			return;
		} else if(MapResource.getMapResource(resID) != null){
			BDroneTarget target = new BDroneTarget(loc, MapResource.getMapResource(resID));
			boolean exists = false;
			for(Drone drone: BDrones){
				CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
				target.addDrone(drone);
				exists = module.addTarget(target);
			}
			if(exists)target.createVisuals();
		}
		
		//add DDroneTarget
		Mapdata data = Map.getMap().getMapData(loc)[Map.DEFAULT_BUILDLAYER];
		if(data!=null){
			if(resID != 0){
				DDroneTarget target = new DDroneTarget(loc);
				target.createVisuals();
				boolean exists = false;
				for(Drone drone: DDrones){
					CTDModule module = (CTDModule) drone.getModule(CTDModule.class);
					target.addDrone(drone);
					exists = module.addTarget(target);
				}
				if(exists)target.createVisuals();
			}
		}
	}

	public default void addDestructionTarget(Location loc, boolean add) {
		if(this instanceof PlayerDummie) {
			System.out.println((add ? "add" : "remove") + " destruction target for player dummie : " + loc);
		}		
		//find DDrones
		ArrayList<Drone> DDrones = new ArrayList<>();
		for(Drone drone: getPlayerDrones()){
			CTDModule module = (CTDModule) drone.getModule(CTDModule.class);
			if(module!=null){
				DDrones.add(drone);
			}
		}
		
		// add|remove DDroneTarget
		if(!add){
			for(Drone drone: DDrones){
				CTDModule module = (CTDModule) drone.getModule(CTDModule.class);
				module.removeTarget(loc);
			}
			return;
		}else if(Map.getMap().getChunks()[loc.getX()/Map.DEFAULT_CHUNKSIZE][loc.getY()/Map.DEFAULT_CHUNKSIZE].getMapData(loc.getX(), loc.getY())[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER] != null){
			DDroneTarget target = new DDroneTarget(loc);
			boolean exists = false;
			for(Drone drone: DDrones){
				CTDModule module = (CTDModule) drone.getModule(CTDModule.class);
				target.addDrone(drone);
				exists = module.addTarget(target);
			}
			if(exists) target.createVisuals();
		}
	}

}
