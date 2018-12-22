package game.pathFinder.system;

public class RequestIDGenerator {
	
	private long lastID = 0l;
	
	public String generateID(PathRequest request){
		
		String id = "";
		id+=String.format("%04d", request.getEntity().getID());
		id+=String.format("%04d", request.getOriginLocation().getX());
		id+=String.format("%04d", request.getOriginLocation().getY());
		id+=String.format("%04d", request.getTarget().getX());
		id+=String.format("%04d", request.getTarget().getY());
		id+=String.format("%01d", request.getState());
		id+=String.format("%05d", lastID);
		
		lastID++;
		return id;
	}

}
