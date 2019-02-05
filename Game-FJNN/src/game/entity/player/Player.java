package game.entity.player;

import java.awt.event.KeyEvent;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.ImageData;
import events.GameEventManager;
import events.entity.PlayerMoveEvent;
import game.GameManager;
import game.entity.Entity;
import game.entity.player.playerDrone.DroneConstructor;
import game.entity.requester.EntityRequester;
import game.entity.type.EntityType;
import game.inventory.equipment.EquipmentInventory;
import game.map.Map;

public class Player extends PlayerContext{
	
	private static final int tickDiffProPlayerUpdate = 10;
	
	private static Player player;
	public static Player getPlayer() {
		return player;
	}
	
	private static Location ScreenCenter = new Location(1920/2-Map.DEFAULT_SQUARESIZE/2, 1080/2-Map.DEFAULT_SQUARESIZE);	
	private PlayerInterface playerInterface;
	
	public Player(Location location, int entityID, EquipmentInventory inv){
		super();
		
		Image image = new Image(ScreenCenter, EntityType.Player.getSize(), "", EntityType.Player.getSpriteSheet(), null);
		image.setSpriteState(EntityType.Player.getSpriteIds()[0]);
		super.create(entityID, EntityType.Player.createAnimation(false, DEFAULT_ENTITY_LAYER + 1, image), ScreenCenter, EntityType.Player.getSize(), EntityType.Player.getSpeed(),
				Entity.DEFAULT_DIRECTION, DEFAULT_ENTITY_LAYER + 1, new ImageData(new Location(0, 0), image));
		
		load(inv);		
		this.playerInterface = new PlayerInterface(this);
		
		EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_StarterDrone);
		EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_BuildDrone);
		EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_DestructionDrone);
		player = this;
		
		this.setLocation(location.getX(), location.getY());	
	}
	
	long lastTick = 0;
	@Override
	public void tick() {	
			
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_W) || Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_SPACE)){
			this.moveManager.move(Direction.UP);
		}
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_A)){
			this.moveManager.move(Direction.LEFT);
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_D)){
			this.moveManager.move(Direction.RIGHT);
		}else {
			this.moveManager.slowDownXVelocity();
		}
			
		if(GameManager.TickManager.getCurrentTick() - lastTick > tickDiffProPlayerUpdate) {
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this));
			lastTick = GameManager.TickManager.getCurrentTick();
		}
			
		playerInterface.tick();
		
		super.tick();
	}

	
	@Override
	public void move(Direction d) {
		if(d.equals(Direction.DOWN) || d.equals(Direction.NONE)) {
			this.moveManager.slowDownXVelocity();			
		}else super.move(d);
	}
	
	
	@Override
	public void setLocation(int x, int y){
		if(player == null || !player.equals(this)) {
			super.setLocation(x, y);
			return;
		}			
		
		x = Map.getMap().getXOver(x);
		if(x == hitbox.getX() && y == hitbox.getY())return;
		
		Map.getMap().setMoved(-(x-this.getX()), -(y-this.getY()));
		this.hitbox.setLocation(x, y);
	
	}

}
