package game.gridData;

import java.awt.Dimension;

import Data.Hitbox;
import Data.Location;
import Data.Animation.Animation;
import Data.Animation.AnimationManager;
import Data.Image.Image;
import Engine.Engine;
import data.Resource;
import game.map.Map;

public abstract class GridData{	

	protected static Dimension blockDimension = new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE);
	
	protected Hitbox hitbox;
	protected Location location;
	public int layer;		
	protected Image image;
	protected Animation anim;
	protected Resource res;
	protected int spriteState = 0;
	
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
//			System.out.println(res.getID()+"->"+image.getSpriteState()+"->"+layer);
			Engine.getEngine(this, this.getClass()).addImage(image, layer);
			created = true;
		}else image.disabled = false;
		if(anim!=null)anim.start();
		return this;
	}
	
	public void destroyVisual(){
		Engine.getEngine(this, this.getClass()).removeImage(layer, image);
		if(anim!=null){
			anim.stop();
			AnimationManager.remove(anim);
		}
	}
	
	public void hide(){
		if(anim!=null)anim.stop();
		image.disabled = true;
	}
	
	public void setLocation(Location loc){
		this.hitbox = new Hitbox(new Location(loc.getX()*Map.DEFAULT_SQUARESIZE + res.getHitbox().getX(), loc.getY()*Map.DEFAULT_SQUARESIZE + res.getHitbox().getY()), res.getHitbox().getDimension());
		this.image.setLocation(loc);
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
}
