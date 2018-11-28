package game.pathFinder.system;


public class PathLocation{

	private int x;
	private int y;
	
	private int distance;
	
	private int maxX;

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

}
