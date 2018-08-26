package game.gridData;

import java.awt.Dimension;

import Data.Hitbox;
import Data.Location;
import Data.Animation.Animation;
import Data.Animation.AnimationManager;
import Data.Image.Image;
import Engine.Engine;
import data.LightSpriteSheet;
import data.Resource;
import game.map.Map;
import sprites.Sprites;

public abstract class GridData{	

	protected static Dimension blockDimension = new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE);
	
	protected Hitbox hitbox;
	protected Location location;
	public int layer;		
	protected Image image;
	protected Animation anim;
	protected Resource res;
	protected int spriteState = 0;
	protected int hp;
	protected Image damageLevel;
	
	protected boolean created = false;
	
	public GridData(Resource resource, int layer, Location loc) {
		location = loc;
		this.hitbox = new Hitbox(new Location(loc.getX()*Map.DEFAULT_SQUARESIZE + resource.getHitbox().getX(), loc.getY()*Map.DEFAULT_SQUARESIZE + resource.getHitbox().getY()), resource.getHitbox().getDimension());
		this.layer = layer;
		this.res = resource;
		this.image = new Image(new Location(loc.x*Map.DEFAULT_SQUARESIZE, loc.y*Map.DEFAULT_SQUARESIZE), blockDimension, "", res.getSprites(), null);
		this.image.setSpriteState(res.getSpriteIDs()[spriteState]);
		if(res.hasAnimation()){
			this.anim = res.getAnimationType().newAnimation(false, layer, image, res);
		}
		hp = resource.getHP();
		this.damageLevel = new Image(image.getLocation().clone(), blockDimension, "", LightSpriteSheet.getLightSpriteSheet(Sprites.DamageLevel.getSpriteSheet()), null);
	}

	public Location getLocation(){
		return location;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Animation getAnim() {
		return anim;
	}
	
	public Resource getResource() {
		return res;
	}

	public GridData show(){	
		if(!created){
			Engine.getEngine(this, this.getClass()).addImage(image, layer);
			created = true;
			Engine.getEngine(this, this.getClass()).addImage(this.damageLevel, layer);
			this.damageLevel.disabled = true;
		}else{
			image.disabled = false;
			if(this.isDamaged())this.damageLevel.disabled = false;
		}
		if(anim!=null)anim.start();
		return this;
	}
	
	public void destroyVisual(){
		Engine.getEngine(this, this.getClass()).removeImage(layer, image);
		Engine.getEngine(this, this.getClass()).removeImage(layer, damageLevel);
		if(anim!=null){
			anim.stop();
			AnimationManager.remove(anim);
		}
	}
	
	public void hide(){
		if(anim!=null)anim.stop();
		image.disabled = true;
		this.damageLevel.disabled = true;
	}
	
	public void setLocation(Location loc){
		this.hitbox = new Hitbox(new Location(loc.getX()*Map.DEFAULT_SQUARESIZE + res.getHitbox().getX(), loc.getY()*Map.DEFAULT_SQUARESIZE + res.getHitbox().getY()), res.getHitbox().getDimension());
		this.image.setLocation(new Location(loc.getX()*Map.DEFAULT_SQUARESIZE, loc.getY()*Map.DEFAULT_SQUARESIZE));
		this.damageLevel.setLocation(this.image.getLocation().clone());
		this.location = loc;
	}
	
	public int getDefaultSpriteState(){
		return res.getSpriteIDs()[spriteState];
	}

	public void setSpriteState(int state) {
		spriteState = state;		
		this.updateImage();
	}

	protected void updateImage() {
		this.image.setSpriteState(res.getSpriteIDs()[spriteState]);
	}

	public boolean overlaps(Hitbox hb){
		boolean overlaps = this.hitbox.overlaps(hb);
//		System.out.println(hb.toString()+" -> "+this.hitbox.toString() + " -> "+overlaps);
		return overlaps;
	}
	
	public void damage(int amount){
		hp-=amount;
		if(hp<0)hp=0;
		updateDamageLevel();
	}
	
	
	private void updateDamageLevel() {
		if(this.isDamaged() && !isDestroyed()){
			double d = 1.0-(double)this.hp/(double)this.getResource().getHP();
			int state = (int)(d*Sprites.DamageLevel.getSpriteSheet().getSpriteAmount());
			this.damageLevel.setSpriteState(state);
			this.damageLevel.disabled = this.image.disabled;
		}
	}

	public void heal(int amount){
		hp+=amount;
		if(hp>this.getResource().getHP())hp = this.getResource().getHP();
		updateDamageLevel();
	}
	
	public boolean isDamaged(){
		return hp<this.getResource().getHP();
	}

	public boolean isDestroyed() {
		return hp<=0;
	}
}
