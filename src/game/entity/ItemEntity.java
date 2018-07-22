package game.entity;

import java.util.ArrayList;

import Data.Location;
import Data.Image.Image;
import data.ImageData;
import game.entity.type.EntityType;
import game.inventory.items.ItemType;
import game.map.Map;

public class ItemEntity extends Entity{

	private ItemType itemType;
	
	public ItemEntity(ItemType itemType, Location loc){
		super(new ArrayList<>());
		entityTypes.add(EntityType.ItemEntity);	
		
		this.itemType = itemType;
		
		Image itemImage = new Image(loc.clone(), EntityType.ItemEntity.getSize(), "", itemType.getSpriteSheet(), null);
		itemImage.setSpriteState(itemType.getSpriteIds()[0]);
		
		super.create(EntityType.ItemEntity.createAnimation(false, 2+1, itemImage), loc, itemImage.getDimension(), EntityType.ItemEntity.getSpeed(), DEFAULT_DIRECTION, 2+1,
				new ImageData(new Location(0,0), itemImage));
	}
	
	@Override
	public void tick(){
		super.tick();
	}
	
	public ItemType getItemType(){
		return this.itemType;
	}
//	
//	@Override
//	public void setLocation(int x, int y){
//		x = Map.getMap().getXOver(x);
//		if(x == hitbox.getX() && y == hitbox.getY())return;
//		for(int i = 0; i<this.images.length; i++){
//			this.images[i].getImage().setLocation(new Location(this.images[i].getLocation().getX()+x, this.images[i].getLocation().getY()+y));
//		}
//		this.hitbox.setLocation(x, y);
//	}

}
