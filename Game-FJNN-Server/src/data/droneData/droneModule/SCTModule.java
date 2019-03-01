package data.droneData.droneModule;

import java.util.HashMap;

import client.ComsData;
import data.droneData.actionTarget.ActionTarget;
import data.map.ServerMap;
import game.map.Map;

public class SCTModule extends SDroneModule{
	
	private static final int invalidTargetId = -1;
	
	private HashMap<Integer, ActionTarget> targets = new HashMap<>();
	private int currentTarget;
	
	private int actionTargetType;
	
	public SCTModule(int actionTargetType) {
		this.actionTargetType = actionTargetType;
	}

	@Override
	public void tick() {
		if (this.drone.hasModule(SELModule.class)) {
			if (((SELModule) this.drone.getModule(SELModule.class)).isCharging()) return;
		}
		
		if(!hasTarget()){
			if (!targets.isEmpty()) {
				currentTarget = getNextDroneTarget();
				System.out.println("new Target: " + currentTarget);
				if (hasTarget()) {
					ActionTarget nextTraget = targets.get(currentTarget);

					this.drone.getSEPathManager().setBlockTarget(nextTraget.getBlockLocation(), ComsData.TargetLevel_Drone_CommandActionTarget);
					this.drone.getCurrentSMap().getMapSM().publishDroneCTSelectionUpdate(this.drone.getId(), nextTraget);
				}
			}
		} else {
			if (!this.drone.getSEPathManager().hasTarget()) {
				ActionTarget target = this.targets.get(currentTarget);
				this.drone.getSEPathManager().setBlockTarget(target.getBlockLocation(),
						ComsData.TargetLevel_Drone_CommandActionTarget);
				this.drone.getCurrentSMap().getMapSM().publishDroneCTSelectionUpdate(this.drone.getId(), target);
			}
		}
	}
	
	private boolean hasTarget() {
		return this.currentTarget != invalidTargetId && targets.get(currentTarget) != null;
	}

	private int getNextDroneTarget() {
		int distance = -1;
		ActionTarget next = null;
		for (ActionTarget target : targets.values()) {
			int d = getDistance(target);
			if (distance == -1 || d < distance) {
				if (this.drone.canReach(target.getBlockLocation())
						&& target.getType().canInteract(target.getBlockLocation(), this.drone.getCurrentSMap())) {
					distance = d;
					next = target;
				}
			}
		}
		
		return next == null ? invalidTargetId : next.getKey(this.drone.getCurrentSMap());
	}

	private int getDistance(ActionTarget target) {
		ServerMap map = this.drone.getCurrentSMap();
		int width = map.getWidth();
		
		int x = Math.abs(
					map.getXOver(this.drone.getPixelHitbox().getX() + this.drone.getPixelHitbox().getWidth() / 2)
					- map.getXOver(target.getBlockLocation().getX() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE / 2)
		);		
		
		if (x > (width * Map.DEFAULT_SQUARESIZE) / 2) {
			x -= width * Map.DEFAULT_SQUARESIZE;
		} if (x < -(width * Map.DEFAULT_SQUARESIZE) / 2) {
			x += width * Map.DEFAULT_SQUARESIZE;
		}
		
		int y = Math.abs(this.drone.getPixelHitbox().getY() + this.drone.getPixelHitbox().getHeigth() / 2
				- (target.getBlockLocation().getY() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE / 2));

		return (int) Math.sqrt(x * x + y * y);
	}

	public boolean addTarget(ActionTarget newTarget) {
		if(newTarget.getType().getTypeId() != actionTargetType) return false;
		System.out.println("add Target");
		this.targets.put(newTarget.getKey(this.drone.getCurrentSMap()), newTarget);
		
		return true;
	}

	public boolean removeTarget(ActionTarget target) {
		if(target.getType().getTypeId() != actionTargetType) return false;
		
		int key = target.getKey(this.drone.getCurrentSMap());
		this.targets.remove(key);
		
		if(currentTarget == key) currentTarget = invalidTargetId;
		
		return true;
	}
}