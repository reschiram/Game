package data;

import Data.Hitbox;
import Data.Image.SpriteSheet;
import anim.AnimationType;

public abstract class Resource {
	
	public static final String noResourceID = "0000";

	protected int id;
	protected SpriteSheet sprites;
	protected int[] spriteIds;
	protected ResourcePart[] resParts;
	protected AnimationType animType;
	protected Hitbox hitbox;
	protected boolean opaque;
	protected int hp;
	
	public Resource(int id, AnimationType animType, SpriteSheet sprites, int[] spriteIDs, ResourcePart[] resParts, Hitbox hitbox, boolean opaque, int hp) {
		this.id = id;
		this.sprites = sprites;
		this.spriteIds = spriteIDs;
		this.resParts = resParts;		
		this.animType = animType;
		this.hitbox = hitbox;
		this.opaque = opaque;
		this.hp = hp;
	}
	
	public boolean isOpaque(){
		return opaque;
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

	public int getHP() {
		return hp;
	}

}
