package game.entity.player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.ImageData;
import data.Velocity;
import events.GameEventManager;
import events.entity.PlayerMoveEvent;
import game.entity.Entity;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneConstructor;
import game.entity.requester.EntityRequester;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
import game.entity.type.data.LightEntityData;
import game.entity.type.interfaces.EntityInventory;
import game.entity.type.interfaces.EntityLight;
import game.inventory.Inventory;
import game.inventory.ItemCollector;
import game.inventory.equipment.EquipmentInventory;
import game.map.Map;

public class Player extends Entity implements EntityInventory, EntityLight{
	
	private static Player player;
	public static Player getPlayer() {
		return player;
	}
	
	private static Location ScreenCenter = new Location(1920/2-Map.DEFAULT_SQUARESIZE/2, 1080/2-Map.DEFAULT_SQUARESIZE);
	
	private PlayerInterface playerInterface;
	private ItemCollector itemCollector;
	
	private ArrayList<Drone> drones = new ArrayList<>();
	
	public Player(Location location, int entityID, EquipmentInventory inv, boolean isOwnPlayer){
		super(new ArrayList<>());
		entityTypes.add(EntityType.Player);	
		entityTypes.add(EntityType.LightEntity);
		
		Image image = new Image(ScreenCenter, EntityType.Player.getSize(), "", EntityType.Player.getSpriteSheet(), null);
		super.create(entityID, EntityType.Player.createAnimation(false, DEFAULT_ENTITY_LAYER, image), ScreenCenter, EntityType.Player.getSize(), EntityType.Player.getSpeed(),
				Entity.DEFAULT_DIRECTION, DEFAULT_ENTITY_LAYER, new ImageData(new Location(0, 0), image));
		
		this.itemCollector = new ItemCollector(this, inv, 3.0);		
		
		if(isOwnPlayer) {
			this.playerInterface = new PlayerInterface(this);
			EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_StarterDrone);
			EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_BuildDrone);
			EntityRequester.getEntityRequester().requestDrone(this, DroneConstructor.DefaultDrone_DestructionDrone);
			player = this;
		}	
		
		this.setLocation(location.getX(), location.getY());	
	}
	
	@Override
	public void tick() {	
		
		if(player != null && player.equals(this)) {
			
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
			
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this));
			
			playerInterface.tick();
		}
		
		super.tick();
		itemCollector.tick();
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

	public Inventory getInventory() {
		return itemCollector.getInventory();
	}

	public ArrayList<Drone> getPlayerDrones() {
		return drones;
	}

	@Override
	public int getLightDistance() {
		LightEntityData lightdata = (LightEntityData)EntityType.Player.getData(EntityData.LIGHTENTITYDATA);
		return lightdata.getLightDistance();
	}

	@Override
	public double getLightStrength() {
		LightEntityData lightdata = (LightEntityData)EntityType.Player.getData(EntityData.LIGHTENTITYDATA);
		return lightdata.getLightStrength();
	}

	public void addDrone(Drone drone) {
		this.drones.add(drone);
	}

	public Velocity getVelocity() {
		return this.moveManager.getVelocity();
	}

	public void setVelocity(Velocity velocity) {
		this.moveManager.setVelocity(velocity);
	}

}
