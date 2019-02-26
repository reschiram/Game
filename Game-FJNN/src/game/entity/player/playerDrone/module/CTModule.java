package game.entity.player.playerDrone.module;

import java.util.HashMap;

import Data.Location;
import client.commData.DroneTargetInfos;
import data.MapResource;
import events.GameEventManager;
import events.entity.DroneUpdateEvent;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneTarget;
import game.map.Map;

public abstract class CTModule extends DroneModule{
	
	protected HashMap<Integer, DroneTarget> targets = new HashMap<>();
	private DroneTarget currentDroneTarget;

	protected double maxDistanceFromTarget;
	
	@Override
	public void tick() {
		if(currentDroneTarget==null){
			 if(!targets.isEmpty()){
				currentDroneTarget = getNextDroneTarget();
				GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, null));
				if(currentDroneTarget!=null){
					if(canSetDroneTarget()) {
						this.drone.getPathController().setTarget(currentDroneTarget.getPixelLocation(), DroneModule.publishPathToServer);
					}
				}
			 }
		}else{
			if(this.drone.getLastMoved()[0] == 0 && this.drone.getLastMoved()[1] == 0 && this.drone.getPathController().isDone()){
				
				int distance = this.getDistance(currentDroneTarget);
				
				if(distance <= maxDistanceFromTarget){
					this.drone.setIsWorking(true);
					if(currentDroneTarget.interact()){
						this.drone.setIsWorking(false);
					}
				}else{
					if(canSetDroneTarget()) {
						this.drone.getPathController().setTarget(currentDroneTarget.getPixelLocation(), DroneModule.publishPathToServer);
					}
				}
			} 
		}
	}

	private boolean canSetDroneTarget() {
		if(!this.drone.getPathController().hasTarget())return true;
		CTModule module = null;
		if(this instanceof CTDModule){
			module = (CTModule) this.drone.getModule(CTBModule.class);
		}else if(this instanceof CTBModule){
			module = (CTModule) this.drone.getModule(CTDModule.class);
		}
		
//		System.out.println("canSetDroneTarget: "+this.drone.getPathController().getTarget().isEqual(currentDroneTarget.getPixelLocation())+"|"+
//				(!this.drone.getPathController().getBlockTarget().isEqual(this.drone.getHost().getBlockLocation()) || this.drone.getPathController().reachedDestination())+"|"+
//				(module == null || module.currentDroneTarget==null || (!this.drone.getPathController().getTarget().isEqual(module.currentDroneTarget.getPixelLocation()) || this.drone.getPathController().reachedDestination())));
//		
//		System.out.println(this.drone.getPathController().getTarget()+"<~>"+currentDroneTarget.getPixelLocation());
		
		if( !this.drone.getPathController().getTarget().isEqual(currentDroneTarget.getPixelLocation()) &&
		   (!this.drone.getPathController().getBlockTarget().isEqual(this.drone.getHost().getBlockLocation()) || this.drone.getPathController().reachedDestination()) &&
		   (module == null || module.currentDroneTarget==null || (!this.drone.getPathController().getTarget().isEqual(module.currentDroneTarget.getPixelLocation()) || this.drone.getPathController().reachedDestination())))return true;
		return false;
	}

	private DroneTarget getNextDroneTarget() {
		int distance = -1;
		DroneTarget next = null;
		for(DroneTarget target: targets.values()){
			int d = getDistance(target);
			if(distance == -1 || d < distance){
				boolean canReach = this.drone.canReach(target.getBlockLocation()); 
				if(canReach && this.canInteract(target.getBlockLocation())){
					distance = d;
					next = target;
				}
			}
		}
		return next;
	}

	protected boolean canInteract(Location location) {
		return true;
	}

	private int getDistance(DroneTarget target) {
		int x = Math.abs(Map.getMap().getXOver(this.drone.getX()+this.drone.getWidth() /2) - Map.getMap().getXOver(target.getBlockLocation().getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));
		if(x> (Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)/2)x-=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
		if(x<-(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)/2)x+=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
		int y = Math.abs(					   this.drone.getY()+this.drone.getHeight()/2  - 					  (target.getBlockLocation().getY()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));

		return (int) Math.sqrt(x*x+y*y);
	}

	public void removeTarget(Location loc) {		
		int key = loc.getX()+loc.getY()*Map.getMap().getWidth();
		if(targets.containsKey(key)){
			DroneTarget target = this.targets.get(key);
			target.removeDrone(this.drone);
			this.targets.remove(key);
			GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, new DroneTargetInfos(target, false)));
		}
		if(currentDroneTarget!=null && this.currentDroneTarget.getBlockLocation().equals(loc)){
			this.drone.getPathController().setTarget(null, DroneModule.publishPathToServer);
			GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, new DroneTargetInfos(currentDroneTarget, false)));
			this.currentDroneTarget=null;
		}
	}
	
	public boolean addTarget(DroneTarget target){		
		Location location = target.getBlockLocation();
		int key = location.getX()+location.getY()*Map.getMap().getWidth();		
		if(hasTarget(key, target)) return false;
		targets.put(location.getX()+location.getY()*Map.getMap().getWidth(), target);
		GameEventManager.getEventManager().publishEvent(new DroneUpdateEvent(this.drone, new DroneTargetInfos(target, true)));
		return true;
	}
	
	private boolean hasTarget(int key, DroneTarget target) {
		if(targets.containsKey(key)){
			return true;
		}
		return false;
	}
	
	@Override
	public void register(Drone drone) {
		super.register(drone);
		
		int maxx = Map.DEFAULT_SQUARESIZE/2+drone.getWidth();
		int maxy = Map.DEFAULT_SQUARESIZE/2+drone.getHeight();
		
		maxDistanceFromTarget = Math.sqrt(maxx*maxx+maxy*maxy);
	}

	public DroneTargetInfos getCurrentTarget() {
		return new DroneTargetInfos(currentDroneTarget, false);
	}

	public void updateTarget(DroneTargetInfos newCurrentTarget) {
		if(newCurrentTarget == null) return;	
		int newKey = newCurrentTarget.getBlockLocation().getX()+newCurrentTarget.getBlockLocation().getY()*Map.getMap().getWidth();
		
		if(currentDroneTarget != null && (newCurrentTarget.isDone() || newCurrentTarget.isNull())) {			
			this.currentDroneTarget = null;
			this.drone.getPathController().setTarget(null, false);			
		}
		
		if (newCurrentTarget.isNull()) {			
			if(this.targets.containsKey(newKey)) {
				DroneTarget target = this.targets.get(newKey);
				this.targets.remove(newKey);
				target.removeDrone(this.drone);
			}
			return;
		} else if (newCurrentTarget.isDone()) {			
			if(this.targets.containsKey(newKey)) {
				DroneTarget target = this.targets.get(newKey);
				this.targets.remove(newKey);
				target.setDone(true);
				target.interact();
			}
			return;
		}
		
		DroneTarget newDroneTarget = this.targets.get(newKey);
		if(newCurrentTarget != null) {
			this.currentDroneTarget = newDroneTarget;
			this.drone.getPathController().setTarget(currentDroneTarget.getPixelLocation(), false);
		}
	}
	
}
