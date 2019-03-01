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
	
	private static final int Update_Rate_Drone_EnergyLoad = 50;
	private static final int Update_Rate_Entity_Position = 80;
	
	private long startTick = 0l;	
	private int updateRate;
	
	private int updateType;	
	private Object updatableObject;
	
	public SCUpdateManager(int updateType, Object updatableObject) {
		this.updateType = updateType;
		this.updatableObject = updatableObject;
		
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
		if(forceUpdate || GameManager.TickManager.getCurrentTick() - startTick > updateRate) {
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
		if(updatableObject instanceof Entity) {
			Entity entity = (Entity) updatableObject;
			GameEventManager.getEventManager().publishEvent(new EntityStatusEvent(entity, true));
		}
	}

	private void updateDroneEnergyLoad() {
		if(updatableObject instanceof ELModule) {
			GameEventManager.getEventManager().publishEvent(new ELEventDU((ELModule)updatableObject));
		}
	}
	
}
