package game.pathFinder.system;

public enum PathDirections {
	
	UP	 ( 0,  1),
	DOWN ( 0, -1),
	RIGHT( 1,  0),
	LEFT (-1,  0);
	
	private int x;
	private int y;

	private PathDirections(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
