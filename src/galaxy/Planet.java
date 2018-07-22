package galaxy;

import Data.Image.SpriteSheet;
import game.map.Map;

public class Planet extends SpaceObject{
	
	protected Map map;
	
	public Planet(String name, int diameter, int distanceToCenter, SpriteSheet spriteSheet, int spriteState, Map map) {
		super(name, diameter, distanceToCenter, spriteSheet, spriteState);
		this.map = map;
	}	
	public Map getMap(){
		return map;
	}
}
