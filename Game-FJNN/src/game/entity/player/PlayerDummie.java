package game.entity.player;

import Data.Location;
import Data.Image.Image;
import data.ImageData;
import game.entity.Entity;
import game.entity.type.EntityType;
import game.inventory.equipment.EquipmentInventory;

public class PlayerDummie extends PlayerContext{
	
	public PlayerDummie(Location location, int entityID, EquipmentInventory inv, boolean isOwnEntity){
		super();
		
		Image image = new Image(location, EntityType.Player.getSize(), "", EntityType.Player.getSpriteSheet(), null);
		image.setSpriteState(EntityType.Player.getSpriteIds()[0]);
		super.create(entityID, EntityType.Player.createAnimation(false, DEFAULT_ENTITY_LAYER, image), location, EntityType.Player.getSize(), EntityType.Player.getSpeed(),
				Entity.DEFAULT_DIRECTION, DEFAULT_ENTITY_LAYER, isOwnEntity, new ImageData(new Location(0, 0), image));
		
		load(inv);
	}
}
