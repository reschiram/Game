package game.inventory.items;

import Data.Image.SpriteSheet;
import data.LightSpriteSheet;
import data.MapResource;
import game.inventory.equipment.EquipmentType;
import sprites.Sprites;

public class ItemType {

	public static ItemType Lamp;
	public static ItemType Dirt;
	public static ItemType Stone;
	public static ItemType Iron_Ore;
	public static ItemType Gold_Ore;
	public static ItemType Silver_Ore;
	public static ItemType Coal;
	
	public static EquipmentType DigTool;
	public static EquipmentType BuildTool;

	public static ItemType[] types;
	
	public static ItemType getItemType(String id) {
		for(ItemType type: types){
			if(type.getID().equals(id))return type;
		}
		return null;
	}

	public static void LoadMapresources() {
		Dirt		.MapID = MapResource.Dirt		.getID();
		Stone		.MapID = MapResource.Stone		.getID();
		Iron_Ore	.MapID = MapResource.Iron_Ore	.getID();
		Gold_Ore	.MapID = MapResource.Gold_Ore	.getID();
		Silver_Ore	.MapID = MapResource.Silver_Ore	.getID();
		Coal		.MapID = MapResource.Coal		.getID();
		Lamp		.MapID = MapResource.Lamp		.getID();
	}
	
	public static void Load(){
		Dirt  		= new ItemType		("Dirt"			, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												15);
		Stone 		= new ItemType		("Stone"		, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												12);
		Iron_Ore 	= new ItemType		("Iron_Ore"		, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												10);
		Gold_Ore 	= new ItemType		("Gold_Ore"		, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												11);
		Silver_Ore 	= new ItemType		("Silver_Ore"	, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												13);
		Coal	 	= new ItemType		("Coal"			, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												14);
		
		DigTool		= new EquipmentType	("DigTool"		,  1, LightSpriteSheet.getLightSpriteSheet(Sprites.Equipment	.getSpriteSheet()), game.inventory.equipment.tools.DigTool	.class, 1);
		BuildTool	= new EquipmentType	("BuildTool"	,  1, LightSpriteSheet.getLightSpriteSheet(Sprites.Equipment	.getSpriteSheet()), game.inventory.equipment.tools.BuildTool.class, 1);
		
		Lamp 		= new ItemType		("Lamp"			, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Blocks		.getSpriteSheet()), 												30);
	
		types = new ItemType[]{Dirt, Stone, Iron_Ore, Gold_Ore, Silver_Ore, Coal, Lamp, DigTool, BuildTool};
	}
	
	protected SpriteSheet sheet;
	protected int[] ids;
	protected int maxStackAmount;
	protected String id;
	private int MapID;
	
	protected ItemType(String id, int maxStackAmount, SpriteSheet sheet, int... ids){
		this.sheet = sheet;
		this.ids = ids;
		this.maxStackAmount = maxStackAmount;
		this.id = id;
	}

	public SpriteSheet getSpriteSheet() {
		return sheet;
	}

	public int[] getSpriteIds() {
		return ids;
	}

	public int getMaxStackAmount() {
		return maxStackAmount;
	}
	
	public String getID() {
		return id;
	}

	public int getMapID() {
		return MapID;
	}
	
}
