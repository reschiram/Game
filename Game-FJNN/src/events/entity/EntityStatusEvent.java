package events.entity;

import Data.Location;
import game.entity.Entity;

public class EntityStatusEvent extends EntityEvent<Entity> {

	private boolean alive;

	public EntityStatusEvent(Entity entity, boolean alive) {
		super(entity, entity.getLocation());
		this.alive = alive;
	}

	public EntityStatusEvent(Entity entity, boolean alive, Location currentPixelLoc) {
		super(entity, currentPixelLoc);
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

}
