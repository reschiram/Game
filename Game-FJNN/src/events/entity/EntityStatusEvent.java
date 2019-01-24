package events.entity;

import game.entity.Entity;

public class EntityStatusEvent extends EntityEvent {

	private boolean alive;

	public EntityStatusEvent(Entity entity, boolean alive) {
		super(entity);
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

}
