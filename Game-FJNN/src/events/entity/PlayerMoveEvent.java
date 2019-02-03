package events.entity;

import Data.Location;
import data.Velocity;
import game.entity.player.Player;

public class PlayerMoveEvent extends EntityEvent{
	private Velocity velocity;

	public PlayerMoveEvent(Player player) {
		super(player, player.getLocation());
		this.velocity = player.getVelocity();
	}
	
	public PlayerMoveEvent(Player player, int velocityX, int velocityY, Location pixelLoc) {
		super(player, pixelLoc);
		this.velocity = new Velocity(velocityX, velocityY);
	}

	public Velocity getVelocity() {
		return velocity;
	}
	
}
