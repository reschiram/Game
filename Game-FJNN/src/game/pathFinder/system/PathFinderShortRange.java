package game.pathFinder.system;

import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import game.map.Map;

public class PathFinderShortRange {
	
	public static final int maxDistance = 20;
	
	private Map map;

	public PathFinderShortRange(Map map) {
		this.map = map;
	}

	public void findPath(PathRequest request) {
		PathLocation[][] pathData = new PathLocation[maxDistance*2+1][maxDistance*2+1];
		
		Location start = request.getOriginLocation();
		Location end = request.getTarget();
		
		ArrayList<PathLocation> lastLocations = new ArrayList<>();
		lastLocations.add(new PathLocation(start.getX(), start.getY(), this.map.getWidth(), 0));
		pathData[maxDistance][maxDistance] = lastLocations.get(0);
		lastLocations.get(0).setAccesable(true);
		
		boolean found = start.isEqual(end);		
		while(!lastLocations.isEmpty() && !found){
			ArrayList<PathLocation> nextLocations = new ArrayList<>();
			
			for(PathLocation lastLocation : lastLocations){
				for(PathDirections d: PathDirections.values()){					
					PathLocation nextLocation = lastLocation.next(d, map);
					
					int[] pathDataLocation = getDistance(start, nextLocation);
					if(nextLocation.getY() > 0 && nextLocation.getY() < map.getHeight()
							&& Math.abs(pathDataLocation[0]) <= maxDistance && Math.abs(pathDataLocation[1]) <= maxDistance 
							&& pathData[pathDataLocation[0]+maxDistance][pathDataLocation[1]+maxDistance] == null){
						
						pathData[pathDataLocation[0]+maxDistance][pathDataLocation[1]+maxDistance] = nextLocation;
						
						if(nextLocation.getX() == end.getX() && nextLocation.getY() == end.getY()){
							found = true;
							break;
						}else if(Map.getMap().entityCanAcces(request.getEntity(), nextLocation.getX(), nextLocation.getY())){
							nextLocations.add(nextLocation);
							nextLocation.setAccesable(true);
						}else nextLocation.setAccesable(false);
					}
				}
				if(found)break;
			}
			
			lastLocations = nextLocations;
		}
		
		if(!found){
			request.setState(PathRequest.STATE_NOPATHFOUND);
			request.getPath().add(start);
			request.getPath().add(end);
		} else {
			ArrayList<Location> path = new ArrayList<>();
			
			int[] pathDataLocation = getDistance(start, end);			
			PathLocation lastLocation = pathData[pathDataLocation[0]+maxDistance][pathDataLocation[1]+maxDistance];
			path.add(lastLocation.toLocation());
			
			
			
			while(lastLocation.getDistance()>0){
				
				for(Direction d: Direction.values()){					
					PathLocation nextLocation = pathData[pathDataLocation[0]+maxDistance+d.getX()][pathDataLocation[1]+maxDistance+d.getY()];
					if(nextLocation != null && nextLocation.isAccesable() && nextLocation.getDistance()<lastLocation.getDistance())lastLocation = nextLocation;
				}	
				
				int[] nextPathDataLocation = getDistance(start, lastLocation);
				if(nextPathDataLocation[0] == pathDataLocation[0] && nextPathDataLocation[1] == pathDataLocation[1]){
					break;
				}else{
					path.add(lastLocation.toLocation());
					pathDataLocation = nextPathDataLocation;
				}
			}
			
			if(path.get(path.size()-1).isEqual(new Location(start.getX()*Map.DEFAULT_SQUARESIZE, start.getY()*Map.DEFAULT_SQUARESIZE))){
				for(int i = path.size()-1; i >= 0; i--)request.getPath().add(path.get(i));
				request.setState(PathRequest.STATE_DONE);
			}
		}
	}

	private int[] getDistance(Location start, PathLocation target){
		int dx = target.getX()-start.getX();
		if(dx> this.map.getWidth()/2)dx = dx - this.map.getWidth();
		if(dx<-this.map.getWidth()/2)dx = dx + this.map.getWidth();
		int dy = target.getY()-start.getY();
		
		return new int[]{dx, dy};
	}
	
	private int[] getDistance(Location start, Location target){
		int dx = target.getX()-start.getX();
		if(dx> this.map.getWidth()/2)dx = dx - this.map.getWidth();
		if(dx<-this.map.getWidth()/2)dx = dx + this.map.getWidth();
		int dy = target.getY()-start.getY();
		
		return new int[]{dx, dy};
	}

}
