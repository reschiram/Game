package game.entity.player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.ImageData;
import events.GameEventManager;
import events.entity.PlayerMoveEvent;
import game.entity.Entity;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneConstructor;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
import game.entity.type.data.EntityInventoryData;
import game.entity.type.data.LightEntityData;
import game.entity.type.interfaces.EntityInventory;
import game.entity.type.interfaces.EntityLight;
import game.inventory.Inventory;
import game.inventory.ItemCollector;
import game.inventory.equipment.EquipmentInventory;
import game.inventory.equipment.tools.BuildTool;
import game.inventory.equipment.tools.DigTool;
import game.map.Map;

public class Player extends Entity implements EntityInventory, EntityLight{
	
	private static Location ScreenCenter = new Location(1920/2-Map.DEFAULT_SQUARESIZE/2, 1080/2-Map.DEFAULT_SQUARESIZE);
	
	private PlayerInterface playerInterface;
	private ItemCollector itemCollector;
	
	private ArrayList<Drone> drones = new ArrayList<>();
	private EquipmentInventory inventory;
	
	public Player(Location location){
		super(new ArrayList<>());
		entityTypes.add(EntityType.Player);	
		entityTypes.add(EntityType.LightEntity);
		
		Image image = new Image(ScreenCenter, EntityType.Player.getSize(), "", EntityType.Player.getSpriteSheet(), null);
		super.create(EntityType.Player.createAnimation(false, DEFAULT_ENTITY_LAYER, image), ScreenCenter, EntityType.Player.getSize(), EntityType.Player.getSpeed(),
				Entity.DEFAULT_DIRECTION, DEFAULT_ENTITY_LAYER, new ImageData(new Location(0, 0), image));

		this.setLocation(location.getX(), location.getY());
		
		this.playerInterface = new PlayerInterface(this);
		
		EntityInventoryData invData = (EntityInventoryData)EntityType.Player.getData(EntityData.ENTITYINVENTOTYDATA);
		this.itemCollector = new ItemCollector(this, invData.createInventory(), 3.0);
		this.inventory = (EquipmentInventory) itemCollector.getInventory();
		
		this.inventory.addItem(new DigTool());
		this.inventory.addItem(new BuildTool());
		
		Drone drone = DroneConstructor.constructStarterDrone(this);
		drone.show();
		this.drones.add(drone);
		drone = DroneConstructor.constructStarterBuildDrone(this);
		drone.show();
		this.drones.add(drone);
		drone = DroneConstructor.constructStarterDestructionDrone(this);
		drone.show();
		this.drones.add(drone);
		
	}
	
	@Override
	public void tick() {	
		
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_W) || Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_SPACE)){
			this.moveManager.move(Direction.UP);
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this, Direction.UP, false));
		}
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_A)){
			this.moveManager.move(Direction.LEFT);
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this, Direction.LEFT, false));
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_D)){
			this.moveManager.move(Direction.RIGHT);
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this, Direction.RIGHT, false));
		}else {
			this.moveManager.slowDownXVelocity();
			GameEventManager.getEventManager().publishEvent(new PlayerMoveEvent(this, 1, 0, true));
		}

		super.tick();
		
		playerInterface.tick();
		itemCollector.tick();
	}
	
	@Override
	public void setLocation(int x, int y){
//		System.out.println(x+"->"+y);
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

}
