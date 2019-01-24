package events;

import Data.Queue;
import events.entity.EntityEvent;
import events.inventory.InventoryEvent;
import events.map.MapEvent;

public class GameEventManager {
	
	private static GameEventManager gameEventManager;
	
	public static GameEventManager getEventManager() {
		if(gameEventManager == null) gameEventManager = new GameEventManager();
		return gameEventManager;
	}
	
	private Queue<EntityEvent> entityEvents = new Queue<>();	
	private Queue<InventoryEvent> inventoryEvents = new Queue<>();	
	private Queue<MapEvent> mapEvents = new Queue<>();
	
	private Queue<GameEvent> events = new Queue<>();
	
	public void publishEvent(GameEvent event) {
		if(event instanceof EntityEvent) {
			this.entityEvents.add((EntityEvent) event);
		}else if(event instanceof InventoryEvent) {
			this.inventoryEvents.add((InventoryEvent) event);
		}else if(event instanceof MapEvent) {
			this.mapEvents.add((MapEvent) event);
		}else {
			this.events.add(event);
		}
	}

}
