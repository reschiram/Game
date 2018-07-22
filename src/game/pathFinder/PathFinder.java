package game.pathFinder;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;
import game.tick.TickManager;

public class PathFinder {
	
	private static int maxRange = 5;
	
	private Entity entity;
	private Location target;

	private int stage = -1;
	private Queue<Location> possibleTargets = new Queue<>();
	private PathNode[][] nodes;
	private ArrayList<PathNode> lastNodes = new ArrayList<>();
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
		}else if(stage > 0){
			fill(possibleTargets.get());
		}else if(stage == -2){
			lastNodes.clear();
			getPath();
			stage = -1;
		}
	}

	private void getPath() {
		int sx = this.entity.getX()/Map.DEFAULT_SQUARESIZE-stage;
		int sy = this.entity.getY()/Map.DEFAULT_SQUARESIZE-stage;
		
		int tx = target.getX()-sx;
		int ty = target.getY()-sy;
		
		for(PathNode node = this.nodes[tx][ty]; node!=null && node.getDistance()>= 0; ){
			int distance = node.getDistance();
			
				 if(tx-1 >= 0 					 && this.nodes[tx-1][ty  ] != null && this.nodes[tx-1][ty  ].getDistance() < distance) tx-=1;
			else if(tx+1 <  this.nodes.length 	 && this.nodes[tx-1][ty  ] != null && this.nodes[tx-1][ty  ].getDistance() < distance) tx+=1;
			else if(ty-1 >= 0 					 && this.nodes[tx  ][ty-1] != null && this.nodes[tx  ][ty-1].getDistance() < distance) ty-=1;
			else if(ty+1 <  this.nodes[0].length && this.nodes[tx  ][ty+1] != null && this.nodes[tx  ][ty+1].getDistance() < distance) ty+=1;
				 
			path.add(new Location(Map.getMap().getBlockXOver(node.getLocation().getX())*Map.DEFAULT_SQUARESIZE, node.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
		}
	}

	private void fill(Location target) {
		int sx = this.entity.getX()/Map.DEFAULT_SQUARESIZE-stage;
		int sy = this.entity.getY()/Map.DEFAULT_SQUARESIZE-stage;
		
		int maxX = Math.abs(target.getX() - this.entity.getX()/Map.DEFAULT_SQUARESIZE)+stage*2;
		if(maxX > maxPath)maxX-=Map.getMap().getWidth();
		
		if(lastNodes.isEmpty()){
			this.nodes = new PathNode[maxX][Math.abs(target.getY() - this.entity.getY()/Map.DEFAULT_SQUARESIZE+stage*2)];

			lastNodes.add(new PathNode(new Location(sx, sy), 0));
			nodes[0][0] = lastNodes.get(0);
		}
		
		if(maxX<0 || maxX + Math.abs(target.getY() - this.entity.getY()/Map.DEFAULT_SQUARESIZE+stage*2)> maxPath){
			stage  = -1;
			possibleTargets = new Queue<>();
			target = null;
			return;
		}

		long start = System.currentTimeMillis();
		
		for(int a = lastNodes.get(0).getDistance(); a<maxPath; a++){
			ArrayList<PathNode> next = new ArrayList<>();
			for(int i = 0; i<lastNodes.size(); i++){
				PathNode node = lastNodes.get(i);
				if(new Location(Map.getMap().getBlockXOver(node.getLocation().getX()), node.getLocation().getY()).isEqual(target)){
					stage = -2;
					return;
				}
				
				if(node.getLastDetectionX() != -1)add(node, next,  1,  0, sx, sy);
				if(node.getLastDetectionX() !=  1)add(node, next, -1,  0, sx, sy);
				if(node.getLastDetectionY() != -1)add(node, next,  0,  1, sx, sy);
				if(node.getLastDetectionY() !=  1)add(node, next,  0, -1, sx, sy);
				
			}
			lastNodes = next;
			if(System.currentTimeMillis()-start>TickManager.getTickDuration())return;
		}
		
		lastNodes.clear();
		stage++;
	}

	private void add(PathNode lastNode, ArrayList<PathNode> list, int x, int y, int sx, int sy) {
		Location loc = new Location(lastNode.getLocation().getX() + x, lastNode.getLocation().getY() +y);	
		
		PathNode newNode = nodes[loc.getX()-sx][loc.getY()-sy];
		if(newNode == null){
			newNode = new PathNode(loc, lastNode.getDistance()+1);
			nodes[loc.getX()-sx][loc.getY()-sy] = newNode;
		}else if(newNode.getDistance()<lastNode.getDistance()+1) return;
		
		if(x!=0)newNode.setLastDetectionX(x);
		if(y!=0)newNode.setLastDetectionY(y);
		list.add(newNode);
	}

//
//	private void nextNodes(SortArrayList<PathNode> list, int startX, int startY, int x, int y, int lastdistance, Location target) {
//		if(x == target.getX() && y == target.getY()){
//			ArrayList<PathNode> nodes = new ArrayList<>();
//			nodes.add(new PathNode(x+y*target.getX(), lastdistance+1, target.getX()+target.getX()*target.getX()));
//			return nodes;
//		}
//		list.add();
//		ArrayList<PathNode> nodes = nextNodes(list, startX, startY, x, y, lastdistance, target)
//	}

//	private void obstacle() {
//		for(int i = 0; i<path.size(); i++){
//			PathNode loc = path.get(i);
//			if(!Map.getMap().entityCanAcces(this.entity, loc.getLocation().getX(), loc.getLocation().getY())){
//				ArrayList<PathNode> previousPathL = new ArrayList<>();
//				ArrayList<PathNode> previousPathR = new ArrayList<>();
//				for(int a = 0; a<i; a++){
//					previousPathL.add(path.get(i));
//					previousPathR.add(path.get(i));
//				}
//				
//				PathNode previous;
//				if(i-1<0)previous = new PathNode(this.entity.getTopLeftBlockLocation(), 0);
//				else previous = path.get(i-1);
//				
//				if(loc.getLocation().getX()-previous.getLocation().getX() == 0){
//					previousPathL = nextPathNode(previousPathL, -1,  0);
//					previousPathR = nextPathNode(previousPathR,  1,  0);
//				}else{
//					previousPathL = nextPathNode(previousPathL,  0, -1);
//					previousPathR = nextPathNode(previousPathR,  0,  1);					
//				}
//			}
//		}
//	}
//
//	private ArrayList<PathNode> nextPathNode(ArrayList<PathNode> path, int x, int y) {
//		PathNode current = path.get(path.size()-1);
//		Location lastBlockDirection = path.get(path.size()-2).getLastDection();
//		
//		Location NextLoc;
//		if(lastBlockDirection==null){
//			NextLoc = new Location(current.getLocation().getX()+x, current.getLocation().getY()+y);
//		}else{
//			
//		} 
//		
//		return path;
//	}

//	private void directPath(Location target) {
//		Location start 	= new Location(this.entity.getX()/Map.DEFAULT_SQUARESIZE, this.entity.getY()/Map.DEFAULT_SQUARESIZE);
//		Location end 	= new Location(		target.getX()/Map.DEFAULT_SQUARESIZE, 	   target.getY()/Map.DEFAULT_SQUARESIZE);
//		
//		double m = ((double)(end.getY()-start.getY())) / ((double)(end.getX()-start.getX()));
//		
//		int ax = end.getX()-start.getX();
//		int ay = end.getY()-start.getY();
//		
//		double y = 0;
//		for(int x = 0; x<=Math.abs(end.getX()-start.getX()); x++){
//			for(int a = (int)y;a <= Math.abs((int)(y+m)); a++){
//				int dx = x;
//				if(ax<0)dx*=-1;
//				int dy = a;
//				if(ay<0)dy*=-1;
//				PathNode node = new PathNode(new Location(Map.getMap().getBlockXOver(start.getX()+dx), (int) (start.getY()+dy)), x+a);
//				if(node.getLocation().isEqual(end)){
//					 a = Math.abs((int)(y+m)+1);
//					 x = Math.abs(end.getX()-start.getX()+1);
//				}else if(node.getLocation().isEqual(start)){
//					 a = Math.abs((int)(y+m)+1);					
//				}else{
//					this.path.add(node);
//				}
//			}
//			y+=m;
//		}		
//		this.path.add(new PathNode(end, Math.abs(end.getX()-start.getX())+Math.abs(end.getY()-start.getY())));
//	}

	public void setTarget(Location target){
		this.target = target;
		this.possibleTargets = correctTarget(target);
		if(possibleTargets == null) this.target = null;
			
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
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()+x), target.getY()+y)){
						locations.put((max+x)+(max+y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()-x), target.getY()+y)){
						locations.put((max-x)+(max+y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()-x), target.getY()-y)){
						locations.put((max-x)+(max-y)*(max+1)*2, x+y);
						maxRange = x+y+1;
					}
					if(Map.getMap().entityCanAcces(this.entity, Map.getMap().getBlockXOver(target.getX()+x), target.getY()-y)){
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
		if(stage!=-1) return null;
		return target;
	}
}
