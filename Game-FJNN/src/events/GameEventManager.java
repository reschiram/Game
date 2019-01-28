package events;

import Data.Queue;
import client.GameCM;
import data.DataPackage;
import data.PackageType;
import events.entity.EntityEvent;
import events.entity.EntityPathEvent;
import events.entity.EntityStatusEvent;
import events.entity.PlayerMoveEvent;
import events.inventory.InventoryEvent;
import events.inventory.ItemAddEvent;
import events.inventory.ItemRemoveEvent;
import events.inventory.ItemSetEvent;
import events.map.MapBlockAddEvent;
import events.map.MapBlockDeleteEvent;
import events.map.MapEvent;

public class GameEventManager {
	
	private static GameEventManager gameEventManager;
	
	public static GameEventManager getEventManager() {
		return gameEventManager;
	}
	
	private GameCM gameCM;
	
	private Queue<EntityEvent> entityEvents = new Queue<>();	
	private Queue<InventoryEvent> inventoryEvents = new Queue<>();	
	private Queue<MapEvent> mapEvents = new Queue<>();
	
	private Queue<GameEvent> events = new Queue<>();
	
	public GameEventManager(GameCM gameCM) {
		gameEventManager = this;
		this.gameCM = gameCM;
	}
	
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
	
	public void tick() {
		handleEntityEvents();
		handleInventoryEvent();
		handleMapEvent();
		handleGameEvent();
	}

	private void handleEntityEvents() {
		while(!entityEvents.isEmpty()) {
			EntityEvent event = entityEvents.get();
			entityEvents.remove();
			try{
				PackageType message = null;
				if (event instanceof PlayerMoveEvent) {
					message = this.gameCM.getClientPackageManager().createPlayerMovedMessage((PlayerMoveEvent) event);
				} else if (event instanceof EntityPathEvent) {
					message = this.gameCM.getClientPackageManager().createEntityPathMessage((EntityPathEvent) event);		
				} else if (event instanceof EntityStatusEvent) {
					message = this.gameCM.getClientPackageManager().createEntityStatusMessage((EntityStatusEvent) event);
				}
				if(message != null)this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInventoryEvent() {
		while(!inventoryEvents.isEmpty()) {
			InventoryEvent event = inventoryEvents.get();
			inventoryEvents.remove();
			try{
				PackageType message = null;
				if (event instanceof ItemAddEvent) {
					message = this.gameCM.getClientPackageManager().createItemAddMessage((ItemAddEvent) event);
				}else if (event instanceof ItemRemoveEvent) {
					message = this.gameCM.getClientPackageManager().createItemRemoveMessage((ItemRemoveEvent) event);
				}else if (event instanceof ItemSetEvent) {
					message = this.gameCM.getClientPackageManager().createItemSetMessage((ItemSetEvent) event);
				}
				if(message != null)this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleMapEvent() {
		while(!mapEvents.isEmpty()) {
			MapEvent event = mapEvents.get();
			mapEvents.remove();
			try{
				PackageType message = null;
				if (event instanceof MapBlockAddEvent) {
					message = this.gameCM.getClientPackageManager().createMapBlockAddMessage((MapBlockAddEvent) event);
				}else if (event instanceof MapBlockDeleteEvent) {
					message = this.gameCM.getClientPackageManager().createMapBlockDeleteMessage((MapBlockDeleteEvent) event);
				}
				if(message != null)this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleGameEvent() {
		while(!events.isEmpty()) {
			GameEvent event = events.get();
			events.remove();
			try {
				System.out.println("Error: unknown event has occured! Created: " + event.getPublishedTick());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
