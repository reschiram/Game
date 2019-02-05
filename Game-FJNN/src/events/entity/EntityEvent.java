package events.entity;

import Data.Location;
import events.GameEvent;
import game.entity.Entity;

public abstract class EntityEvent extends GameEvent{
	
	private Entity entity;
	private Location currentPixelLoc;
	
	public EntityEvent(Entity entity, Location currentPixelLoc) {
		super();
		this.entity = entity;
		this.currentPixelLoc = currentPixelLoc;
	}

	public Entity getEntity() {
		return entity;
	}

	public Location getCurrentPixelLocation() {
		return currentPixelLoc;
	}

}
