package game.entity.player.playerDrone;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import client.commData.DroneTargetInfos;
import data.ImageData;
import events.entity.DroneUpdateEvent;
import game.entity.Entity;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.DroneModule;
import game.entity.player.playerDrone.module.ELModule;
import game.entity.type.EntityType;
import game.entity.type.interfaces.PathUser;
import game.map.Map;
import game.pathFinder.PathController;

public class Drone extends Entity implements PathUser{

	protected PathController pathFinder;
	protected DroneHost host;
	
	protected int[] lastMoved = new int[]{0,0};
	protected boolean isWorking = false;
	
	private HashMap<Class<?>, DroneModule> modules = new HashMap<>();
	
	public Drone(DroneHost host, int entityID){
		super(new ArrayList<>());
		this.entityTypes.add(EntityType.Drone);
		
		EntityType type = EntityType.Drone;
		Image image = new Image(host.getLocation().clone(), type.getSize(), "", type.getSpriteSheet(), null);
		image.setSpriteState(type.getSpriteIds()[0]);
		super.create(entityID, type.createAnimation(false, Entity.DEFAULT_ENTITY_LAYER, image), image.getLocation().clone(), type.getSize(), type.getSpeed(),
				DEFAULT_DIRECTION, Entity.DEFAULT_ENTITY_LAYER, new ImageData(new Location(0,0), image));
		
		this.host = host;		
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

	public DroneHost getHost() {
		return this.host;
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

	public void update(DroneUpdateEvent droneUpdate) {
		if(this.modules.containsKey(ELModule.class)) {
			ELModule module = (ELModule) this.modules.get(ELModule.class);	
			module.updateEnergyLoad(droneUpdate.getDroneEnergy(), droneUpdate.isDroneCharging());
		}
		
		DroneTargetInfos changeInfos = droneUpdate.getDroneTargetInfosChange();
		if(changeInfos != null && !changeInfos.isNull() && !changeInfos.isDone()) {
			if(droneUpdate.getDroneTargetInfosChange().isBuild()) {
				this.host.addBuildTarget(changeInfos.getBlockLocation(), changeInfos.doAdd() ? changeInfos.getResID() : -1);
			} else {
				this.host.addDestructionTarget(changeInfos.getBlockLocation(), changeInfos.doAdd());
			}
		}
		
		if(this.modules.containsKey(CTBModule.class)) {
			CTBModule module = (CTBModule) this.modules.get(CTBModule.class);
			module.updateTarget(droneUpdate.getCurrentBDroneTarget());
		}
	}
	
}
