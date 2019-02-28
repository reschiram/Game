package client;

import events.GameEventManager;
import events.entity.DroneEnergyLoadUpdateEvent;
import game.GameManager;
import game.entity.player.playerDrone.module.ELModule;

public class SCUpdateManager {
	
	public static final int Update_Type_Drone_EnergyLoad = 1;
	
	private static final int Update_Rate_Drone_EnergyLoad = 50;
	
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
				default:
					break;
			}
		}
	}

	private void updateDroneEnergyLoad() {
		if(updatableObject instanceof ELModule) {
			GameEventManager.getEventManager().publishEvent(new DroneEnergyLoadUpdateEvent((ELModule)updatableObject));
		}
	}
	
}
