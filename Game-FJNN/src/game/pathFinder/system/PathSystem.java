package game.pathFinder.system;

import java.util.HashMap;

import Data.Location;
import Data.Queue;
import game.entity.Entity;
import game.map.Map;
import game.tick.TickManager;

public class PathSystem {
	
	private Queue<PathRequest> requests = new Queue<>();
	private RequestIDGenerator requestIDGenerator = new RequestIDGenerator();
	private PathFinder pathFinder;
	
	private HashMap<String, PathRequest> paths = new HashMap<>();
	private Map map;
	
	public PathSystem(Map map){
		System.out.println("PathSystem loaded");
		
		this.map = map;
		
		this.pathFinder = new PathFinder(map);
		
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
			pathFinder.findPath(request);
			paths.put(request.getId(), request);
		}
	}
	
	public PathRequest addPathRequest(Entity entity, Location pixelTarget){
		Location blockTarget = new Location(pixelTarget.getX()/Map.DEFAULT_SQUARESIZE, pixelTarget.getY()/Map.DEFAULT_SQUARESIZE);
		Location offset = new Location(pixelTarget.getX()-blockTarget.getX()*Map.DEFAULT_SQUARESIZE, pixelTarget.getY()-blockTarget.getY()*Map.DEFAULT_SQUARESIZE);
		PathRequest request = new PathRequest(entity, entity.getBlockLocation(), blockTarget , map, offset);
		System.out.println(blockTarget+"->"+pixelTarget+"->"+request.getTarget());
		String id = requestIDGenerator.generateID(request);
		request.setId(id);
		this.requests.add(request);
		return request;
	}
}