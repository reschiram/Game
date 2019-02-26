package client;

import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import events.entity.DroneUpdateEvent;
import events.entity.EntityPathEvent;
import events.entity.EntityStatusEvent;
import events.entity.PlayerMoveEvent;
import events.inventory.ItemAddEvent;
import events.inventory.ItemRemoveEvent;
import events.inventory.ItemSetEvent;
import events.map.MapBlockAddEvent;
import events.map.MapBlockDeleteEvent;
import game.entity.player.PlayerContext;
import game.entity.player.playerDrone.Drone;
import game.entity.type.interfaces.PathUser;
import game.map.Map;

public class GameCEM implements ToClientMessageEventListener{

	private GameCM gameCM;
	
	public GameCEM(GameCM gameCM) {
		this.gameCM = gameCM;
		
		gameCM.getClientManager().getEventManager().registerClientMessageEventListener(this, 1);
	}
	
	@Override
	public void messageFromServer(ToClientMessageEvent event) {		
		try{
			if(event.getMessage().getId() == GameCPM.DataPackage_PlayerMoved) {
				PlayerMoveEvent playerMove = gameCM.getClientPackageManager().readPlayerMoveMessage(event.getMessage());
				if(playerMove == null) {
					event.setActive(false);
					return;
				}				
				PlayerContext p = (PlayerContext)playerMove.getEntity();
				p.setLocation(playerMove.getCurrentPixelLocation().getX(), playerMove.getCurrentPixelLocation().getY());
				p.setVelocity(playerMove.getVelocity());
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_EntityPath) {
				EntityPathEvent entityPath = gameCM.getClientPackageManager().readEntityPathMessage(event.getMessage());
				if(entityPath == null) {
					return;
				}
				
				entityPath.getEntity().setLocation(entityPath.getCurrentPixelLocation().getX(), entityPath.getCurrentPixelLocation().getY());
				PathUser pathUser = (PathUser)entityPath.getEntity();
				boolean blocked = pathUser.getPathController().isBlocked();
				
				pathUser.getPathController().setBlocked(false);
				pathUser.getPathController().setTarget(entityPath.getPixelTarget(), false);
				pathUser.getPathController().setBlocked(blocked);
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_EntityStatus) {
				EntityStatusEvent entityStatus = gameCM.getClientPackageManager().readEntityStatusMessage(event.getMessage());
				if(entityStatus == null) {
					event.setActive(false);
					return;
				}
				
				entityStatus.getEntity().setLocation(entityStatus.getCurrentPixelLocation().getX(), entityStatus.getCurrentPixelLocation().getY());
				if(!entityStatus.isAlive())	entityStatus.getEntity().destroy(false);	
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_ItemAdd) {
				ItemAddEvent itemAdd = gameCM.getClientPackageManager().readItemAddMessage(event.getMessage());
				if(itemAdd == null) {
					return;
				}
				
				itemAdd.getInv().addItemFunktion(itemAdd.getItem());
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_ItemRemove) {
				ItemRemoveEvent itemRemove = gameCM.getClientPackageManager().readItemRemoveMessage(event.getMessage());
				if(itemRemove == null) {
					return;
				}
				
				itemRemove.getInv().removeItemFunktion(itemRemove.getItem());
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_ItemSet) {
				ItemSetEvent itemSet = gameCM.getClientPackageManager().readItemSetMessage(event.getMessage());
				if(itemSet == null) {
					return;
				}
				
				itemSet.getInv().setItemFunktion(itemSet.getSlot(), itemSet.getItem());
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_MapBlockAdd) {
				MapBlockAddEvent mapBlockAdd = gameCM.getClientPackageManager().readBlockAddMessage(event.getMessage());
				if(mapBlockAdd == null) {
					return;
				}
				
				Map.getMap().add(mapBlockAdd.getResource().getID(), mapBlockAdd.getBlockLocation(), mapBlockAdd.getResource().isGround(), false);
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_MapBlockDelete) {
				MapBlockDeleteEvent mapBlockDelete = gameCM.getClientPackageManager().readBlockDeleteMessage(event.getMessage());
				if(mapBlockDelete == null) {
					event.setActive(false);
					return;
				}
				
				Map.getMap().deleteBlock(mapBlockDelete.getMapBlock().getLocation(), mapBlockDelete.getMapBlock().getResource().getLayerUp(), mapBlockDelete.getMapBlock().getResource().isGround(), false);
				event.setActive(false);
			}else if(event.getMessage().getId() == GameCPM.DataPackage_DroneUpdate) {
				DroneUpdateEvent droneTarget = gameCM.getClientPackageManager().readDroneUpdateMessage(event.getMessage());
				System.out.println("Drone Update: " + droneTarget + " | " + ((droneTarget == null) ? "null" : (droneTarget.getEntity() + " | " + droneTarget.getDroneTargetInfosChange())));
				if(droneTarget == null) {
					return;
				}				
				
				Drone drone = droneTarget.getEntity();
				try {
					drone.update(droneTarget);
				}catch (Exception e) {
					e.printStackTrace();
				}
				event.setActive(false);
			}
		}catch (Exception e) {
			System.out.println("Error");
		}
	}

}
