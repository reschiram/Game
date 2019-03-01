package data.droneData.actionTarget;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Location;
import client.ComsData;
import data.droneData.droneModule.SCTModule;
import data.entities.ServerDroneEntity;
import data.server.Player;

public class ActionTargetManager {
	
	private Player player;
	private ArrayList<ServerDroneEntity> drones = new ArrayList<>();
	private HashMap<Integer, ArrayList<ActionTarget>> actionTargets = new HashMap<>();

	public ActionTargetManager(Player player) {
		this.player = player;
	}
	
	public void addDrone(ServerDroneEntity drone) {
		if(drone.getDroneHost().getPlayer().equals(player) && drone.hasModule(SCTModule.class)) {
			this.drones.add(drone);
		}
	}

	public boolean updateActionTarget(Location blockLocation, int actionType, int CTargetStatusUpdateType,
			String additionalInformation) {
		
		int key = blockLocation.getX() + (blockLocation.getY() * player.getCurrentSMap().getWidth());
		
		if(CTargetStatusUpdateType == ComsData.ActionTarget_StatusUpdate_Type_Add) {			
			ArrayList<ActionTarget> targets = getActionTargets(key);
			
			for(int i = 0; i < targets.size(); i++) {
				ActionTarget target = targets.get(i);
				if(target.getType().getTypeId() == actionType) return false;
			}
			
			ActionTarget newTarget = createTarget(blockLocation, actionType, additionalInformation);
			if(newTarget == null) return false;
			
			targets.add(newTarget);
			for(ServerDroneEntity playerDrone : this.drones) {
				if(playerDrone.hasModule(SCTModule.class)) {
					SCTModule module = (SCTModule) playerDrone.getModule(SCTModule.class);
					module.addTarget(newTarget);					
				}
			}
			
			return true;
			
		} else if(CTargetStatusUpdateType == ComsData.ActionTarget_StatusUpdate_Type_Remove) {	
			ArrayList<ActionTarget> targets = getActionTargets(key);
			
			for(int i = 0; i < targets.size(); i++) {
				ActionTarget target = targets.get(i);
				if(target.getType().getTypeId() == actionType) {
					
					for(ServerDroneEntity playerDrone : this.drones) {
						if(playerDrone.hasModule(SCTModule.class)) {
							SCTModule module = (SCTModule) playerDrone.getModule(SCTModule.class);
							module.removeTarget(target);					
						}
					}
					
					targets.remove(i);
					return true;
				}
			}
		}
		
		return false;
	}

	private ActionTarget createTarget(Location blockLocation, int actionType, String additionalInformation) {
		ActionTargetType type = null;
		if (actionType == ComsData.ActionTarget_Type_Build) {
			try {
				Integer resID = Integer.parseInt(additionalInformation);
				type = ActionTargetType.getBuildType(resID);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else if (actionType == ComsData.ActionTarget_Type_Destroy) {
			type = ActionTargetType.getDestroyType();
		}

		if (type == null) return null;
		return new ActionTarget(blockLocation, player, type);	
	}

	private ArrayList<ActionTarget> getActionTargets(int key) {
		if(!actionTargets.containsKey(key))actionTargets.put(key, new ArrayList<>());
		return this.actionTargets.get(key);
	}	

}
