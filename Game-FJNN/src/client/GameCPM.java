package client;

import Data.Location;
import data.DataPackage;
import data.PackageType;
import data.readableData.BooleanData;
import data.readableData.CompleteStringData;
import data.readableData.IntegerData;
import data.readableData.ReadableData;
import data.readableData.StringData;
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
import game.entity.player.Player;
import game.gridData.map.MapBlock;
import game.gridData.map.MapDummieBlock;
import game.gridData.map.Mapdata;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;
import game.map.Map;

public class GameCPM {
	
	public static final int DataPackage_MapDownloadInfos = 14;
	public static final int DataPackage_MapDownloadData = 16;
	public static final int DataPackage_MapDownloadFinished = 18;	
	
	public static final int DataPackage_EntityCreationRequest_Player = 20;
	public static final int DataPackage_EntityCreationRequest_Drone = 22;
	public static final int DataPackage_EntityCreationRequest_ItemEntity = 24;
	public static final int DataPackage_EntityCreationResponse = 26;
	
	public static final int DataPackage_PlayerMoved = 28;
	public static final int DataPackage_EntityPath = 30;
	public static final int DataPackage_EntityStatus = 32;
	
	public static final int DataPackage_ItemAdd = 34;
	public static final int DataPackage_ItemRemove = 36;
	public static final int DataPackage_ItemSet = 38;
	
	public static final int DataPackage_MapBlockAdd = 40;	
	public static final int DataPackage_MapBlockDelete = 42;
	
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
		
		DataPackage.setType(new PackageType(DataPackage_MapDownloadFinished, "MapDownloadFinished", new BooleanData("succesfull")));
		
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

	//<=== read: Entity-Events ===>
	
	public PlayerMoveEvent readPlayerMoveMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		int velocityX = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int velocityY = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[4]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);
		if(!(entity instanceof Player)) {
			System.out.println("Error: player move event occoured for a non-player entity");
			return null;
		}
		
		return new PlayerMoveEvent((Player) entity, velocityX, velocityY, new Location(pixelPosX, pixelPosY));
	}
	
	public EntityPathEvent readEntityPathMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		int pixelTargetPos_X = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int pixelTargetPos_Y = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[4]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);
		
		return new EntityPathEvent(entity, new Location(pixelTargetPos_X, pixelTargetPos_Y), new Location(pixelPosX, pixelPosY));
	}
	
	public EntityStatusEvent readEntityStatusMessage(PackageType message) {
		int entityID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		boolean isAlive = ((BooleanData) message.getDataStructures()[1]).getData().booleanValue();
		int pixelPosX = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int pixelPosY = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Entity entity = EntityManager.getEntityManager().getEntity(entityID);
		
		return new EntityStatusEvent(entity, isAlive, new Location(pixelPosX, pixelPosY));
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
		
		return new ItemAddEvent(inv, item);
	}
	
	public ItemRemoveEvent readItemRemoveMessage(PackageType message) {
		int invID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		String itemType = ((StringData) message.getDataStructures()[1]).getData();
		int amount = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		
		Item item = new Item(ItemType.getItemType(itemType), amount);
		Inventory inv = Inventory.getInventory(invID);
		
		return new ItemRemoveEvent(inv, item);
	}
	
	public ItemSetEvent readItemSetMessage(PackageType message) {
		int invID = ((IntegerData) message.getDataStructures()[0]).getData().intValue();
		String itemType = ((StringData) message.getDataStructures()[1]).getData();
		int amount = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int slot = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Item item = new Item(ItemType.getItemType(itemType), amount);
		Inventory inv = Inventory.getInventory(invID);
		
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
		
		Mapdata block = Map.getMap().getMapData(new Location(blockPos_X, blockPos_Y))[layer];
		if(block != null && !(block instanceof MapBlock)) {
			System.out.println("Error: BlockPart was given");
			block = ((MapDummieBlock)block).getBlock();
		}
		
		return new MapBlockAddEvent((MapBlock) block, resourceID, new Location(blockPos_X, blockPos_Y));
	}
	
	public MapBlockDeleteEvent readBlockDeleteMessage(PackageType message) {
		boolean doDrops = ((BooleanData) message.getDataStructures()[0]).getData().booleanValue();
		int blockPos_X = ((IntegerData) message.getDataStructures()[1]).getData().intValue();
		int blockPos_Y = ((IntegerData) message.getDataStructures()[2]).getData().intValue();
		int layer = ((IntegerData) message.getDataStructures()[3]).getData().intValue();
		
		Mapdata block = Map.getMap().getMapData(new Location(blockPos_X, blockPos_Y))[layer];
		if(block != null && !(block instanceof MapBlock)) {
			System.out.println("Error: BlockPart was given");
			block = ((MapDummieBlock)block).getBlock();
		}
		
		return new MapBlockDeleteEvent((MapBlock) block, doDrops);
	}
}
