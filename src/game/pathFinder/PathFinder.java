package game.pathFinder;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;

public class PathFinder {
	
	private static int maxRange = 5;
	
	private Entity entity;
	private Location target;

	private int stage = -1;
	private Queue<Location> possibleTargets = new Queue<>();
	
	private ArrayList<Location> path = new ArrayList<>();
	
	public PathFinder(Entity entity, int maxPath){
		this.entity = entity;
	}
	
	public boolean hasTarget(){
		return target!=null && target.distance_Math(entity.getLocation())>entity.getSpeed();
	}
	
	public int[] nextDirection(){
		Location target = null;
		if(stage==-1){
			if(path.isEmpty()) target = this.target;
			else{
				target = path.get(0);
			}
		}else return new int[]{0,0};
		
		System.out.println(target);
		
		int x = 0;
		int y = 0;
		
     	int dx = Map.getMap().getXOver(entity.getX()+entity.getWidth()/2) - Map.getMap().getXOver(target.getX()*Map.DEFAULT_SQUARESIZE);
		System.out.println((entity.getX()+entity.getWidth()/2)+" - "+target.getX()*Map.DEFAULT_SQUARESIZE+" -> "+dx);
		if(dx>entity.getWidth()/2){
			if(dx> (Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))x=Direction.RIGHT.getX();
			else x = Direction.LEFT.getX();
		}else if(dx<-entity.getWidth()/2){
			if(dx<-(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))x=Direction.LEFT.getX();
			else x = Direction.RIGHT.getX();
		}

		int dy = entity.getY()+entity.getHeight()/2-target.getY()*Map.DEFAULT_SQUARESIZE;
		
		if(dy>entity.getHeight()/2)y = Direction.UP.getY();
		else if(dy<-entity.getHeight()/2)y = Direction.DOWN.getY();
		
		if(x == 0 && y == 0 && !path.isEmpty()){
			path.remove(0);
			return nextDirection();
		}
		
		return new int[]{x,y};
	}
	
	public void tick(){		
		if(stage == 0){
			path.clear();
			if(possibleTargets.isEmpty()){
				stage = -1;
				target = null;
				return;
			}
			stage++;
		}else if(stage == 1){
			getPath(possibleTargets.get());
			stage = -1;
		}
	}

	private void getPath(Location location) {
		int dx = location.getX()-this.entity.getX()/Map.DEFAULT_SQUARESIZE;
		if(dx> Map.getMap().getWidth()/2)dx = Map.getMap().getWidth() - dx;
		if(dx<-Map.getMap().getWidth()/2)dx = Map.getMap().getWidth() + dx;
		int dy = location.getY()-this.entity.getY()/Map.DEFAULT_SQUARESIZE;
		double m = ((double)dy)/((double)dx);
		
		int direcX = 1;
		if(dx<0)direcX=-1;
		int direcY = 1;
		if(dy<0)direcY=-1;
		int y = 0;
		for(int x = 0; Math.abs(x)<=Math.abs(dx) && Math.abs(y)<=Math.abs(dy); x+=direcX){
			int ny = Math.abs((int) Math.round(m*x)-y);
			for(int py = 0; Math.abs(py)<=ny && Math.abs(x)<=Math.abs(dx) && Math.abs(y+py)<=Math.abs(dy); py+=direcY){
				path.add(new Location(entity.getBlockLocation().getX()+x, entity.getBlockLocation().getY()+y+py));				
			}
			y = (int) Math.round(m*x);
		}
		System.out.println(location + " -> " +entity.getBlockLocation());
		for(Location loc: path){
			System.out.print(loc + " <-> ");
		}
		System.out.println();
	}

	public void setTarget(Location target){
		System.out.println("target -> "+target);
		this.target = target;
		if(target==null){
			stage = -1;
			path.clear();
		}else{
			this.possibleTargets = correctTarget(target);
			if(possibleTargets == null) this.target = null;
		}
		
		if(this.target!=null)stage = 0;
		else stage = -1;
	}

	private Queue<Location> correctTarget(Location target) {
		Queue<Location> possibleTargets = new Queue<>();
		Location end = new Location(target.getX()/Map.DEFAULT_SQUARESIZE, target.getY()/Map.DEFAULT_SQUARESIZE);
		if(!Map.getMap().entityCanAcces(entity, end.getX(), end.getY())){
			HashMap<Integer, Integer> locations = new HashMap<>();
			int maxRange = PathFinder.maxRange;
			int max = maxRange-1;
			for(int x = 0; x<maxRange; x++){
				for(int y = 0; y+x<maxRange; y++){
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()/Map.DEFAULT_SQUARESIZE+x), target.getY()/Map.DEFAULT_SQUARESIZE+y)){
						locations.put((max+x)+(max+y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()/Map.DEFAULT_SQUARESIZE-x), target.getY()/Map.DEFAULT_SQUARESIZE+y)){
						locations.put((max-x)+(max+y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()/Map.DEFAULT_SQUARESIZE-x), target.getY()/Map.DEFAULT_SQUARESIZE-y)){
						locations.put((max-x)+(max-y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()/Map.DEFAULT_SQUARESIZE+x), target.getY()/Map.DEFAULT_SQUARESIZE-y)){
						locations.put((max+x)+(max-y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
				}
			}
			if(locations.isEmpty()) return possibleTargets;
			else{
				int min = -1;
				for(Integer loc : locations.values()){
					int current = locations.get(loc);
					if(min==-1 || min > current){
						possibleTargets = new Queue<>();
						min = current;
					}
					if(min == current){
						int y = loc/((max+1)*2);
						possibleTargets.add(new Location(Map.getMap().getBlockXOver(end.getX()+loc-y-max), end.getY()+y-max));
					}
				}
			}
		}
		possibleTargets.add(end);
		return possibleTargets;
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
