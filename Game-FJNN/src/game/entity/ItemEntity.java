package game.entity;

import java.util.ArrayList;

import Data.Location;
import Data.Image.Image;
import data.ImageData;
import game.entity.type.EntityType;
import game.inventory.items.ItemType;

public class ItemEntity extends Entity{

	private ItemType itemType;
	
	public ItemEntity(ItemType itemType, Location loc, int entityID){
		super(new ArrayList<>());
		entityTypes.add(EntityType.ItemEntity);	
		
		this.itemType = itemType;
		
		Image itemImage = new Image(loc.clone(), EntityType.ItemEntity.getSize(), "", itemType.getSpriteSheet(), null);
		itemImage.setSpriteState(itemType.getSpriteIds()[0]);
		
		super.create(entityID, EntityType.ItemEntity.createAnimation(false, 2+1, itemImage), loc, itemImage.getDimension(), EntityType.ItemEntity.getSpeed(), DEFAULT_DIRECTION, 2+1,
				new ImageData(new Location(0,0), itemImage));
		
		this.moveManager.setDoAccelerateXVelocity(false);
	}
	
	@Override
	public void tick(){
		super.tick();
	}
	
	public ItemType getItemType(){
		return this.itemType;
	}

}
