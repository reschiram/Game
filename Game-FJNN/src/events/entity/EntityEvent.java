package events.entity;

import events.GameEvent;
import game.entity.Entity;

public abstract class EntityEvent extends GameEvent{
	
	private Entity entity;

	public EntityEvent(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
