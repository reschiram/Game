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

	public static PathDirections getMainPathDirection(int[] distance) {
		if(Math.abs(distance[0]) >= Math.abs(distance[1])){
			if(distance[0] > 0)return RIGHT;
			else return LEFT;
		}else{
			if(distance[1] > 0)return DOWN;
			else return UP;
		}
	}

}
