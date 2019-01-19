package game.pathFinder.system;

import java.util.HashMap;

import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;
import tick.TickManager;

public class PathSystem {
	
	private Queue<PathRequest> requests = new Queue<>();
	private RequestIDGenerator requestIDGenerator = new RequestIDGenerator();
	private PathFinderShortRange pathFinderSR;
	private PathFinderLongRange  pathFinderLR;
	
	private HashMap<String, PathRequest> paths = new HashMap<>();
	private Map map;
	
	public PathSystem(Map map){
		System.out.println("PathSystem loaded");
		
		this.map = map;
		
		this.pathFinderSR = new PathFinderShortRange(map);
		this.pathFinderLR = new PathFinderLongRange (map);
		
		startThread();
	}

	private void startThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {	
				while(true){
					pathTick();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void pathTick() {
		long startTick = TickManager.getCurrentTick();
		while (!requests.isEmpty() && startTick == TickManager.getCurrentTick()) {
			PathRequest request = requests.get();
			requests.remove();
			
			if(request.getState() == PathRequest.STATE_PENDING){			
				try{
					if(Math.abs(request.getOriginLocation().getX()-request.getTarget().getX()) <= PathFinderShortRange.maxDistance 
							&& Math.abs(request.getOriginLocation().getY()-request.getTarget().getY()) <= PathFinderShortRange.maxDistance){
						pathFinderSR.findPath(request);
					}else{
						pathFinderLR.findPath(request);
					}
					if(request.getState() == PathRequest.STATE_PENDING){
						System.out.println("Error while handling request: "+request.getId()+" with target: "+request.getTarget()+" from origin location: "+request.getOriginLocation());
						request.setState(PathRequest.STATE_ERROR);
					}
				}catch (Exception e) {
					e.printStackTrace();
					request.setState(PathRequest.STATE_ERROR);
				}
				
				paths.put(request.getId(), request);
			}
		}
	}
	
	public PathRequest addPathRequest(Entity entity, Location pixelTarget){
		Location blockTarget = new Location(pixelTarget.getX()/Map.DEFAULT_SQUARESIZE, pixelTarget.getY()/Map.DEFAULT_SQUARESIZE);
		Location offset = new Location(pixelTarget.getX()-blockTarget.getX()*Map.DEFAULT_SQUARESIZE, pixelTarget.getY()-blockTarget.getY()*Map.DEFAULT_SQUARESIZE);
		PathRequest request = new PathRequest(entity, entity.getBlockLocation(), blockTarget , map, offset);
		String id = requestIDGenerator.generateID(request);
		request.setId(id);
		this.requests.add(request);
		return request;
	}
}
