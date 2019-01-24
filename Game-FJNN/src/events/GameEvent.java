package events;

import game.GameManager;

public abstract class GameEvent {
	
	private boolean active;
	private long publishedTick;
	
	public GameEvent() {
		this.active = true;
		this.publishedTick = GameManager.TickManager.getCurrentTick();
	}

	public boolean isActive() {
		return active;
	}

	public long getPublishedTick() {
		return publishedTick;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
