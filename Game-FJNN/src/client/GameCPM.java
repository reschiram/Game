package client;

import Data.Location;
import client.commData.DroneTargetData;
import client.commData.DroneTargetInfos;
import data.DataPackage;
import data.MapResource;
import data.PackageType;
import data.readableData.BooleanData;
import data.readableData.CompleteStringData;
import data.readableData.DoubleData;
import data.readableData.IntegerData;
import data.readableData.LongData;
import data.readableData.ReadableData;
import data.readableData.StringData;
import data.user.User;
import events.entity.DroneUpdateEvent;
import events.entity.EntityPathEvent;
import events.entity.EntityStatusEvent;
import events.entity.PlayerMoveEvent;
import events.inventory.ItemAddEvent;
import events.inventory.ItemRemoveEvent;
import events.inventory.ItemSetEvent;
import events.map.MapBlockAddEvent;
import events.map.MapBlockDeleteEvent;
import game.entity.Entity;
import game.entity.manager.EntityManager;
import game.entity.player.PlayerDummie;
import game.entity.player.playerDrone.Drone;
import game.gridData.map.MapBlock;
import game.gridData.map.MapDummieBlock;
import game.gridData.map.Mapdata;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;
import game.map.Map;

public class GameCPM {
	
	public static final int maxMessageLength = DataPackage.MAXPACKAGELENGTH - (User.maxUsernameLength + 1);
	
	public static final int DataPackage_MapDownloadInfos = 14;
	public static final int DataPackage_MapDownloadData = 16;
	
	public static final int DataPackage_PlayerMessage = 18;
	public static final int DataPackage_LobbyPlayerStatus = 20;
	public static final int DataPackage_StartGame = 22;
	
	public static final int DataPackage_EntityCreationRequest_Player = 24;
	public static final int DataPackage_EntityCreationRequest_Drone = 26;
	public static final int DataPackage_EntityCreationRequest_ItemEntity = 28;
	public static final int DataPackage_EntityCreationResponse = 30;
	
	public static final int DataPackage_PlayerMoved = 32;
	public static final int DataPackage_EntityPath = 34;
	public static final int DataPackage_EntityStatus = 36;
	public static final int DataPackage_DroneUpdate_Energy = 38;
	public static final int DataPackage_DroneUpdate_Target = 40;
	public static final int DataPackage_DroneUpdate_ActionTarget = 42;
	
	public static final int DataPackage_ItemAdd = 44;
	public static final int DataPackage_ItemRemove = 46;
	public static final int DataPackage_ItemSet = 48;
	
	public static final int DataPackage_MapBlockAdd = 50;	
	public static final int DataPackage_MapBlockDelete = 52;
	
	public static final int MapDownloadData_DataCount = 63;
	
	public GameCPM() {

	}
	
	public void loadPackages() {
		
		//<=== Add MapDownload Packages ===>
		DataPackage.setType(new PackageType(DataPackage_MapDownloadInfos, "MapDownloadInfos", new IntegerData("width"), new IntegerData("height"), new IntegerData("seed"), new IntegerData("PackageCount")));
		
		ReadableData<?>[] content = new ReadableData<?>[1 + (MapDownloadData_DataCount * 4)];
		content[0] = new IntegerData("id");
		for(int i = 0; i < MapDownloadData_DataCount; i++) {
			int base = (i * 4) + 1;
			content[base] 		= new IntegerData("Ground(0|" 	+ String.format("%02d", i));
			content[base + 1] 	= new IntegerData("Ground(1|" 	+ String.format("%02d", i));
			content[base + 2] 	= new IntegerData("Build(0|" 	+ String.format("%02d", i));
			content[base + 3] 	= new IntegerData("Build(1|" 	+ String.format("%02d", i));
		}
		DataPackage.setType(new PackageType(DataPackage_MapDownloadData, "MapDownloadData", content));

		//<=== Add Lobby Packages ===>
		
		DataPackage.setType(new PackageType(DataPackage_LobbyPlayerStatus, "LobbyPlayerStatus",
				new CompleteStringData("userName", User.maxUsernameLength), new BooleanData("isHost"), new IntegerData("Status")))
		;
		
		DataPackage.setType(new PackageType(DataPackage_PlayerMessage, "PlayerMessage",
				new CompleteStringData("userName", User.maxUsernameLength), new CompleteStringData("message", maxMessageLength)))
		;
		
		DataPackage.setType(new PackageType(DataPackage_StartGame, "StartGame",
				new LongData("Time")));
		;
		
		//<=== Add Entity-Creation Packages ===>

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_Player, "EntityCreationRequest_Player",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new IntegerData("BlockPos_X"), new IntegerData("BlockPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_ItemEntity, "EntityCreationRequest_ItemEntity",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new CompleteStringData("ItemType", ItemType.Max_IDLength),
				new IntegerData("BlockPos_X"), new IntegerData("BlockPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_Drone, "EntityCreationRequest_Drone",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new IntegerData("DroneType"), new IntegerData("EntityHost_ID"),
				new IntegerData("BlockPos_X"), new IntegerData("BlockPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationResponse, "EntityCreationResponse",
				new IntegerData("EntityRequest_ID"), new IntegerData("Entity_ID"),
				new StringData("Extra_Info", DataPackage.MAXPACKAGELENGTH - (1 + 4 + 4 + 1))));
		
		//<=== Add Entity-Event Packages ===>

		DataPackage.setType(new PackageType(DataPackage_PlayerMoved, "PlayerMoved", new IntegerData("Entity_ID"),
				new IntegerData("Velocity_X"), new IntegerData("Velocity_Y"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_EntityPath, "EntityPath", new IntegerData("Entity_ID"),
				new IntegerData("Target_X"), new IntegerData("Target_Y"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_EntityStatus, "EntityStatus", new IntegerData("Entity_ID"),
				new BooleanData("Alive"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_DroneUpdate_Energy, "DroneUpdate_Energy", new IntegerData("Entity_ID"),
				new DoubleData("EnergyCount"), new BooleanData("isCharging")));
		
		DataPackage.setType(new PackageType(DataPackage_DroneUpdate_Target, "DroneUpdate_Target", new IntegerData("Entity_ID"),
				new IntegerData("blockLocation_X"), new IntegerData("blockLocation_Y"), new IntegerData("targetLevel")));
		
		DataPackage.setType(new PackageType(DataPackage_DroneUpdate_ActionTarget, "DroneUpdate_Target", new IntegerData("Entity_ID"),
				new IntegerData("newTargetId"), new IntegerData("ActionType")));
		
		//<=== Add Inventory-Event Packages ===>
		
		DataPackage.setType(new PackageType(DataPackage_ItemAdd, "ItemAdd", new IntegerData("Inventory_ID"),
				new CompleteStringData("ItemType", ItemType.Max_IDLength), new IntegerData("Amount")));
		
		DataPackage.setType(new PackageType(DataPackage_ItemRemove, "ItemRemove", new IntegerData("Inventory_ID"),
				new CompleteStringData("ItemType", ItemType.Max_IDLength), new IntegerData("Amount")));
		
		DataPackage.setType(new PackageType(DataPackage_ItemSet, "ItemSet", new IntegerData("Inventory_ID"),
				new CompleteStringData("ItemType", ItemType.Max_IDLength), new IntegerData("Amount"), new IntegerData("new_Slot")));
		
		//<=== Add Map-Event Packages ===>
		
		DataPackage.setType(new PackageType(DataPackage_MapBlockAdd, "MapBlockAdd", new IntegerData("resourceID"),
				new IntegerData("BlockPos_X"), new IntegerData("BlockPos_Y"), new IntegerData("Layer")));
		
		DataPackage.setType(new PackageType(DataPackage_MapBlockDelete, "MapBlockDelete", new BooleanData("doDrops"),
				new IntegerData("BlockPos_X"), new IntegerData("BlockPos_Y"), new IntegerData("Layer")));
	}
	
	//<=== create: Entity-Events ===>

	public PackageType createPlayerMovedMessage(PlayerMoveEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getEntity().getID(),
				event.getVelocity().getXSpeed(), event.getVelocity().getYSpeed(),
				event.getCurrentPixelLocation().getX(),	event.getCurrentPixelLocation().getY());
	}

	public PackageType createEntityPathMessage(EntityPathEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_EntityPath, event.getEntity().getID(),
				event.getPixelTarget().getX(), event.getPixelTarget().getY(),
				event.getCurrentPixelLocation().getX(),	event.getCurrentPixelLocation().getY());
	}

	public PackageType createEntityStatusMessage(EntityStatusEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_EntityStatus, event.getEntity().getID(),
				event.isAlive(),
				event.getCurrentPixelLocation().getX(),	event.getCurrentPixelLocation().getY());
	}

	public PackageType createDroneUpdateMessage(DroneUpdateEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_DroneUpdate, event.getEntity().getID(),
				event.getDroneEnergy(),	event.isDroneCharging(),
				event.getCurrentBDroneTarget(), event.getCurrentDDroneTarget(), event.getDroneTargetInfosChange(),
				event.getCurrentPixelLocation().getX(),	event.getCurrentPixelLocation().getY());
	}

	//<=== read: Entity-Events ===>
	
	public PlayerMoveEvent readPlayerMoveMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		int velocityX = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int velocityY = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[4]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);
		if(entity == null) {
			System.out.println("Error: player move event occoured for a unknown entity");
			return null;
		}else if (!(entity instanceof PlayerDummie)) {
			System.out.println("Error: player move event occoured for a non-player entity");	
			return new PlayerMoveEvent(null, velocityX, velocityY, new Location(pixelPosX, pixelPosY));			
		}
		
		return new PlayerMoveEvent((PlayerDummie) entity, velocityX, velocityY, new Location(pixelPosX, pixelPosY));
	}
	
	public EntityPathEvent readEntityPathMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		int pixelTargetPos_X = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int pixelTargetPos_Y = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[4]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);		
		if(entity == null) {
			System.out.println("Error: entity path event occourred for a unknown entity");
			return null;
		}
		
		return new EntityPathEvent(entity, new Location(pixelTargetPos_X, pixelTargetPos_Y), new Location(pixelPosX, pixelPosY));
	}
	
	public EntityStatusEvent readEntityStatusMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		boolean isAlive = ((BooleanData) message.getDataStructures()[1]).getData().booleanValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);		
		if(entity == null) {
			System.out.println("Error: entity status update event occourred for a unknown entity: " + entityID);
			return null;
		}
		
		return new EntityStatusEvent(entity, isAlive, new Location(pixelPosX, pixelPosY));
	}
	
	public DroneUpdateEvent readDroneUpdateMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		double energyLevel = ((DoubleData) message.getDataStructures()[1]).getData().doubleValue();
		boolean isCharging = ((BooleanData) message.getDataStructures()[2]).getData().booleanValue();
		DroneTargetInfos currentBDroneTargetInfos = ((DroneTargetData) message.getDataStructures()[3]).getData();
		DroneTargetInfos currentDDroneTargetInfos = ((DroneTargetData) message.getDataStructures()[4]).getData();
		DroneTargetInfos changeTargetInfos = ((DroneTargetData) message.getDataStructures()[5]).getData();
		int pixelPosX = ((IntegerData) message.getDataStructures()[6]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[7]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);		
		if(entity == null) {
			System.out.println("Error: entity status update event occourred for a unknown entity: " + entityID);
			return null;
		}else if (!(entity instanceof Drone)) {
			System.out.println("Error: drone target event occoured for a non-drone entity");
			return new DroneUpdateEvent(null, energyLevel, isCharging, currentDDroneTargetInfos,
					currentBDroneTargetInfos, changeTargetInfos, new Location(pixelPosX, pixelPosY));
		}

		return new DroneUpdateEvent((Drone) entity, energyLevel, isCharging, currentDDroneTargetInfos,
				currentBDroneTargetInfos, changeTargetInfos, new Location(pixelPosX, pixelPosY));
	}
	
	//<=== create: Inventory-Events ===>

	public PackageType createItemAddMessage(ItemAddEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_ItemAdd, event.getInv().getInvID(),
				event.getItem().getItemType().getID(), event.getItem().getAmount());
	}

	public PackageType createItemRemoveMessage(ItemRemoveEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_ItemRemove, event.getInv().getInvID(),
				event.getItem().getItemType().getID(), event.getItem().getAmount());
	}

	public PackageType createItemSetMessage(ItemSetEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_ItemSet, event.getInv().getInvID(),
				event.getItem().getItemType().getID(), event.getItem().getAmount(), event.getSlot());
	}
	
	//<=== read: Inventory-Events ===>
	
	public ItemAddEvent readItemAddMessage(PackageType message) {
		int invID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		String itemType = ((StringData) message.getDataStructures()[1]).getData();
		int amount = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		
		Item item = new Item(ItemType.getItemType(itemType), amount);
		Inventory inv = Inventory.getInventory(invID);
		if(inv == null) {
			System.out.println("Error: item add event occourred for a unknown inventory");
			return null;
		}
		
		return new ItemAddEvent(inv, item);
	}
	
	public ItemRemoveEvent readItemRemoveMessage(PackageType message) {
		int invID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		String itemType = ((StringData) message.getDataStructures()[1]).getData();
		int amount = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		
		Item item = new Item(ItemType.getItemType(itemType), amount);
		Inventory inv = Inventory.getInventory(invID);
		if(inv == null) {
			System.out.println("Error: item remove event occourred for a unknown inventory");
			return null;
		}
		
		return new ItemRemoveEvent(inv, item);
	}
	
	public ItemSetEvent readItemSetMessage(PackageType message) {
		int invID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		String itemType = ((StringData) message.getDataStructures()[1]).getData();
		int amount = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int slot = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Item item = new Item(ItemType.getItemType(itemType), amount);
		Inventory inv = Inventory.getInventory(invID);
		if(inv == null) {
			System.out.println("Error: item set event occourred for a unknown inventory");
			return null;
		}
		
		return new ItemSetEvent(inv, item, slot);
	}
	
	//<=== create: Map-Events ===>

	public PackageType createMapBlockAddMessage(MapBlockAddEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_MapBlockAdd, event.getMapBlock().getResource().getID(),
				event.getMapBlock().getLocation().getX(), event.getMapBlock().getLocation().getY(),
				event.getMapBlock().getResource().getLayerUp() + (event.getMapBlock().getResource().isGround() ? 0 : 2));
	}

	public PackageType createMapBlockDeleteMessage(MapBlockDeleteEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_MapBlockDelete, event.doDrops(),
				event.getMapBlock().getLocation().getX(), event.getMapBlock().getLocation().getY(),
				event.getMapBlock().getResource().getLayerUp() + (event.getMapBlock().getResource().isGround() ? 0 : 2));
	}
	
	//<=== read: Map-Events ===>
	
	public MapBlockAddEvent readBlockAddMessage(PackageType message) {
		int resourceID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		int blockPos_X = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int blockPos_Y = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int layer = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Mapdata block = new MapBlock(MapResource.getMapResource(resourceID), layer, new Location(blockPos_X, blockPos_Y));
		
		return new MapBlockAddEvent((MapBlock) block, resourceID, new Location(blockPos_X, blockPos_Y));
	}
	
	public MapBlockDeleteEvent readBlockDeleteMessage(PackageType message) {
		boolean doDrops = ((BooleanData) message.getDataStructures()[0]).getData().booleanValue();
		int blockPos_X = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int blockPos_Y = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int layer = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Mapdata block = Map.getMap().getMapData(new Location(blockPos_X, blockPos_Y))[layer];
		if(block == null) {
			System.out.println("Error: mapBlock delete event occurred for a non existing block");
			return null;
		}else if(!(block instanceof MapBlock)) {
			System.out.println("Error: BlockPart was given");
			block = ((MapDummieBlock)block).getBlock();
		}
		
		return new MapBlockDeleteEvent((MapBlock) block, doDrops);
	}
}
