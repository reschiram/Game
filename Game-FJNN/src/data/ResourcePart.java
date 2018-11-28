package data;

import Data.Location;
import sprites.Sprites;

public class ResourcePart {

	private Location loc;
	private Sprites sprites;
	private int[] spriteIDs;
	private int id;
	
	public ResourcePart(Location loc, Sprites sprites, int id, int... spriteIDs){
		this.loc = loc;
		this.sprites = sprites;
		this.spriteIDs = spriteIDs;
		this.id = id;
	}
	
	public Location getLocation() {
		return loc;
	}

	public Sprites getSprites() {
		return sprites;
	}

	public int[] getSpriteIDs() {
		return spriteIDs;
	}

	public int getID() {
		return id;
	}

}
