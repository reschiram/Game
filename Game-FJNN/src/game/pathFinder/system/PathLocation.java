package game.pathFinder.system;

import Data.Location;
import game.map.Map;

public class PathLocation{

	private int x;
	private int y;
	
	private int distance;
	
	private int maxX;

	private boolean accesable = true;
	
	public PathLocation(int x, int y, int maxX, int distance) {
		this.x = x;
		this.y = y;
		this.maxX = maxX;
		this.distance = distance;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDistance() {
		return distance;
	}

	public int getValue() {
		return x+maxX*y;
	}

	public Location toLocation() {
		return new Location(x*Map.DEFAULT_SQUARESIZE, y*Map.DEFAULT_SQUARESIZE);
	}

	public boolean isAccesable() {
		return accesable;
	}

	public void setAccesable(boolean accesable) {
		this.accesable = accesable;
	}

	public PathLocation next(PathDirections d, Map map) {
		return new PathLocation(map.getBlockXOver(x+d.getX()), y+d.getY(), maxX, distance+Math.abs(d.getX())+Math.abs(d.getY()));
	}
	
	@Override
	public String toString(){
		return "[PathLocation: ("+x+"|"+y+") maxX:"+maxX+" => "+getValue()+" distance: "+distance+"]";
	}

}
