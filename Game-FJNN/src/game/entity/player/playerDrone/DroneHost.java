package game.entity.player.playerDrone;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Hitbox;
import Data.Location;
import client.ComsData;
import data.MapResource;
import events.GameEventManager;
import events.entity.drone.CTStatusEventDU;
import game.entity.Entity;
import game.entity.player.Player;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.DroneModule;
import game.gridData.map.Mapdata;
import game.inventory.Inventory;
import game.map.Map;

public interface DroneHost {
	
	public HashMap<Integer, ArrayList<DroneTarget>> getTargets();

	public Location getLocation();

	public Location getBlockLocation();

	public Inventory getInventory();

	public Hitbox getHitbox();

	public ArrayList<Drone> getPlayerDrones();

	public void addDrone(Drone drone);

	public int getID();
	
	public default void createBuildTarget(Location loc, int resID) {
		int key = loc.getX() + (loc.getY() * Map.getMap().getWidth());	
		
		//add|remove BDroneTarget
		if(resID == -1){
			DroneTarget target = getTarget(key, ComsData.ActionTarget_Type_Build);
			if(target == null) return;
			GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this, target, ComsData.ActionTarget_StatusUpdate_Type_Remove));
			return;
		} else if(MapResource.getMapResource(resID) != null){
			if(getTarget(key, ComsData.ActionTarget_Type_Build) != null) return;
			
			BDroneTarget target = new BDroneTarget(loc, MapResource.getMapResource(resID), this);			
			GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this, target, ComsData.ActionTarget_StatusUpdate_Type_Add));
		}
		
		//add DDroneTarget
		Mapdata data = Map.getMap().getMapData(loc)[Map.DEFAULT_BUILDLAYER];
		if(data!=null){
			if(resID != 0){
				if(getTarget(key, ComsData.ActionTarget_Type_Destroy) != null) return;
				
				DDroneTarget target = new DDroneTarget(loc, this);
				GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this, target, ComsData.ActionTarget_StatusUpdate_Type_Add));				
			}
		}
	}

	public default DroneTarget getTarget(int key, int targetType) {
		if(!this.hasTarget(key)) {
			this.getTargets().put(key, new ArrayList<>());		
			return null;
		}		
		for(DroneTarget target : this.getTargets().get(key)) {
			if(target.getTargetType() == targetType) {
				return target;
			}
		}
		
		return null;
	}

	public default boolean hasTarget(int key) {
		System.out.println("has Target: " + key + "->" + this.getTargets().containsKey(key) + "|" + (this.getTargets().containsKey(key) ? this.getTargets().get(key).size() : 0));
		return this.getTargets().containsKey(key) && !this.getTargets().get(key).isEmpty();
	}

	public default ArrayList<DroneTarget> getTargets(int key) {
		if(!this.hasTarget(key)) this.getTargets().put(key, new ArrayList<>());
		return this.getTargets().get(key);
	}

	public default void createDestructionTarget(Location loc, boolean add) {	
		int key = loc.getX() + (loc.getY() * Map.getMap().getWidth());	
		
		// add|remove DDroneTarget
		if(!add){
			DroneTarget target = getTarget(key, ComsData.ActionTarget_Type_Destroy);
			if(target == null) return;
			
			GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this, target, ComsData.ActionTarget_StatusUpdate_Type_Remove));
		}else if(Map.getMap().getChunks()[loc.getX()/Map.DEFAULT_CHUNKSIZE][loc.getY()/Map.DEFAULT_CHUNKSIZE].getMapData(loc.getX(), loc.getY())[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER] != null){
			if(getTarget(key, ComsData.ActionTarget_Type_Destroy) != null) return;
			
			DDroneTarget target = new DDroneTarget(loc, this);
			GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this, target, ComsData.ActionTarget_StatusUpdate_Type_Add));
		}
	}

	public default void addTarget(DroneTarget target) {
		if (this.getTarget(target.getKey(), target.getTargetType()) != null) return;

		if (this instanceof Player) target.createVisuals();
		
		for(Drone drone : getPlayerDrones()) {
			if(target instanceof BDroneTarget) {
				DroneModule module = drone.getModule(CTBModule.class);
				if(module != null) {
					((CTBModule) module).addTarget(target);
				}
			} else if(target instanceof DDroneTarget) {
				DroneModule module = drone.getModule(CTDModule.class);
				if(module != null) {
					((CTDModule) module).addTarget(target);
				}
			}
		}
		
		this.getTargets(target.getKey()).add(target);
	}

	public default void removeTarget(Location blockLocation, int droneTargetType) {
		int key = blockLocation.getX() + (blockLocation.getY() * Map.getMap().getWidth());
		
		if (this.getTarget(key, droneTargetType) == null) return;
		
		for(Drone drone : getPlayerDrones()) {
			if(droneTargetType == ComsData.ActionTarget_Type_Build) {
				DroneModule module = drone.getModule(CTBModule.class);
				if(module != null) {
					((CTBModule) module).removeTarget(blockLocation);
				}
			} else if(droneTargetType == ComsData.ActionTarget_Type_Destroy) {
				DroneModule module = drone.getModule(CTDModule.class);
				if(module != null) {
					((CTDModule) module).removeTarget(blockLocation);
				}
			}
		}
		
		ArrayList<DroneTarget> targets = getTargets(key);
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).getTargetType() == droneTargetType) {
				if (this instanceof Player) targets.get(i).destroyVisulas();
				targets.remove(i);
			}
		}
	}

}
