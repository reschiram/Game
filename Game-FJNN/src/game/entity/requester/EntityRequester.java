package game.entity.requester;

import java.util.HashMap;

import Data.Location;
import client.GameCM;
import client.GameCPM;
import data.DataPackage;
import data.PackageType;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.readableData.IntegerData;
import data.readableData.StringData;
import game.entity.Entity;
import game.entity.manager.EntityManager;
import game.entity.player.Player;
import game.entity.type.EntityType;
import game.inventory.items.ItemType;

public class EntityRequester implements ToClientMessageEventListener{
	
	private static EntityRequester entityRequester;
	public static EntityRequester getEntityRequester() {
		return entityRequester;
	}
	
	private GameCM gameCM;		
	private HashMap<Integer, EntityRequest> requests = new HashMap<>();
	
	public EntityRequester(GameCM gameCM) {
		entityRequester = this;
		this.gameCM = gameCM;
		
		gameCM.getClientManager().getEventManager().registerClientMessageEventListener(this, 4);
	}
	
	public void requestItemEntity(ItemType type, Location blockSpawn) {
		ItemEntityRequest request = new ItemEntityRequest(type, blockSpawn);
		sendRequest(request, -1);				
		this.requests.put(request.getRequestID(), request);
	}

	public void requestPlayer(Location blockSpawn) {
		PlayerRequest request = new PlayerRequest(blockSpawn);
		sendRequest(request, -1);				
		this.requests.put(request.getRequestID(), request);
	}

	public void requestDrone(Player player, int droneType) {
		DroneRequest request = new DroneRequest(player.getBlockLocation(), droneType, player);		
		sendRequest(request, -1);		
		this.requests.put(request.getRequestID(), request);
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if (event.getMessage().getId() == GameCPM.DataPackage_EntityCreationResponse) {
			int requestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int entityID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			
			String extraInfos = ((StringData) event.getMessage().getDataStructures()[2]).getData();
			
			if(!requests.containsKey(requestID)) {
				System.out.println("Error: unidentified Entity-Creation: "+requestID);
				return;
			}
			
			EntityRequest request = this.requests.get(requestID);
			this.requests.remove(requestID);
			try {
				request.spawnEntity(entityID, extraInfos);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			event.setActive(false);
		}else if (event.getMessage().getId() == GameCPM.DataPackage_EntityCreationRequest_Drone) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int droneType = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();
			int droneHostID = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();
			
			Entity entity = EntityManager.getEntityManager().getEntity(droneHostID);
			if (entity == null || !(entity instanceof Player)) {
				System.out.println("Error: unidentified entity or not player as host for drone");
				return;
			}
			Player p = (Player) entity;
			
			DroneRequest request = new DroneRequest(p.getBlockLocation(), droneType, p);
			this.requests.put(request.getRequestID(), request);
			sendRequest(request, oldRequestID);		

			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_EntityCreationRequest_ItemEntity) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			String itemType = ((StringData) event.getMessage().getDataStructures()[3]).getData();

			int spawnBlockX = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();
			int spawnBlockY = ((IntegerData) event.getMessage().getDataStructures()[5]).getData().intValue();

			ItemType type = ItemType.getItemType(itemType);
			if (type == null) {
				System.out.println("Error: unidentified itemType - Item could not be dropped");
				return;
			}

			ItemEntityRequest request = new ItemEntityRequest(type, new Location(spawnBlockX, spawnBlockY));
			this.requests.put(request.getRequestID(), request);
			sendRequest(request, oldRequestID);		

			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_EntityCreationRequest_Player) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int spawnBlockX = ((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue();
			int spawnBlockY = ((IntegerData) event.getMessage().getDataStructures()[4]).getData().intValue();

			PlayerRequest request = new PlayerRequest(new Location(spawnBlockX, spawnBlockY));
			this.requests.put(request.getRequestID(), request);
			sendRequest(request, oldRequestID);		

			event.setActive(false);
		}
	}
	
	private void sendRequest(EntityRequest request, int oldRequestID) {
		try {
			PackageType message = null;
			if(request instanceof DroneRequest) {
				DroneRequest droneRequest = (DroneRequest) request;
				
				message = PackageType.readPackageData(GameCPM.DataPackage_EntityCreationRequest_Drone,
						oldRequestID, request.getRequestID(), EntityType.Drone.getID(), droneRequest.getDroneType(), droneRequest.getPlayer().getID(), request.getBlockSpawn().getX(), request.getBlockSpawn().getY());
			}else if(request instanceof ItemEntityRequest) {
				ItemEntityRequest itemEntityRequest = (ItemEntityRequest) request;
				
				message = PackageType.readPackageData(GameCPM.DataPackage_EntityCreationRequest_ItemEntity,
						oldRequestID, request.getRequestID(), EntityType.ItemEntity.getID(), itemEntityRequest.getItemType().getID(), request.getBlockSpawn().getX(), request.getBlockSpawn().getY());
			}else if(request instanceof PlayerRequest) {
				
				message = PackageType.readPackageData(GameCPM.DataPackage_EntityCreationRequest_Player,
						oldRequestID, request.getRequestID(), EntityType.Player.getID(), request.getBlockSpawn().getX(), request.getBlockSpawn().getY());
			}

			gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
