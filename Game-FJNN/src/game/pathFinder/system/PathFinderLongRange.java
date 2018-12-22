package game.pathFinder.system;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Location;
import game.map.Map;

public class PathFinderLongRange {
	
	private static int maxAttemps = 200;
	private static int maxDeep = 20;
	
	private Map map;

	public PathFinderLongRange(Map map) {
		this.map = map;
	}

	public void findPath(PathRequest request) {
		ArrayList<PathLocation> path = getDirectPath(request);
			
		//TODO: path = EvadeObstacles(path, request);
			
		if(request.getState() == PathRequest.STATE_PENDING){
			path.add(new PathLocation(request.getTarget().getX(), request.getTarget().getY(), Map.getMap().getWidth(), 9999));
			for(PathLocation loc : path){
				request.getPath().add(loc.toLocation());
			}
		}
		request.setState(PathRequest.STATE_DONE);
	}

	private ArrayList<PathLocation> getDirectPath(PathRequest request) {
		ArrayList<PathLocation> path = new ArrayList<>();
		
		Location target = request.getTarget();
		Location start = request.getEntity().getBlockLocation();
		
		int[] distance = getDistance(start, target);
		int dx = distance[0];
		int dy = distance[1];
		double m = ((double)dy)/((double)dx);
		
		
		int direcX = 1;
		if(dx<0)direcX=-1;
		int direcY = 1;
		if(dy<0)direcY=-1;
		
		int y = 0;		
		int c = 0;
		
		for(int x = 0; Math.abs(x)<=Math.abs(dx) && Math.abs(y)<=Math.abs(dy); x+=direcX){
			int ny = Math.abs((int) Math.round(m*x)-y);
			for(int py = 0; Math.abs(py)<=ny && Math.abs(x)<=Math.abs(dx) && Math.abs(y+py)<=Math.abs(dy); py+=direcY){
				path.add(new PathLocation(Map.getMap().getBlockXOver((start.getX()+x)), (start.getY()+y+py), this.map.getWidth(), c));	
				c++;
			}
			y = (int) Math.round(m*x);
		}
		
		return path;
	}

	private ArrayList<PathLocation> EvadeObstacles(ArrayList<PathLocation> path, PathRequest request) {
		HashMap<Integer, PathLocation> checkedMapData = new HashMap<>();
		
		for(int i = 0; i<path.size(); i++){
			PathLocation loc = path.get(i);
			checkedMapData.put(loc.getValue(), loc);
			if(!Map.getMap().entityCanAcces(request.getEntity(), loc.getX(), loc.getY())){
				loc.setAccesable(false);
			}
		}

		for(int i = 0; i<path.size(); i++){
			PathLocation loc = path.get(i);
			if(!loc.isAccesable()){
				if(i == 0){
					request.setState(PathRequest.STATE_ERROR);
					System.out.println("Entity can't acces current Location");
					return path;
				}else i = obstacle(i, path, checkedMapData, request);
			}
		}
		
		return path;
	}

	private int obstacle(int index, ArrayList<PathLocation> path, HashMap<Integer, PathLocation> checkedMapData, PathRequest request) {
		PathLocation lastAccesable = path.get(index-1);
		boolean pathFound = false;
		
		ArrayList<PathLocation> lastLocations = new ArrayList<>();
		lastLocations.add(lastAccesable);
		
		while(!pathFound){
			ArrayList<PathLocation> nextLocations = new ArrayList<>();
			
			for(int i = 0; i<lastLocations.size(); i++){
				lastAccesable = lastLocations.get(i);
				
				int[] distance = getDistance(lastAccesable.toLocation(), request.getTarget());		
				int dx = distance[0]>0 ?  1 : -1;
				int dy = distance[1]>0 ?  1 : -1;
				if(Math.abs(distance[1]) < Math.abs(distance[0])) dy = 0;
				else dx = 0;
				
				PathLocation nextLocation = new PathLocation(lastAccesable.getX()+dx, lastAccesable.getY()+dy, this.map.getWidth(), lastAccesable.getDistance()+1);
			}
		}
		
		
		return index;
	}
	
	private int[] getDistance(Location start, Location target){
		int dx = target.getX()-start.getX();
		if(dx> this.map.getWidth()/2)dx = dx - this.map.getWidth();
		if(dx<-this.map.getWidth()/2)dx = dx + this.map.getWidth();
		int dy = target.getY()-start.getY();
		
		return new int[]{dx, dy};
	}

}
