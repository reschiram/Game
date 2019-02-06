package events.entity;

import Data.Location;
import data.Velocity;
import game.entity.player.PlayerContext;

public class PlayerMoveEvent extends EntityEvent<PlayerContext>{
	private Velocity velocity;

	public PlayerMoveEvent(PlayerContext player) {
		super(player, player.getLocation());
		this.velocity = player.getVelocity();
	}
	
	public PlayerMoveEvent(PlayerContext player, int velocityX, int velocityY, Location pixelLoc) {
		super(player, pixelLoc);
		this.velocity = new Velocity(velocityX, velocityY);
	}

	public Velocity getVelocity() {
		return velocity;
	}
	
}
