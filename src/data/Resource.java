package data;

import java.awt.Dimension;

import Data.Hitbox;
import Data.Image.SpriteSheet;
import anim.AnimationType;

public abstract class Resource {

	protected int id;
	protected SpriteSheet sprites;
	protected int[] spriteIds;
	protected ResourcePart[] resParts;
	protected AnimationType animType;
	protected Hitbox hitbox;
	
	public Resource(int id, AnimationType animType, SpriteSheet sprites, int[] spriteIDs, ResourcePart[] resParts, Hitbox hitbox) {
		this.id = id;
		this.sprites = sprites;
		this.spriteIds = spriteIDs;
		this.resParts = resParts;		
		this.animType = animType;
		this.hitbox = hitbox;
	}
	
	public int getID(){
		return id;
	}
	
	public SpriteSheet getSprites(){
		return sprites;
	}
	
	public int[] getSpriteIDs(){
		return spriteIds;
	}
	
	public ResourcePart[] getResourceParts(){
		return resParts;
	}
	
	public AnimationType getAnimationType(){
		return this.animType;
	}
	
	public boolean hasAnimation(){
		if(this.animType==null)return false;
		else if(this.animType.equals(AnimationType.NONE)) return false;
		return true;
	}

	public Hitbox getHitbox() {
		return hitbox;
	}

}
