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
	}

	private void handleEntityEvents() {
		while(!entityEvents.isEmpty()) {
			EntityEvent event = entityEvents.get();
			entityEvents.remove();
			try{
				if (event instanceof PlayerMoveEvent) {
					PackageType message = this.gameCM.getClientPackageManager().createPlayerMovedMessage((PlayerMoveEvent) event);
					this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
				} else if (event instanceof EntityPathEvent) {
					PackageType message = this.gameCM.getClientPackageManager().createEntityPathMessage((EntityPathEvent) event);
					this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));					
				} else if (event instanceof EntityStatusEvent) {
					PackageType message = this.gameCM.getClientPackageManager().createEntityStatusMessage((EntityStatusEvent) event);
					this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));			
				}
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
				if (event instanceof ItemAddEvent) {
//					PackageType message = this.gameCM.getClientPackageManager().createItemAddMessage((ItemAddEvent) event);
//					this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
