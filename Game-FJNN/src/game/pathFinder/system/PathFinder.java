package game.pathFinder.system;

import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;

public class PathFinder {
	
	private static int maxAttemps = 200;
	private static int maxDeep = 20;
	
	private Map map;

	public PathFinder(Map map) {
		this.map = map;
	}

	public void findPath(PathRequest request) {		
		System.out.println("==== request:"+request.getId()+"-"+request.getState()+ " ====");
		if(!request.getEntity().getBlockLocation().isEqual(request.getOriginLocation())){
			request.setState(PathRequest.STATE_EXPIRED);
			return;
		}

		HashMap<Integer, PathLocation> path = new HashMap<Integer, PathLocation>();

		System.out.println("find:"+request.getState());
		findPath(request, path, request.getOriginLocation());
		
		System.out.println("found:"+request.getState());		
		if(request.getState() == PathRequest.STATE_DONE){
			loadPath(path, request);
		}else{
			request.setState(PathRequest.STATE_NOPATHFOUND);
		}
		
		System.out.println("end:"+request.getState());
	}

	private void loadPath(HashMap<Integer, PathLocation> path, PathRequest request) {
		int ox = request.getOriginLocation().getX();
		int oy = request.getOriginLocation().getY();		
		int tx = request.getTarget().getX();
		int ty = request.getTarget().getY();
		
		Queue<Location> pathReversed = new Queue<>();
		pathReversed.add(new Location(tx, ty));

		PathLocation lastLocation = getDataFromPath(tx, ty, path);
		if(lastLocation==null){
			request.setState(PathRequest.STATE_ERROR);
			return;
		}
		
		int lastDistance = lastLocation.getDistance();
		
		while(lastLocation.getX()!=ox && lastLocation.getY()!=oy){	
			PathLocation nextLocation = null;
			
			for(Direction d: Direction.values()){
				if(!d.equals(Direction.NONE)){
					int x = map.getBlockXOver(lastLocation.getX()+d.getX());
					int y = lastLocation.getY()+d.getY();
					if(y>=0 && y<map.getHeight()){
						PathLocation loc = getDataFromPath(x, y, path);
						if(loc != null){
							if(loc.getDistance()<=lastDistance){
								lastDistance = loc.getDistance();
								nextLocation = loc;
							}
						}
					}
				}
			}			
			if(nextLocation == null){
				request.setState(PathRequest.STATE_ERROR);
				return;
			}else{
				int dx = Math.abs(pathReversed.get().getX()-nextLocation.getX());
				int dy = Math.abs(pathReversed.get().getY()-nextLocation.getY());
				if((dx!=0 && dy!=0) || (lastLocation.getX()==ox && lastLocation.getY()==oy))pathReversed.add(new Location(nextLocation.getX(), nextLocation.getY()));
				lastLocation = nextLocation;
			}
		}
		
		while(!pathReversed.isEmpty()){
			request.getPath().add(pathReversed.get());
			pathReversed.remove();
		}
	}

	private void findPath(PathRequest request, HashMap<Integer, PathLocation> path, Location origin) {
		findDirect(request, path, origin);
		
		for(int id: path.keySet()){
			PathLocation loc = path.get(id);	
			if(!checkEntireEntityBody(request.getEntity(), loc.getX(), loc.getY())){
				Queue<PathLocation> last = new Queue<>();
				last.add(loc);
				for(int distanceFromOrign = 0; distanceFromOrign<maxDeep; distanceFromOrign++){
					Queue<Location> next = new Queue<>();
					
				}
			}
		}
		
		Queue<Location> last = new Queue<>();
		last.add(origin);
		for(int distanceFromOrign = 0; distanceFromOrign<maxDeep; distanceFromOrign++){
			Queue<Location> next = new Queue<>();
			while(!last.isEmpty()){
				Location start = last.get();
				last.remove();
				if(path.size()<maxAttemps && request.getState()==PathRequest.STATE_PENDING){
					findDirect(request, path, distanceFromOrign, start);					
					if(request.getState() == PathRequest.STATE_DONE)return;
					for(Direction d: Direction.values()){
						if(!d.equals(Direction.NONE)){
							int x = map.getBlockXOver(start.getX()+d.getX());
							int y = start.getY()+d.getY();
							if(y>=0 && y<map.getHeight() && getDataFromPath(x, y, path) == null && checkEntireEntityBody(request.getEntity(), x, y)){
								next.add(new Location(x, y));
								if(request.getState() == PathRequest.STATE_DONE)return;
							}
						}
					}
				}else return;
			}
			last = next;
		}
	}

	private Queue<PathLocation> findDirect(PathRequest request, HashMap<Integer, PathLocation> path, Location origin) {
		Location target = request.getTarget();
		
		Queue<PathLocation> loc
		
		int dx = target.getX()-origin.getX();
		if(dx> Map.getMap().getWidth()/2)dx = dx - Map.getMap().getWidth();
		if(dx<-Map.getMap().getWidth()/2)dx = dx + Map.getMap().getWidth();
		int dy = target.getY()-origin.getY();
		double m = ((double)dy)/((double)dx);
		
		int direcX = 1;
		if(dx<0)direcX=-1;
		int direcY = 1;
		if(dy<0)direcY=-1;
		int y = 0;
		
		int distance = 0;
		
		for(int x = 0; Math.abs(x)<=Math.abs(dx) && Math.abs(y)<=Math.abs(dy); x+=direcX){
			int ny = Math.abs((int) Math.round(m*x)-y);
			for(int py = 0; Math.abs(py)<=ny && Math.abs(x)<=Math.abs(dx) && Math.abs(y+py)<=Math.abs(dy); py+=direcY){
				int bx = Map.getMap().getBlockXOver(origin.getX()+x);
				int by = origin.getY()+y+py;
				System.out.println(bx+"|"+by);
				if(by<0 || by>=map.getHeight())return; 
				
				PathLocation foundData = getDataFromPath(bx, by, path);
				if(foundData!=null)return;
				
				distance++;			
				PathLocation nextLoc = new PathLocation(bx, by, map.getWidth(), distance);
				path.put(nextLoc.getValue(), nextLoc);
				if(target.getX() == bx && target.getY() == by){
					request.setState(PathRequest.STATE_DONE);
					return;
				}
			}
			y = (int) Math.round(m*x);
		}
	}

	private PathLocation getDataFromPath(int x, int y, HashMap<Integer, PathLocation> path) {
		int id = x+y*map.getWidth();
		PathLocation loc = path.get(id);
		return loc;
	}

	private boolean checkEntireEntityBody(Entity entity, int x, int y) {
		return map.entityCanAcces(entity, x, y);
	}

}
