package data.map;

import java.util.HashMap;

import Data.Location;
import client.commData.DroneTargetData;
import client.commData.DroneTargetInfos;
import data.DataPackage;
import data.MapResource;
import data.PackageType;
import data.entities.ServerDroneEntity;
import data.entities.ServerEntity;
import data.entities.ServerItemEntity;
import data.entities.ServerPlayerEntity;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.server.InvalidServerClientIDException;
import data.readableData.BooleanData;
import data.readableData.IntegerData;
import data.readableData.StringData;
import data.server.Lobby;
import data.server.Player;
import data.server.request.ServerEntityRequest;
import data.server.request.ServerRequest;
import game.entity.type.EntityType;
import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;
import game.map.Map;
import server.GameSM;
import server.GameSPM;

public class MapSEM implements ToServerMessageEventListener{
	private HashMap<Integer, ServerRequest> requests = new HashMap<>();

	private Lobby lobby;
	private GameSM gameSM;

	public MapSEM(Lobby lobby, GameSM gameSM) {
		this.lobby = lobby;
		this.gameSM = gameSM;
		
		gameSM.getServerManager().getEventManager().registerServerMessageEventListener(this, 4);
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		handleEntityRequestMessages(event);
		handleEvents(event);
		
	}

	private void handleEvents(ToServerMessageEvent event) {
		if(event.getMessage().getId() != GameSPM.DataPackage_PlayerMoved) System.out.println("New Message Event: "+event.getMessage().getId());
		
		boolean send = false;
		if (event.getMessage().getId() == GameSPM.DataPackage_EntityStatus) {
			int entityID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			boolean isAlive = ((BooleanData) event.getMessage().getDataStructures()[1]).getData().booleanValue();
			int pixelPosX = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();
			int pixelPosY = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();

			ServerEntity entity = lobby.getCurrentMap().getEntityManager().getEntity(entityID);
			if (!isAlive) entity.destroy();
			entity.setPixelLocation(new Location(pixelPosX, pixelPosY));
			
			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_PlayerMoved) {
			int entityID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int velocityX = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			int velocityY = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();
			int pixelPosX = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();
			int pixelPosY = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();

			ServerPlayerEntity entity = (ServerPlayerEntity) lobby.getCurrentMap().getEntityManager().getEntity(entityID);
			entity.setVelocity(velocityX, velocityY);
			entity.setPixelLocation(new Location(pixelPosX, pixelPosY));

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_ItemAdd) {
			int invID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			String itemType = ((StringData) event.getMessage().getDataStructures()[1]).getData();
			int amount = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();

			Inventory inv = lobby.getCurrentMap().getInventoryManager().getInventory(invID);
			inv.addItemFunktion(new Item(ItemType.getItemType(itemType), amount));

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_ItemRemove) {
			int invID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			String itemType = ((StringData) event.getMessage().getDataStructures()[1]).getData();
			int amount = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();

			Inventory inv = lobby.getCurrentMap().getInventoryManager().getInventory(invID);
			inv.removeItemFunktion(new Item(ItemType.getItemType(itemType), amount));

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_ItemSet) {
			int invID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			String itemType = ((StringData) event.getMessage().getDataStructures()[1]).getData();
			int amount = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();
			int newSlot = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();

			Inventory inv = lobby.getCurrentMap().getInventoryManager().getInventory(invID);
			inv.setItemFunktion(newSlot, new Item(ItemType.getItemType(itemType), amount));

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_MapBlockAdd) {
			int resID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int blockPos_X = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();		
			int blockPos_Y = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();				
			
			lobby.getCurrentMap().addMapBlock(MapResource.getMapResource(resID), new Location(blockPos_X, blockPos_Y));			

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_MapBlockDelete) {
			int blockPos_X = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();		
			int blockPos_Y = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();	
			int layer = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();				
			
			lobby.getCurrentMap().removeMapBlock(new Location(blockPos_X, blockPos_Y), layer);			

			event.setActive(false);
			send = true;
		} else if (event.getMessage().getId() == GameSPM.DataPackage_DroneUpdate) {
			DroneTargetInfos currentBDroneTargetInfos = ((DroneTargetData) event.getMessage().getDataStructures()[3]).getData();
			DroneTargetInfos currentDDroneTargetInfos = ((DroneTargetData) event.getMessage().getDataStructures()[4]).getData();
			DroneTargetInfos nextDroneTargetInfos = ((DroneTargetData) event.getMessage().getDataStructures()[5]).getData();
			
			System.out.print  ("BDroneTarget: " + ((DroneTargetData) event.getMessage().getDataStructures()[3]).toString() + " -> " + currentBDroneTargetInfos.getBlockLocation() + " => ");
			System.out.print  ("DDroneTarget: " + ((DroneTargetData) event.getMessage().getDataStructures()[4]).toString() + " -> " + currentDDroneTargetInfos.getBlockLocation() + " => ");
			System.out.println("CDroneTarget: " + ((DroneTargetData) event.getMessage().getDataStructures()[5]).toString() + " -> " + nextDroneTargetInfos.getBlockLocation());
			
			event.setActive(false);
			send = true;
		}
		
		if(send) {
			for (Player player : lobby.getConnectedPlayers()) {
				if (player.getServerClientID() != event.getClientID()) {
					try {
						if(event.getMessage().getId() != GameSPM.DataPackage_PlayerMoved) System.out.println("SendPackage: "+event.getMessage().getId());
						gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(event.getMessage()));
					} catch (Exception | InvalidServerClientIDException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void handleEntityRequestMessages(ToServerMessageEvent event) {
		if (event.getMessage().getId() == GameSPM.DataPackage_EntityCreationRequest_Drone) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int currentRequestID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			int droneType = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();
			int droneHostID = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();
			int blockPos_X = ((IntegerData) event.getMessage().getDataStructures()[5]).getData().intValue();
			int blockPos_Y = ((IntegerData) event.getMessage().getDataStructures()[6]).getData().intValue();
				
			ServerEntity droneHost = lobby.getCurrentMap().getEntityManager().getEntity(droneHostID);
			if (!(droneHost instanceof ServerPlayerEntity)) {
				System.out.println("Error: droneHost must be a Player!");
				return;
			}

			System.out.println(event.getClientID() + ": recieveDroneRequest-OldRequestID: " + oldRequestID);
			ServerDroneEntity drone = null;
			if (oldRequestID < 0) {
				Inventory inv = getInventory(9, 0, oldRequestID);

				drone = new ServerDroneEntity(-1, new Location(blockPos_X * Map.DEFAULT_SQUARESIZE, blockPos_Y * Map.DEFAULT_SQUARESIZE), EntityType.Drone, droneType,
						inv, (ServerPlayerEntity) droneHost);
				lobby.getCurrentMap().getEntityManager().addEntity(drone);
			} else {
				drone = (ServerDroneEntity) getKnownEntity(oldRequestID);
			}
			
			ServerEntityRequest request = new ServerEntityRequest(currentRequestID, drone);
			publishRequest(event.getClientID(), request, oldRequestID, currentRequestID);
			event.setActive(false);
		} else if (event.getMessage().getId() == GameSPM.DataPackage_EntityCreationRequest_ItemEntity) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int currentRequestID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			String itemType = ((StringData) event.getMessage().getDataStructures()[3]).getData();
			int blockPos_X = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();
			int blockPos_Y = ((IntegerData) event.getMessage().getDataStructures()[5]).getData().intValue();

			ServerItemEntity itemEntity = null;
//			System.out.println("recieveItemRequest-OldRequestID: " + oldRequestID);
			if (oldRequestID < 0) {
				itemEntity = new ServerItemEntity(-1, new Location(blockPos_X * Map.DEFAULT_SQUARESIZE, blockPos_Y * Map.DEFAULT_SQUARESIZE), EntityType.ItemEntity,
						ItemType.getItemType(itemType));
				lobby.getCurrentMap().getEntityManager().addEntity(itemEntity);
			} else {
				itemEntity = (ServerItemEntity) getKnownEntity(oldRequestID);
			}
			
			ServerEntityRequest request = new ServerEntityRequest(currentRequestID, itemEntity);
			publishRequest(event.getClientID(), request, oldRequestID, currentRequestID);
			event.setActive(false);			
		} else if (event.getMessage().getId() == GameSPM.DataPackage_EntityCreationRequest_Player) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int currentRequestID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			int blockPos_X = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();
			int blockPos_Y = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();

			ServerPlayerEntity playerEntity = null;
			System.out.println(event.getClientID() + ": recievePlayerRequest-OldRequestID: " + oldRequestID);
			if (oldRequestID < 0) {
				EquipmentInventory inv = (EquipmentInventory) getInventory(40, 9, oldRequestID);

				playerEntity = new ServerPlayerEntity(-1, new Location(blockPos_X * Map.DEFAULT_SQUARESIZE, blockPos_Y * Map.DEFAULT_SQUARESIZE), EntityType.Player,
						gameSM.getServerUserManager().getValidatedUser(event.getClientID()), inv);
				lobby.getCurrentMap().getEntityManager().addEntity(playerEntity);
			} else {
				playerEntity = (ServerPlayerEntity) getKnownEntity(oldRequestID);
			}

			ServerEntityRequest request = new ServerEntityRequest(currentRequestID, playerEntity);
			publishRequest(event.getClientID(), request, oldRequestID, currentRequestID);
			event.setActive(false);
		}
	}

	private ServerEntity getKnownEntity(int oldRequestID) {
		ServerRequest request = this.requests.get(oldRequestID);
		if (request != null && request instanceof ServerEntityRequest) {
			return ((ServerEntityRequest) request).getServerEntity();
		} else new Exception("oldRequestId points to wrong request").printStackTrace();
		return null;
	}

	private Inventory getInventory(int itemSize, int equipSize, int oldRequestID) {
		Inventory inv = null;
		if(oldRequestID < 0) {
			if(equipSize <= 0) inv = new Inventory(itemSize, -1);
			else inv = new EquipmentInventory(itemSize, equipSize, -1);
			lobby.getCurrentMap().getInventoryManager().addInventory(inv);
		} else {
			ServerRequest request = this.requests.get(oldRequestID);
			if (request != null && request instanceof ServerEntityRequest
					&& ((ServerEntityRequest) request).getServerEntity() instanceof ServerDroneEntity) {
				ServerDroneEntity entity = (ServerDroneEntity) ((ServerEntityRequest) request).getServerEntity();
				inv = entity.getInv();
			} else new Exception("oldRequestId points to wrong request").printStackTrace();
		}
		return inv;
	}

	public void publishRequest(long clientID, ServerEntityRequest request, int oldRequestID, int currentClientRequestID) {
		System.out.println("publishRequest: (clientID: " + clientID + "|" + oldRequestID + "|"+ currentClientRequestID + "|" + request + ")");		
		if(oldRequestID < 0) this.requests.put(request.getRequestID(), request);
		
		for (Player player : lobby.getConnectedPlayers()) {
			if (player.getServerClientID() == clientID) {
				try {
					PackageType message = gameSM.getGameSPM().answerRequest(clientID, request, currentClientRequestID);
					gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(message));
				} catch (Exception | InvalidServerClientIDException e) {
					e.printStackTrace();
				}
			} else if (oldRequestID < 0) {
				try {
					PackageType message = gameSM.getGameSPM().passRequestMessage(request);
					gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(message));
				} catch (Exception | InvalidServerClientIDException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
