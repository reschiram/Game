package client;

import data.DataPackage;
import data.PackageType;
import data.readableData.BooleanData;
import data.readableData.IntegerData;
import data.readableData.ReadableData;
import data.readableData.StringData;
import events.entity.EntityPathEvent;
import events.entity.EntityStatusEvent;
import events.entity.PlayerMoveEvent;
import game.inventory.items.ItemType;

public class GameCPM {
	
	public static final int DataPackage_MapDownloadInfos = 14;
	public static final int DataPackage_MapDownloadData = 16;
	
	public static final int DataPackage_EntityCreationRequest_Player = 18;
	public static final int DataPackage_EntityCreationRequest_Drone = 20;
	public static final int DataPackage_EntityCreationRequest_ItemEntity = 22;
	public static final int DataPackage_EntityCreationResponse = 24;
	
	public static final int DataPackage_InventoryRequest = 26;
	public static final int DataPackage_InventoryResponse = 28;
	
	public static final int DataPackage_PlayerMoved = 30;
	public static final int DataPackage_EntityPath = 32;
	public static final int DataPackage_EntityStatus = 34;
	
	public static final int DataPackage_ItemAdd = 36;
	public static final int DataPackage_ItemRemove = 38;
	
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
		
		//<=== Add Entity-Creation Packages ===>

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_Player, "EntityCreationRequest_Player",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_ItemEntity, "EntityCreationRequest_ItemEntity",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new StringData("ItemType", ItemType.Max_IDLength),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationRequest_Drone, "EntityCreationRequest_Drone",
				new IntegerData("Old_EntityRequest_ID"), new IntegerData("Current_EntityRequest_ID"), new IntegerData("EntityType"),
				new IntegerData("DroneType"), new IntegerData("EntityHost_ID"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));

		DataPackage.setType(new PackageType(DataPackage_EntityCreationResponse, "EntityCreationResponse",
				new IntegerData("EntityRequest_ID"), new IntegerData("Entity_ID")));
		
		//<=== Add Entity-Creation Packages ===>

		DataPackage.setType(new PackageType(DataPackage_InventoryRequest, "InventoryRequest",
				new IntegerData("Old_InventoryRequest_ID"), new IntegerData("Current_InventoryRequest_ID"),
				new IntegerData("Inventory_Slots"), new IntegerData("Equipment_Slots")));

		DataPackage.setType(new PackageType(DataPackage_InventoryResponse, "InventoryResponse",
				new IntegerData("InventoryRequest_ID"),	new IntegerData("Inventory_ID")));
		
		//<=== Add Entity-Event Packages ===>

		DataPackage.setType(new PackageType(DataPackage_PlayerMoved, "PlayerMoved", new IntegerData("Entity_ID"),
				new IntegerData("Move_X"), new IntegerData("Move_Y"), new BooleanData("Slow_Down"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_EntityPath, "EntityPath", new IntegerData("Entity_ID"),
				new IntegerData("Target_X"), new IntegerData("Target_Y"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_EntityStatus, "EntityStatus", new IntegerData("Entity_ID"),
				new BooleanData("Alive"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		DataPackage.setType(new PackageType(DataPackage_EntityStatus, "EntityStatus", new IntegerData("Entity_ID"),
				new BooleanData("Alive"),
				new IntegerData("PixelPos_X"), new IntegerData("PixelPos_Y")));
		
		//<=== Add Inventory-Event Packages ===>
		
		DataPackage.setType(new PackageType(DataPackage_ItemAdd, "ItemAdd", new IntegerData("Inventory_ID"),
				new StringData("ItemType", ItemType.Max_IDLength), new IntegerData("Amount")));
		
		DataPackage.setType(new PackageType(DataPackage_ItemRemove, "ItemRemove", new IntegerData("Inventory_ID"),
				new StringData("ItemType", ItemType.Max_IDLength), new IntegerData("Amount")));
	}

	public PackageType createPlayerMovedMessage(PlayerMoveEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getEntity().getID(),
				event.getMoveX(), event.getMoveY(), event.isSlowDown(),
				event.getEntity().getLocation().getX(),	event.getEntity().getLocation().getY());
	}

	public PackageType createEntityPathMessage(EntityPathEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getEntity().getID(),
				event.getPixelTarget().getX(), event.getPixelTarget().getY(),
				event.getEntity().getLocation().getX(),	event.getEntity().getLocation().getY());
	}

	public PackageType createEntityStatusMessage(EntityStatusEvent event) throws Exception {
		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getEntity().getID(),
				event.isAlive(),
				event.getEntity().getLocation().getX(),	event.getEntity().getLocation().getY());
	}

//	public PackageType createItemAddMessage(ItemAddEvent event) {
//		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getInv().getID(),
//				event.getItem().getItemType().getID(),	event.getItem().getAmount());
//	}
//
//	public PackageType createItemRemoveMessage(ItemAddEvent event) {
//		return PackageType.readPackageData(DataPackage_PlayerMoved, event.getInv().getID(),
//				event.getItem().getItemType().getID(),	event.getItem().getAmount());
//	}

}
