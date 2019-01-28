package events.entity;

import Data.Direction;
import Data.Location;
import game.entity.player.Player;

public class PlayerMoveEvent extends EntityEvent{
	
	private int[] direction = new int[2];
	private boolean slowDown;

	public PlayerMoveEvent(Player player, Direction direction, boolean slowDown) {
		super(player, player.getLocation());
		this.direction[0] = direction.getX();
		this.direction[1] = direction.getY();
		this.slowDown = slowDown;
	}
	
	public PlayerMoveEvent(Player player, int x, int y, boolean slowDown) {
		super(player, player.getLocation());
		this.direction[0] = x;
		this.direction[1] = y;
		this.slowDown = slowDown;
	}

	public PlayerMoveEvent(Player player, Direction direction, boolean slowDown, Location currentPixellocation) {
		super(player, currentPixellocation);
		this.direction[0] = direction.getX();
		this.direction[1] = direction.getY();
		this.slowDown = slowDown;
	}

	public boolean isSlowDown() {
		return slowDown;
	}
	
	public int getMoveX() {
		return this.direction[0];
	}
	
	public int getMoveY() {
		return this.direction[0];
	}
	
}
