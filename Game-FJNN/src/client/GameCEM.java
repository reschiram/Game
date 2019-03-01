package client;

import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import events.entity.EntityPathEvent;
import events.entity.EntityStatusEvent;
import events.entity.PlayerMoveEvent;
import events.entity.drone.CTSelectionEventDU;
import events.entity.drone.CTStatusEventDU;
import events.entity.drone.ELEventDU;
import events.entity.drone.TargetEventDU;
import events.inventory.ItemAddEvent;
import events.inventory.ItemRemoveEvent;
import events.inventory.ItemSetEvent;
import events.map.MapBlockAddEvent;
import events.map.MapBlockDeleteEvent;
import game.entity.player.PlayerContext;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.DroneModule;
import game.entity.player.playerDrone.module.ELModule;
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
			} else if (event.getMessage().getId() == GameCPM.DataPackage_MapBlockDelete) {
				MapBlockDeleteEvent mapBlockDelete = gameCM.getClientPackageManager().readBlockDeleteMessage(event.getMessage());
				event.setActive(false);
				
				if (mapBlockDelete == null) return;

				Map.getMap().deleteBlock(mapBlockDelete.getMapBlock().getLocation(),
						mapBlockDelete.getMapBlock().getResource().getLayerUp(),
						mapBlockDelete.getMapBlock().getResource().isGround(), false);
				
			} else if (event.getMessage().getId() == GameCPM.DataPackage_DroneUpdate_Energy) {
				ELEventDU elEvent = gameCM.getClientPackageManager().readELDUMessage(event.getMessage());
				event.setActive(false);

				if (elEvent == null) return;

				((ELModule) elEvent.getEntity().getModule(ELModule.class)).updateEnergyLoad(elEvent.getEnergyLevel(),
						elEvent.isCharging());
				
			} else if (event.getMessage().getId() == GameCPM.DataPackage_DroneUpdate_Target) {
				TargetEventDU targetEvent = gameCM.getClientPackageManager().readTargetDUMessage(event.getMessage());
				event.setActive(false);

				System.out.println(targetEvent + " -> " + targetEvent.getBlockTarget() + " -> " + targetEvent.getTargetLevel() + " : " + targetEvent.getPublishedTick());				
				if (targetEvent == null) return;
				
				targetEvent.getEntity().getPathController().setBlockTarget(targetEvent.getBlockTarget(), false);				
			} else if (event.getMessage().getId() == GameCPM.DataPackage_DroneUpdate_CMDTarget_Status) {
				CTStatusEventDU targetEvent = gameCM.getClientPackageManager().readCTStatusDUMessage(event.getMessage());

				if (targetEvent == null) return;				
				event.setActive(false);			
				
				System.out.println(targetEvent.getTarget() + "|" + targetEvent.isFailed() + "->" + targetEvent.getStatusUpdateType());	
				if (targetEvent.isFailed()) return;	

				if (targetEvent.getStatusUpdateType() == ComsData.ActionTarget_StatusUpdate_Type_Add) {
					targetEvent.getMaster().addTarget(targetEvent.getTarget());
				} else if (targetEvent.getStatusUpdateType() == ComsData.ActionTarget_StatusUpdate_Type_Remove) {
					targetEvent.getMaster().removeTarget(targetEvent.getTarget().getBlockLocation(), targetEvent.getTarget().getTargetType());
				}
			} else if (event.getMessage().getId() == GameCPM.DataPackage_DroneUpdate_CMDTarget_Selection) {
				CTSelectionEventDU targetEvent = gameCM.getClientPackageManager().readCTSelectionDUMessage(event.getMessage());
				event.setActive(false);
				
				System.out.println("new target selection update: " + targetEvent);
				if (targetEvent == null) return;
				
				if (targetEvent.getTargetType() == ComsData.ActionTarget_Type_Build) {					
					DroneModule module = targetEvent.getEntity().getModule(CTBModule.class);
					if(module != null) {
						if(!((CTBModule)module).hasTarget(targetEvent.getTargetKey())) event.setActive(true);
						else ((CTBModule)module).selectCurrentTarget(targetEvent.getTargetKey());
					}
				} else if (targetEvent.getTargetType() == ComsData.ActionTarget_Type_Destroy) {					
					DroneModule module = targetEvent.getEntity().getModule(CTDModule.class);
					if(module != null) {
						if(!((CTDModule)module).hasTarget(targetEvent.getTargetKey())) {
							System.out.println("No Target at: " + targetEvent.getTargetKey());
							event.setActive(true);
						} else ((CTDModule)module).selectCurrentTarget(targetEvent.getTargetKey());
					}
				}
			}
		}catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
