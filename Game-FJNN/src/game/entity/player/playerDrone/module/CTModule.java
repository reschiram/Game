package game.entity.player.playerDrone.module;

import java.util.HashMap;

import Data.Location;
import client.commData.DroneTargetInfos;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneTarget;
import game.map.Map;

public abstract class CTModule extends DroneModule{
	
	protected HashMap<Integer, DroneTarget> targets = new HashMap<>();
	private DroneTarget currentDroneTarget;

	protected double maxDistanceFromTarget;
	
	@Override
	public void tick() {
//		System.out.println(currentDroneTarget);
		if (currentDroneTarget != null && this.drone.getLastMoved()[0] == 0 && this.drone.getLastMoved()[1] == 0
				&& this.drone.getPathController().isDone()) {

			if (this.getDistance(currentDroneTarget) <= maxDistanceFromTarget) {
				this.drone.setIsWorking(true);
				if (currentDroneTarget.interact()) {
					this.drone.setIsWorking(false);
					currentDroneTarget = null;
				}
			}
		}
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
			if(currentDroneTarget != null && currentDroneTarget.equals(target)) currentDroneTarget = null;
			this.targets.remove(key);
		}
	}
	
	public boolean addTarget(DroneTarget target){		
		int key = target.getKey();
		System.out.println("try add target: " + target.getKey() + " -> " + hasTarget(key));
		if(hasTarget(key)) return false;		
		this.targets.put(key, target);
		return true;
	}
	
	public boolean hasTarget(int key) {
		System.out.println(this.targets.size() + "|" + this.targets.get(key));
		return this.targets.containsKey(key) && this.targets.get(key) != null;
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

	public void selectCurrentTarget(int targetKey) {
		this.currentDroneTarget = this.targets.get(targetKey);
	}
	
}
