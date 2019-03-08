package client;

import events.GameEventManager;
import events.entity.EntityStatusEvent;
import events.entity.drone.ELEventDU;
import game.GameManager;
import game.entity.Entity;
import game.entity.player.playerDrone.module.ELModule;

public class SCUpdateManager {

	public static final int Update_Type_Drone_EnergyLoad = 1;
	public static final int Update_Type_Entity_Position = 2;

	private static final int Update_Rate_Drone_EnergyLoad = 80;
	private static final int Update_Rate_Entity_Position = 50;

	private long startTick = 0l;
	private int updateRate;

	private int updateType;
	private Object updatableObject;
	private boolean isOwnObject;

	public SCUpdateManager(boolean isOwnObject, int updateType, Object updatableObject) {
		this.updateType = updateType;
		this.updatableObject = updatableObject;
		this.isOwnObject = isOwnObject;
		
		switch (updateType) {
			case Update_Type_Drone_EnergyLoad:
				updateRate = Update_Rate_Drone_EnergyLoad;
				break;
			case Update_Type_Entity_Position:
				updateRate = Update_Rate_Entity_Position;
				break;
			default:
				updateRate = 100;
				break;
		}
	}

	public void update(boolean forceUpdate) {
		if (!isOwnObject) return;

		if (forceUpdate || GameManager.TickManager.getCurrentTick() - startTick > updateRate) {
			startTick = GameManager.TickManager.getCurrentTick();
			
			switch (updateType) {
				case Update_Type_Drone_EnergyLoad:
					updateDroneEnergyLoad();
					break;
				case Update_Type_Entity_Position:
					updateEntityPosition();
					break;
				default:
					break;
			}
		}
	}

	private void updateEntityPosition() {
		if (updatableObject instanceof Entity) {
			Entity entity = (Entity) updatableObject;
			GameEventManager.getEventManager().publishEvent(new EntityStatusEvent(entity, true));
		}
	}

	private void updateDroneEnergyLoad() {
		if (updatableObject instanceof ELModule) {
			GameEventManager.getEventManager().publishEvent(new ELEventDU((ELModule) updatableObject));
		}
	}

	public boolean isOwnObject() {
		return isOwnObject;
	}
	
}
