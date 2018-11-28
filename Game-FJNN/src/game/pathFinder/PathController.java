package game.pathFinder;

import Data.Location;
import game.entity.Entity;
import game.map.Map;
import game.pathFinder.system.PathRequest;
import game.pathFinder.system.PathSystem;

public class PathController {
	
	private Entity entity;
	private PathRequest lastRequest;
	private PathSystem pathSystem;
	
	private boolean blocked = false;
	
	public PathController(Entity entity, PathSystem pathSystem) {
		this.entity = entity;
		this.pathSystem = pathSystem;
	}

	public boolean hasTarget() {
		return lastRequest!=null;
	}

	public int[] nextDirection() {
		if(!hasTarget() || !isDone()) return new int[]{0,0};
		Location target = lastRequest.getPath().get();
		target = new Location(target.getX()*Map.DEFAULT_SQUARESIZE + lastRequest.getOffset().getX(),
				target.getY()*Map.DEFAULT_SQUARESIZE + lastRequest.getOffset().getY());
		Location origin = new Location(entity.getBlockLocation().getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2,
				entity.getBlockLocation().getY()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2);
		
		int dx = target.getX()-origin.getX();
		if(dx> Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE/2)dx = dx - Map.getMap().getWidth() * Map.DEFAULT_SQUARESIZE;
		if(dx<-Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE/2)dx = dx + Map.getMap().getWidth() * Map.DEFAULT_SQUARESIZE;
		int dy = target.getY()-origin.getY();		
		
		System.out.println("move->"+dx+"|"+dy);
		
		if(Math.abs(dx)<entity.getWidth()/2 && Math.abs(dy)<entity.getHeight()/2){
			lastRequest.getPath().remove();
			if(lastRequest.getPath().isEmpty()){
				lastRequest = null;
				return new int[]{0,0};
			}
		}
		
		if(hasTarget() && dx == 0 && dy == 0) return nextDirection();
		
		return new int[]{Math.abs(dx)>=Math.abs(dy) ? (dx>0 ? -1:1):0, Math.abs(dy)>=Math.abs(dy) ? 0:(dy>0 ? -1:1)};
	}

	public void setBlockTarget(Location blockLocation) {
		if(this.blocked)return;
		
		if(blockLocation==null){
			this.lastRequest.setState(PathRequest.STATE_EXPIRED);
			this.lastRequest = null;
			return;
		}
		
		this.lastRequest = pathSystem.addPathRequest(this.entity, new Location(blockLocation.getX()*Map.DEFAULT_SQUARESIZE, blockLocation.getY()*Map.DEFAULT_SQUARESIZE));
	}

	public boolean reachedDestination() {
		return !hasTarget() || (isDone() && this.lastRequest.getPath().isEmpty());
	}

	public void setTarget(Location loc) {
		if(this.blocked)return;
		
		if(loc==null){
			this.lastRequest.setState(PathRequest.STATE_EXPIRED);
			this.lastRequest = null;
			return;
		}
		
		this.lastRequest = pathSystem.addPathRequest(this.entity, loc);
	}

	public Location getBlockTarget() {
		if(!this.hasTarget())return null;
		return lastRequest.getTarget();
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isDone() {
		return !hasTarget() || lastRequest.getState()!=PathRequest.STATE_PENDING;
	}

	public Location getTarget() {
		if(!this.hasTarget())return null;
		return new Location(lastRequest.getTarget().getX()*Map.DEFAULT_SQUARESIZE, lastRequest.getTarget().getY()*Map.DEFAULT_SQUARESIZE);
	}
	

}
