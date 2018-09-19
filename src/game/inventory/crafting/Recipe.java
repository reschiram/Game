package game.inventory.crafting;

import data.MapResource;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class Recipe {
	
	public static Recipe Dirt_Block;
	public static Recipe Stone_Block;
	public static Recipe Lamp_Block;
	
	private static Recipe[] Recipes;
	
	public static void Load(){
		Dirt_Block 		= new Recipe(MapResource.Dirt	.getID(), new Item(ItemType.Dirt	, 3)							);
		Stone_Block 	= new Recipe(MapResource.Stone	.getID(), new Item(ItemType.Stone	, 4)							);
		Lamp_Block 		= new Recipe(MapResource.Lamp	.getID(), new Item(ItemType.Stone	, 3), new Item(ItemType.Coal, 1));
		
		Recipes = new Recipe[]{Dirt_Block, Stone_Block, Lamp_Block};
	}
	
	public static Recipe getRecipe(int mapResourceID){
		for(Recipe recipe: Recipes){
			if(recipe.getResult() == mapResourceID)return recipe;
		}
		return null;
	}
	
	private Item[] items;
	private int result;
	
	public Recipe(int result, Item... items){
		this.result = result;
		this.items = items;
	}

	public Item[] getItems() {
		return items;
	}

	public int getResult() {
		return result;
	}
	
	public boolean craft(Inventory inv){
		for(Item item: this.items){
			if(!inv.hasItem(item))return false;
		}
		for(Item item: this.items)inv.removeItem(item);
		return true;
	}

}
