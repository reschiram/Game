package events.entity.drone;

import Data.Location;
import client.ComsData;
import data.MapResource;
import events.GameEvent;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;
import game.entity.player.playerDrone.DroneHost;
import game.entity.player.playerDrone.DroneTarget;
import game.map.Map;

public class CTStatusEventDU extends GameEvent {

	private DroneTarget target;
	private int statusUpdateType;
	private DroneHost master;
	
	private boolean failed = false;

	public CTStatusEventDU(DroneHost master, DroneTarget target, int statusUpdateType) {
		super();
		
		this.master = master;		
		this.target = target;
		this.statusUpdateType = statusUpdateType;
	}

	public CTStatusEventDU(DroneHost master, Location blockLocation, int actionType, int statusUpdateType, String additionalInformation) {
		super();
		
		this.master = master;
		this.statusUpdateType = statusUpdateType;
		
		int key = blockLocation.getX() + (blockLocation.getY() * Map.getMap().getWidth());
		
		System.out.println("new Target status event: " + blockLocation + " -> " + key);
		
		if (master.hasTarget(key)) target = master.getTarget(key, actionType);
		
		if (statusUpdateType == ComsData.ActionTarget_StatusUpdate_Type_Add) {
			if (target != null) {
				failed = true;
			} else {
				if(actionType == ComsData.ActionTarget_Type_Build) {
					try {
						int resId = Integer.parseInt(additionalInformation);
						target = new BDroneTarget(blockLocation, MapResource.getMapResource(resId), master);
					} catch (Exception e) {
						e.printStackTrace();
						failed = true;
					}
				}else {
					target = new DDroneTarget(blockLocation, master);
				}
			}
		} else if(statusUpdateType == ComsData.ActionTarget_StatusUpdate_Type_Remove) {
			if(target == null) {
				failed = true;
			}
		}
	}

	public DroneTarget getTarget() {
		return target;
	}

	public int getStatusUpdateType() {
		return statusUpdateType;
	}

	public DroneHost getMaster() {
		return master;
	}

	public boolean isFailed() {
		return failed;
	}

	public String getAdditionalInformation() {
		return target == null ? ""
				: ((target instanceof BDroneTarget) ? (((BDroneTarget) target).getResource().getID() + "") : "");
	}

}
