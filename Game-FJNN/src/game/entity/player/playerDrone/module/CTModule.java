package game.entity.player.playerDrone.module;

import java.util.HashMap;

import Data.Location;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneTarget;
import game.map.Map;

public abstract class CTModule extends DroneModule{
	
	protected HashMap<Integer, DroneTarget> targets = new HashMap<>();
	protected DroneTarget currentDroneTarget;

	protected double maxDistanceFromTarget;
	
	@Override
	public void tick() {
		if(currentDroneTarget==null){
			 if(!targets.isEmpty()){
				currentDroneTarget = getNextDroneTarget();
				if(currentDroneTarget!=null){
//					System.out.println("NextDroneTarget");
					if(canSetDroneTarget())this.drone.getPathController().setTarget(currentDroneTarget.getPixelLocation());
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
//					System.out.println("CurrentDroneTarget");
					if(canSetDroneTarget())this.drone.getPathController().setTarget(currentDroneTarget.getPixelLocation());
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
				boolean canReach = this.drone.canReach(target.getLocation()); 
				if(canReach && this.canInteract(target.getLocation())){
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
		int x = Math.abs(Map.getMap().getXOver(this.drone.getX()+this.drone.getWidth() /2) - Map.getMap().getXOver(target.getLocation().getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));
		if(x> (Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)/2)x-=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
		if(x<-(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)/2)x+=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
		int y = Math.abs(					   this.drone.getY()+this.drone.getHeight()/2  - 					  (target.getLocation().getY()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));

		return (int) Math.sqrt(x*x+y*y);
	}

	public void removeTarget(Location loc) {
		int key = loc.getX()+loc.getY()*Map.getMap().getWidth();
		if(targets.containsKey(key)){
			DroneTarget target = this.targets.get(key);
			target.removeDrone(this.drone);
			this.targets.remove(key);
		}
		if(currentDroneTarget!=null && this.currentDroneTarget.getLocation().equals(loc)){
			this.currentDroneTarget=null;
			this.drone.getPathController().setTarget(null);
		}
	}
	
	public boolean addTarget(DroneTarget target){
		Location location = target.getLocation();
		int key = location.getX()+location.getY()*Map.getMap().getWidth();		
		if(hasTarget(key, target)) return false;
		targets.put(location.getX()+location.getY()*Map.getMap().getWidth(), target);
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
}
