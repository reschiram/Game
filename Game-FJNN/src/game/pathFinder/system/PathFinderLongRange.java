package game.pathFinder.system;

import java.util.ArrayList;

import Data.Location;
import game.map.Map;

public class PathFinderLongRange {
	
	private static int maxDeep = 20;
	private static int maxObstacles = 100;
	
	private Map map;

	public PathFinderLongRange(Map map) {
		this.map = map;
	}

	public void findPath(PathRequest request) {
		ArrayList<Location> path = new ArrayList<>();
		
		Location target = request.getTarget();
		Location start = request.getEntity().getBlockLocation();
		
		path.add(start);
		Location currentEnd = start;		
		
		for(int count = 0; count < maxObstacles && currentEnd.distance_Math(target) > 1 && request.getState() == PathRequest.STATE_PENDING; count++){
			path.addAll(getDirectPath(request, currentEnd, target));
			Location nextEnd = path.get(path.size()-1);
			if(nextEnd.distance_Math(target) > 1){
				path.addAll(evadeObstacle(request, nextEnd, target));	
				nextEnd = path.get(path.size()-1);
			}
			currentEnd = nextEnd;
		}
		
			
		if(request.getState() == PathRequest.STATE_PENDING){
			path.add(new Location(request.getTarget().getX(), request.getTarget().getY()));
			for(Location loc : path){
				request.getPath().add(new Location(loc.getX()*Map.DEFAULT_SQUARESIZE, loc.getY()*Map.DEFAULT_SQUARESIZE));
			}
			request.setState(PathRequest.STATE_DONE);
		}
	}

	private ArrayList<Location> evadeObstacle(PathRequest request, Location start, Location target) {
		ArrayList<Location> path1 = new ArrayList<>();
		ArrayList<Location> path2 = new ArrayList<>();
		path1.add(start);
		path2.add(start);		
		
		for(int counter = 0; counter < maxDeep; counter++){
			//Path 1 ===>
			Location lastLocation = path1.get(path1.size()-1);
			PathDirections direction = PathDirections.getMainPathDirection(getDistance(lastLocation, target));
			Location nextLocation = new Location(lastLocation.getX()+direction.getX(), lastLocation.getY()+direction.getY());
			if(this.map.entityCanExist(request.getEntity(), nextLocation.getX(), nextLocation.getY())) return path1;
			else path1.add(new Location(lastLocation.getX() + (direction.getX() == 0 ? 1 : 0), lastLocation.getY() + (direction.getY() == 0 ? 1 : 0)));
			
			//Path 2 ===>
			lastLocation = path2.get(path2.size()-1);
			direction = PathDirections.getMainPathDirection(getDistance(lastLocation, target));
			nextLocation = new Location(lastLocation.getX()+direction.getX(), lastLocation.getY()+direction.getY());
			if(this.map.entityCanExist(request.getEntity(), nextLocation.getX(), nextLocation.getY())) return path2;
			else path2.add(new Location(lastLocation.getX() + (direction.getX() == 0 ? -1 : 0), lastLocation.getY() + (direction.getY() == 0 ? -1 : 0)));
		}
		
		request.setState(PathRequest.STATE_ERROR);
		return new ArrayList<>();
	}

	private ArrayList<Location> getDirectPath(PathRequest request, Location start, Location target) {
		ArrayList<Location> path = new ArrayList<>();
		
		int[] distance = getDistance(start, target);
		int dx = distance[0];
		int dy = distance[1];
		double m = ((double)dy)/((double)dx);
		
		int direcX = 1;
		if(dx<0)direcX=-1;
		int direcY = 1;
		if(dy<0)direcY=-1;
		
		int y = 0;		
		
		for(int x = 0; Math.abs(x)<=Math.abs(dx) && Math.abs(y)<=Math.abs(dy); x+=direcX){
			int ny = Math.abs((int) Math.round(m*x)-y);
			for(int py = 0; Math.abs(py)<=ny && Math.abs(x)<=Math.abs(dx) && Math.abs(y+py)<=Math.abs(dy); py+=direcY){				
				Location nextLocation = new Location(Map.getMap().getBlockXOver((start.getX()+x)), (start.getY()+y+py));					
				if(!this.map.entityCanAcces(request.getEntity(), nextLocation.getX(), nextLocation.getY())) return path;
				path.add(nextLocation);
			}
			y = (int) Math.round(m*x);
		}
		
		return path;
	}

	private int[] getDistance(Location start, Location target){
		int dx = target.getX()-start.getX();
		if(dx> this.map.getWidth()/2)dx = dx - this.map.getWidth();
		if(dx<-this.map.getWidth()/2)dx = dx + this.map.getWidth();
		int dy = target.getY()-start.getY();
		
		return new int[]{dx, dy};
	}

}
