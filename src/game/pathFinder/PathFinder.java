package game.pathFinder;

import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import game.entity.Entity;
import game.gridData.map.Mapdata;
import game.map.Map;

public class PathFinder {
	
	private static int maxRange = 3;
	private static int maxRangeObstacle = 14;
	
	private Entity entity;
	private Location target;

	private int stage = -1;
	private Location possibleTargets;
	
	private ArrayList<Location> path = new ArrayList<>();
	
	public PathFinder(Entity entity, int maxPath){
		this.entity = entity;
	}
	
	public boolean hasTarget(){
		return target!=null && new Location(target.getX()+Map.DEFAULT_SQUARESIZE/2, target.getY()+Map.DEFAULT_SQUARESIZE/2).distance_Math(entity.getLocation())>entity.getSpeed();
	}
	
	public int[] nextDirection(){
		Location target = null;
		if(stage==-1){
			if(path.isEmpty()){
				if(this.target != null)target = new Location(this.target.getX()+Map.DEFAULT_SQUARESIZE/2, this.target.getY()+Map.DEFAULT_SQUARESIZE/2);
				else target = null;
			}
			else{
				target = new Location(path.get(0).getX()+Map.DEFAULT_SQUARESIZE/2, path.get(0).getY()+Map.DEFAULT_SQUARESIZE/2);
			}
		}else return new int[]{0,0};
		
		if(target == null)return new int[]{0,0};
		
		
		int x = getDirectionX(target);
		int y = getDirectionY(target);
		
		if(x == 0 && y == 0){
			if(!path.isEmpty())path.remove(0);
			else return new int[]{0,0};
			return nextDirection();
		}
		
//		if(path.size()>1){
//			Location nextTarget = new Location(path.get(1).getX()+Map.DEFAULT_SQUARESIZE/2, path.get(1).getY()+Map.DEFAULT_SQUARESIZE/2);
//			System.out.println(x);
//			if(x == 0)x = getDirectionX(nextTarget);
//			if(y == 0)y = getDirectionY(nextTarget);
//		}
		
//		System.out.println(dx+"|"+dy+"->"+x+"|"+y);
		
		return new int[]{x,y};
	}
	
	private int getDirectionY(Location target) {
		int dy = entity.getY()+entity.getHeight()/2-target.getY();		
		if(dy>entity.getHeight()/2)return Direction.UP.getY();
		else if(dy<-entity.getHeight()/2)return Direction.DOWN.getY();
		return 0;
	}

	private int getDirectionX(Location target) {
     	int dx = Map.getMap().getXOver(entity.getX()+entity.getWidth()/2) - Map.getMap().getXOver(target.getX());
		if(dx>entity.getWidth()/2){
			if(dx> (Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))return Direction.RIGHT.getX();
			else  return Direction.LEFT.getX();
		}else if(dx<-entity.getWidth()/2){
			if(dx<-(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))return Direction.LEFT.getX();
			else return Direction.RIGHT.getX();
		}
		return 0;
	}

	public void tick(){		
		if(stage == 0){
			path.clear();
			if(possibleTargets == null){
				stage = -1;
				target = null;
				return;
			}
			stage++;
		}else if(stage == 1){
			getPath(possibleTargets);
			stage = -1;
		}
	}

	private void getPath(Location location) {
//		long time = System.currentTimeMillis();
		int dx = location.getX()-this.entity.getBlockLocation().getX();
		if(dx> Map.getMap().getWidth()/2)dx = dx - Map.getMap().getWidth();
		if(dx<-Map.getMap().getWidth()/2)dx = dx + Map.getMap().getWidth();
		int dy = location.getY()-this.entity.getBlockLocation().getY();
		double m = ((double)dy)/((double)dx);
		
		System.out.println(dx+"|"+dy+"->"+m);
		
		int direcX = 1;
		if(dx<0)direcX=-1;
		int direcY = 1;
		if(dy<0)direcY=-1;
		int y = 0;
		for(int x = 0; Math.abs(x)<=Math.abs(dx) && Math.abs(y)<=Math.abs(dy); x+=direcX){
			int ny = Math.abs((int) Math.round(m*x)-y);
			for(int py = 0; Math.abs(py)<=ny && Math.abs(x)<=Math.abs(dx) && Math.abs(y+py)<=Math.abs(dy); py+=direcY){
				path.add(new Location(Map.getMap().getBlockXOver((entity.getBlockLocation().getX()+x))*Map.DEFAULT_SQUARESIZE, (entity.getBlockLocation().getY()+y+py)*Map.DEFAULT_SQUARESIZE));				
			}
			y = (int) Math.round(m*x);
		}
//		time-=System.currentTimeMillis();
//		long ob = System.currentTimeMillis();
		for(int i = 0; i<path.size(); i++){
			Location loc = path.get(i);
			if(!Map.getMap().entityCanAcces(entity, loc.getX()/Map.DEFAULT_SQUARESIZE, loc.getY()/Map.DEFAULT_SQUARESIZE) && i>0){
				loc = path.get(i-1);
				i = obstacle(i, new Location(loc.getX()/Map.DEFAULT_SQUARESIZE, loc.getY()/Map.DEFAULT_SQUARESIZE));
			}
		}
//		System.out.println("Path: "+this.path);
//		System.out.println("Time for PathFinder: " + time + " + " + (ob-System.currentTimeMillis()));
		path.add(new Location(this.target.getX(), this.target.getY()));
	}

	private int obstacle(int i, Location loc) {
		PathNode[][] nodes = new PathNode[maxRangeObstacle][maxRangeObstacle];
		ArrayList<Location> last = new ArrayList<>();
		last.add(loc);
		nodes[maxRangeObstacle/2][maxRangeObstacle/2] = new PathNode(loc, 0);
		for(int d = 1; d<maxRangeObstacle*2; d++){
			ArrayList<Location> next = new ArrayList<>();
			for(Location node:last){
				for(Direction direc: Direction.values()){
					if(!direc.equals(Direction.NONE)){
						Location newLoc = new Location(node.getX() + direc.getX(), node.getY() + direc.getY());
						int x = newLoc.getX()-loc.getX()+maxRangeObstacle/2;
						int y = newLoc.getY()-loc.getY()+maxRangeObstacle/2;
						if(x >= 0 && y >= 0 && x < nodes.length && y < nodes[0].length && nodes[x][y]==null){
							boolean hasBlock = false;
							for(int ox = -1; ox<=1; ox++){
								for(int oy = -1; oy<=1; oy++){
									Mapdata data = Map.getMap().getMapData(new Location(newLoc.getX()+ox, newLoc.getY()+oy))[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP];
//									System.out.println(ox+"|"+oy+"|"+Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP+" -> "+data);
									if(data != null && !data.canHost(entity.getWidth(), entity.getHeight())){
										hasBlock= true;
										ox = 2;
										oy = 2;
									}
								}
							}
							nodes[x][y] = new PathNode(newLoc, Integer.MAX_VALUE);
							Mapdata data = Map.getMap().getMapData(newLoc)[Map.DEFAULT_BUILDLAYER + Entity.DEFAULT_ENTITY_UP];
							if(data == null || data.canHost(entity.getWidth(), entity.getHeight())){
								nodes[x][y].setDistance(d);
								Location newPixelLoc = new Location(newLoc.getX()*Map.DEFAULT_SQUARESIZE, newLoc.getY()*Map.DEFAULT_SQUARESIZE);
								for(int a = i+1; a<path.size(); a++){
									Location pathNode = path.get(a);
									if(pathNode.isEqual(newPixelLoc) || pathNode.distance_Math(newPixelLoc)<Math.sqrt(2)*Map.DEFAULT_SQUARESIZE-1){
										Mapdata pathData = Map.getMap().getMapData(new Location(pathNode.getX()/Map.DEFAULT_SQUARESIZE, pathNode.getY()/Map.DEFAULT_SQUARESIZE))[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP];
										if(pathData == null || pathData.canHost(entity.getWidth(), entity.getHeight())){
											getPath(i, a-1, loc, nodes, nodes[x][y]);
											return a;
										}
									}
								}
								if(hasBlock)next.add(newLoc);
							}
						}
					}
				}
			}
			last = next;
		}
		return i;
	}

	private void getPath(int start, int end, Location loc, PathNode[][] nodes, PathNode current) {
		for(int i = start; start<end; end--){
			path.remove(i);
		}
		path.remove(end);
		int x = current.getLocation().getX()-loc.getX()+maxRangeObstacle/2;
		int y = current.getLocation().getY()-loc.getY()+maxRangeObstacle/2;
		path.add(end, new Location(current.getLocation().getX()*Map.DEFAULT_SQUARESIZE, current.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
		boolean changed = true;
		while(current != null && current.getDistance()>1 && changed){
			changed = false;
			for(Direction d: Direction.values()){
				if(!d.equals(Direction.NONE) && current.getDistance()>1){
					if(x+d.getX() >= 0 && x+d.getX() < nodes.length && y+d.getY() >= 0 && y+d.getY() < nodes[0].length &&
							nodes[x+d.getX()][y+d.getY()] != null && nodes[x+d.getX()][y+d.getY()].getDistance()<current.getDistance()){
						x+=d.getX();
						y+=d.getY();
						current = nodes[x][y];
						path.add(end, new Location(current.getLocation().getX()*Map.DEFAULT_SQUARESIZE, current.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
						changed = true;
					}
				}
			}
		}
	}

	public void setTarget(Location target){
		this.target = target;
		if(target==null){
			stage = -1;
			path.clear();
		}else{
			this.possibleTargets = correctTarget(target);
			if(possibleTargets == null) setTarget(null);
		}
		
		if(this.target!=null)stage = 0;
		else stage = -1;
	}

	private Location correctTarget(Location target) {
		Location[][] nodes = new Location[maxRange][maxRange];
		Location blockTarget = new Location(target.getX()/Map.DEFAULT_SQUARESIZE, target.getY()/Map.DEFAULT_SQUARESIZE);
		if(!Map.getMap().entityCanAcces(entity, blockTarget.getX(), blockTarget.getY())){
			ArrayList<Location> last = new ArrayList<>();
			last.add(blockTarget);
			for(int d = 0; d<maxRange*2; d++){
				ArrayList<Location> next = new ArrayList<>();
				for(Location node:last){
					for(Direction direc: Direction.values()){
						if(!direc.equals(Direction.NONE)){
							Location newLoc = new Location(node.getX() + direc.getX(), node.getY() + direc.getY());
							int x = newLoc.getX()-blockTarget.getX()+maxRange/2;
							int y = newLoc.getY()-blockTarget.getY()+maxRange/2;
							if(x >= 0 && y >= 0 && x < nodes.length && y < nodes[0].length){
								Location newNode = nodes[x][y];
								if(newNode==null){
									if(Map.getMap().entityCanAcces(entity, Map.getMap().getBlockXOver(newLoc.getX()), newLoc.getY()))return newLoc;
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

	public Location getTarget() {
		return target;
	}

	public void setBlockTarget(Location target) {
		if(target == null)setTarget(null);
		else setTarget(new Location(target.getX()*Map.DEFAULT_SQUARESIZE, target.getY()*Map.DEFAULT_SQUARESIZE));
		
	}

	public boolean isDone() {
		return stage == -1;
	}

	public Location getBlockTarget() {
		return new Location(target.getX()/Map.DEFAULT_SQUARESIZE, target.getY()/Map.DEFAULT_SQUARESIZE);
	}
}
