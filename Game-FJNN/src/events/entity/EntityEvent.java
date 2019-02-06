package events.entity;

import Data.Location;
import events.GameEvent;
import game.entity.Entity;

public abstract class EntityEvent<EntityType extends Entity> extends GameEvent{
	
	private EntityType entity;
	private Location currentPixelLoc;
	
	public EntityEvent(EntityType entity, Location currentPixelLoc) {
		super();
		this.entity = entity;
		this.currentPixelLoc = currentPixelLoc;
	}

	public EntityType getEntity() {
		return entity;
	}

	public Location getCurrentPixelLocation() {
		return currentPixelLoc;
	}

}
