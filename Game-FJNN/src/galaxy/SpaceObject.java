package galaxy;

import Data.Image.SpriteSheet;

public class SpaceObject {
	
	protected String name;
	protected int diameter;
	protected int distanceToCenter;
	protected SpriteSheet spriteSheet;
	protected int spriteState;
	
	public SpaceObject(String name, int diameter, int distanceToCenter, SpriteSheet spriteSheet, int spriteState){
		this.name = name;
		this.diameter = diameter;
		this.distanceToCenter = distanceToCenter;
		this.spriteSheet = spriteSheet;
		this.spriteState = spriteState;
	}	
	
	public String getName() {
		return name;
	}

	public int getDiameter() {
		return diameter;
	}

	public int getDistanceToCenter() {
		return distanceToCenter;
	}

	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public int getSpriteState() {
		return 0;
	}

}
