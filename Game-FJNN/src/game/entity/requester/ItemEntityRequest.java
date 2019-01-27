package game.entity.requester;

import Data.Location;
import game.entity.ItemEntity;
import game.inventory.items.ItemType;

public class ItemEntityRequest extends EntityRequest{

	private ItemType itemType;

	public ItemEntityRequest(ItemType itemType, Location blockSpawn) {
		super(blockSpawn);
		this.itemType = itemType;
	}
	
	public ItemType getItemType() {
		return itemType;
	}

	@Override
	public void spawnEntity(int entityID) {
		new ItemEntity(itemType, new Location(getPixelSpawn_X(), getPixelSpawn_Y()), entityID).show();
	}

}
