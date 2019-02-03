package data.entities;

import Data.Location;
import game.entity.type.EntityType;
import game.inventory.items.ItemType;

public class ServerItemEntity extends ServerEntity{

	private ServerEntity target;
	private ItemType type;	
	
	public ServerItemEntity(int id, Location pixeLoc, EntityType entityType, ItemType type) {
		super(id, pixeLoc, entityType);
		this.type = type;
	}

	public ItemType getType() {
		return type;
	}

	public ServerEntity getTarget() {
		return target;
	}

	public void setTarget(ServerEntity target) {
		this.target = target;
	}

	@Override
	public String getExtraInfos(long currentClientID) throws Exception {
		return "";
	}	

}
