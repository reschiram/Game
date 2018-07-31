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
	private PathNode[][] nodes;
	private int maxPath;
	
	private ArrayList<Location> path = new ArrayList<>();
	
	public PathFinder(Entity entity, int maxPath){
		this.entity = entity;
		this.maxPath = maxPath;
	}
	
	public boolean hasTarget(){
		return target!=null && target.distance_Math(entity.getLocation())>entity.getSpeed();
	}
	
	public int[] nextDirection(){
		Location target = null;
		if(stage==-1){
			if(path.isEmpty()) target = this.target;
			else{
				target = path.get(path.size()-1);
			}
		}else return new int[]{0,0};
		
		System.out.println(target);
		
		int x = 0;
		int y = 0;
		
     	int dx = Map.getMap().getXOver(entity.getX()+entity.getWidth()/2) - Map.getMap().getXOver(target.getX());
//		System.out.println((entity.getX()+entity.getWidth()/2)+" - "+target.getX()+" -> "+dx);
		if(dx>entity.getWidth()/2){
			if(dx> (Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))x=Direction.RIGHT.getX();
			else x = Direction.LEFT.getX();
		}else if(dx<-entity.getWidth()/2){
			if(dx<-(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE-1920))x=Direction.LEFT.getX();
			else x = Direction.RIGHT.getX();
		}

		int dy = entity.getY()+entity.getHeight()/2-target.getY();
		
		if(dy>entity.getHeight()/2)y = Direction.UP.getY();
		else if(dy<-entity.getHeight()/2)y = Direction.DOWN.getY();
		
		if(x == 0 && y == 0 && !path.isEmpty()){
			path.remove(path.size()-1);
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
			fill(possibleTargets.get());
			stage++;
		}else if(stage == 2){
			getPath(possibleTargets.get());
			stage = -1;
		}
	}

	private void getPath(Location target) {		
		
		int maxDistance = target.distance_Math(this.entity.getBlockLocation());

		Direction[] sequence = Direction.values();
		for(PathNode node = getFromNodes(target.getX(), target.getY(), maxDistance); node!=null && node.getDistance()> 1;){
			path.add(new Location(Map.getMap().getBlockXOver(node.getLocation().getX())*Map.DEFAULT_SQUARESIZE, node.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
			for(int i = 0; i<sequence.length; i++){
				PathNode newNode = getFromNodes(node.getLocation().getX()+sequence[i].getX(), node.getLocation().getY()+sequence[i].getY(), maxDistance);
				if(newNode !=null && newNode.getDistance()!=-1 && newNode.getDistance()<node.getDistance()){
					node = newNode;
					i = sequence.length;
				}
			}
		}
	}

	private void fill(Location target) {
		
		int w = maxPath/2-2+maxPath;
		int h = maxPath/2-2+maxPath;
		
		int maxDistance = target.distance_Math(this.entity.getBlockLocation());
		
		nodes = new PathNode[w][h];
		ArrayList<PathNode> lastNodes = new ArrayList<>();
		lastNodes.add(new PathNode(this.entity.getBlockLocation(), 0));
		addToNodes(lastNodes.get(0), maxDistance);
		
		for(int a = lastNodes.get(0).getDistance(); a<maxPath; a++){
			ArrayList<PathNode> next = new ArrayList<>();
			for(int i = 0; i<lastNodes.size(); i++){
				PathNode node = lastNodes.get(i);
				if(new Location(Map.getMap().getBlockXOver(node.getLocation().getX()), node.getLocation().getY()).isEqual(target)){				
					return;
				}
				
				if(node.getLastDetectionX() != -1)add(node, next,  1,  0, maxDistance);
				if(node.getLastDetectionX() !=  1)add(node, next, -1,  0, maxDistance);
				if(node.getLastDetectionY() != -1)add(node, next,  0,  1, maxDistance);
				if(node.getLastDetectionY() !=  1)add(node, next,  0, -1, maxDistance);
				
			}
			lastNodes = next;
		}
		
		lastNodes.clear();
		
	}
	
	private void addToNodes(PathNode node, int maxDistance){
		int x = node.getLocation().getX()-this.entity.getBlockLocation().getX() + maxPath/2-2;
		int y = node.getLocation().getY()-this.entity.getBlockLocation().getY() + maxPath/2-2;
		if(x>0 && x<nodes.length && y>0 && y<nodes[0].length){
			nodes[x][y] = node;
		}else if(this.entity.getBlockLocation().distance_Math(node.getLocation())<=maxDistance){
			if(x<0)x += Map.getMap().getWidth();
			else if(x>nodes.length) x-= Map.getMap().getWidth();
			if(x>0 && x<nodes.length && y>0 && y<nodes[0].length){
				nodes[x][y] = node;
			}
		}
	}
	
	private PathNode getFromNodes(int x, int y, int maxDistance){
		int dx = x-this.entity.getBlockLocation().getX() + maxPath/2-2;
		int dy = y-this.entity.getBlockLocation().getY() + maxPath/2-2;		
		if(dx>0 && dx<nodes.length && dy>0 && dy<nodes[0].length){
			return nodes[dx][dy];
		}else if(this.entity.getBlockLocation().distance_Math(new Location(x, y))<=maxDistance){
			if(dx<0)dx += Map.getMap().getWidth();
			else if(dx>nodes.length)dx-= Map.getMap().getWidth();
			if(dx>0 && dx<nodes.length && dy>0 && dy<nodes[0].length){
				return nodes[dx][dy];
			}
		}
		return new PathNode(new Location(-1, -1), -1);
	}

	private void add(PathNode lastNode, ArrayList<PathNode> list, int x, int y, int maxDistance) {
		Location loc = new Location(lastNode.getLocation().getX() + x, lastNode.getLocation().getY() +y);	
		
		PathNode newNode = getFromNodes(loc.getX(), loc.getY(), maxDistance);
		if(newNode == null){
			newNode = new PathNode(loc, lastNode.getDistance()+1);
			addToNodes(newNode, maxDistance);
		}else{
			if(newNode.getDistance()==-1)return;
			else if(newNode.getDistance()<lastNode.getDistance()+1) return;
		}
		
		if(x!=0)newNode.setLastDetectionX(x);
		if(y!=0)newNode.setLastDetectionY(y);
		list.add(newNode);
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
