package game.inventory.items;

import Data.Image.SpriteSheet;
import data.LightSpriteSheet;
import game.inventory.equipment.EquipmentType;
import sprites.Sprites;

public class ItemType {

	public static ItemType Dirt;
	public static ItemType Stone;
	
	public static EquipmentType DigTool;

	public static ItemType[] types;
	
	public static ItemType getItemType(String id) {
		for(ItemType type: types){
			if(type.getID().equals(id))return type;
		}
		return null;
	}
	
	public static void Load(){
		Dirt  	= new ItemType		("Dirt"		, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												0);
		Stone 	= new ItemType		("Stone"	, 64, LightSpriteSheet.getLightSpriteSheet(Sprites.Items		.getSpriteSheet()), 												1);
		DigTool	= new EquipmentType	("DigTool"	,  1, LightSpriteSheet.getLightSpriteSheet(Sprites.Equipment	.getSpriteSheet()), game.inventory.equipment.tools.DigTool.class, 	1);
	
		types = new ItemType[]{Dirt, Stone, DigTool};
	}
	
	protected SpriteSheet sheet;
	protected int[] ids;
	protected int maxStackAmount;
	protected String id;
	
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
	
}
