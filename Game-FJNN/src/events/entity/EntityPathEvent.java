package events.entity;

import Data.Location;
import game.entity.Entity;

public class EntityPathEvent extends EntityEvent{
	
	private Location pixelTarget;

	public EntityPathEvent(Entity entity, Location pixelTarget) {
		super(entity);
		this.pixelTarget = pixelTarget;
	}

	public Location getPixelTarget() {
		return pixelTarget;
	}

}
