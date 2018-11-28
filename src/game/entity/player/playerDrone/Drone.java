package game.entity.player.playerDrone;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import data.ImageData;
import game.entity.Entity;
import game.entity.player.Player;
import game.entity.player.playerDrone.module.DroneModule;
import game.entity.type.EntityType;
import game.map.Map;
import game.pathFinder.PathController;

public class Drone extends Entity{

	protected PathController pathFinder;
	protected Player player;
	
	protected int[] lastMoved = new int[]{0,0};
	protected boolean isWorking = false;
	
	private HashMap<Class<?>, DroneModule> modules = new HashMap<>();
	
	public Drone(Player player){
		super(new ArrayList<>());
		this.entityTypes.add(EntityType.Drone);
		
		EntityType type = EntityType.Drone;
		Image image = new Image(player.getLocation().clone(), type.getSize(), "", type.getSpriteSheet(), null);
		image.setSpriteState(type.getSpriteIds()[0]);
		super.create(type.createAnimation(false, Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP+1, image), image.getLocation().clone(), type.getSize(), type.getSpeed(),
				DEFAULT_DIRECTION, Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP+1, new ImageData(new Location(0,0), image));
		
		this.player = player;		
		this.pathFinder = new PathController(this, Map.getMap().getPathSystem());
		
		this.moveManager.setDoAccelerateXVelocity(false);
		this.moveManager.setDoAccelerateYVelocity(false);
	}
	
	@Override
	public void tick(){
		super.tick();
		
		if(pathFinder.hasTarget()){
			int[] directions = this.pathFinder.nextDirection();
			
			this.lastMoved[0] = this.moveManager.canMoveX()!=0 ? directions[0] : 0;
			this.moveManager.move(Direction.getDirection(directions[0], 0));
			
			this.lastMoved[1] = this.moveManager.canMoveY()!=0 ? directions[0] : 0;
			this.moveManager.move(Direction.getDirection(0 , directions[1]));
		}else{
			this.lastMoved = new int[]{0,0};
		}
		
		for(DroneModule module : this.modules.values()){
			module.tick();
		}
	}

	public int[] getLastMoved() {
		return lastMoved;
	}

	public PathController getPathController() {
		return this.pathFinder;
	}

	public Player getHost() {
		return this.player;
	}

	public DroneModule getModule(Class<?> moduleClass) {
		return this.modules.get(moduleClass);
	}

	public void addModule(DroneModule module) {
		this.modules.put(module.getClass(), module);
		module.register(this);
	}

	public boolean isWorking() {
		return isWorking;
	}
	
	public void setIsWorking(boolean isWorking){
		this.isWorking = isWorking;
	}
	
}
