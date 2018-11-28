package game.pathFinder;

import Data.Location;

public class PathNode{
	
	private Location location;
	private int distance, lastDetectionX, lastDetectionY;
	
	public PathNode(Location location, int distance){
		this.location = location;
		this.distance = distance;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public int getDistance(){
		return distance;
	}

	public int getLastDetectionX() {
		return lastDetectionX;
	}

	public void setLastDetectionX(int lastDetectionX) {
		this.lastDetectionX = lastDetectionX;
	}

	public int getLastDetectionY() {
		return lastDetectionY;
	}

	public void setLastDetectionY(int lastDetectionY) {
		this.lastDetectionY = lastDetectionY;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
