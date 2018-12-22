package game.pathFinder.system;

import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;

public class PathRequest {
	
	public static final int STATE_PENDING = 0;
	public static final int STATE_EXPIRED = 1;
	public static final int STATE_DONE = 2;
	public static final int STATE_ERROR = 3;
	public static final int STATE_NOPATHFOUND = 4;
	
	private static final int Default_Range = 3;
	
	private String id;
	private int state = STATE_PENDING;
	
	private Entity entity;
	private Location originLocation;
	private Location target;
	private Location offset;

	private Queue<Location> path = new Queue<>(); 
	
	public PathRequest(Entity entity, Location originLocation, Location target, Map map, Location offset) {
		this.entity = entity;
		this.originLocation = originLocation;
		this.offset = offset;
		this.target = correctTarget(target, map);
		if(target == null)this.state = STATE_ERROR;
		
		System.out.println("Target: "+this.target);
	}

	public void setId(String id) {
		this.id = id;
	}

	private Location correctTarget(Location blockTarget, Map map) {
		if(entity.canReach(blockTarget))return blockTarget.clone();
		Location[][] nodes = new Location[Default_Range][Default_Range];
		if(!map.entityCanAcces(entity, blockTarget.getX(), blockTarget.getY())){
			ArrayList<Location> last = new ArrayList<>();
			last.add(blockTarget);
			for(int d = 0; d<Default_Range*2; d++){
				ArrayList<Location> next = new ArrayList<>();
				for(Location node:last){
					for(Direction direc: Direction.values()){
						if(!direc.equals(Direction.NONE)){
							Location newLoc = new Location(node.getX() + direc.getX(), node.getY() + direc.getY());
							int x = newLoc.getX()-blockTarget.getX()+Default_Range/2;
							int y = newLoc.getY()-blockTarget.getY()+Default_Range/2;
							if(x >= 0 && y >= 0 && x < nodes.length && y < nodes[0].length){
								Location newNode = nodes[x][y];
								if(newNode==null){
									if(map.entityCanAcces(entity, map.getBlockXOver(newLoc.getX()), newLoc.getY()))return newLoc;
									else{
										nodes[x][y] = newLoc;
										next.add(newLoc);
									}
								}
							}
						}
					}
				}
				last = next;
			}
		}else return blockTarget;
		return null;
	}

	public Entity getEntity() {
		return entity;
	}

	public Location getOriginLocation() {
		return originLocation;
	}

	public Location getTarget() {
		return target;
	}

	public String getId() {
		return id;
	}

	public Queue<Location> getPath() {
		return path;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Location getOffset() {
		return offset;
	}

	public Location getOriginTarget() {
		return new Location((this.target.getX()*Map.DEFAULT_SQUARESIZE) + offset.getX(), (this.target.getY()*Map.DEFAULT_SQUARESIZE) + offset.getY());
	}
}
