package events.entity;

import Data.Location;
import game.entity.Entity;

public class EntityPathEvent extends EntityEvent<Entity>{
	
	private Location pixelTarget;

	public EntityPathEvent(Entity entity, Location pixelTarget) {
		super(entity, entity.getLocation());
		this.pixelTarget = pixelTarget;
	}

	public EntityPathEvent(Entity entity, Location pixelTarget, Location currentPixelLoc) {
		super(entity, currentPixelLoc);
		this.pixelTarget = pixelTarget;
	}

	public Location getPixelTarget() {
		return pixelTarget;
	}

}
