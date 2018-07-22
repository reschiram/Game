package game.entity;

import java.awt.Dimension;
import java.util.ArrayList;

import Data.DataObject;
import Data.Direction;
import Data.Hitbox;
import Data.Location;
import Data.Animation.Animation;
import Engine.Engine;
import data.ImageData;
import data.map.Lamp;
import game.entity.player.Player;
import game.entity.player.playerDrone.DroneTarget;
import game.entity.manager.EntityManager;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
import game.entity.type.data.EntityInventoryData;
import game.gridData.map.Mapdata;
import game.map.Map;
import game.overlay.DayManager;

public abstract class Entity {
	
	public static Direction DEFAULT_DIRECTION = Direction.LEFT; 
	public static int DEFAULT_ENTITY_LAYER = 4;
	public static int DEFAULT_ENTITY_UP    = 0;
	
	protected ImageData[] images;
	protected Animation anim;
	protected int layer;
	
	protected Hitbox hitbox;
	protected Direction direction;	
	protected boolean active = false;
	protected ArrayList<EntityType> entityTypes;
	
	private int  id;
	private long lastTick;	

	protected int speed;
	private boolean inAir = false;
	private double ySpeed = 0.0;
	private int jump_start = -1;
	private int maxFallSpeed = 20;
	
	private DataObject<Integer> lightLevel;
	private boolean hasCurrentlySurfaceLightLevel;
	
	public Entity(ArrayList<EntityType> entityTypes){
		this.entityTypes = entityTypes;
		this.entityTypes.add(EntityType.Entity);
	}
	
	protected void create(Animation anim, Location location, Dimension size, int speed, Direction direction, int layer, ImageData... images){
		this.images = images;
		this.anim = anim;
		this.hitbox = new Hitbox(location, size);
		this.speed = speed;
		this.direction = direction;
		this.layer = layer;
		this.id = EntityManager.getEntityManager().register(this);	
	}
	
	private boolean created = false;
	public void show(){
		if(created){
			for(ImageData image: images)image.getImage().disabled = false;
			if(anim!=null)anim.start();
		}else{
//			System.out.println(images[0].getImage().getSpriteState());
			for(ImageData image: images)Engine.getEngine(this, this.getClass()).addImage(image.getImage(), layer);
			if(anim!=null)anim.start();
		}
	}
	
	public void hide(){
		for(ImageData image: images)image.getImage().disabled = true;		
		if(anim!=null)anim.stop();
	}
	
	public void activate(){
		active = true;
	}
	
	public void deActivate(){
		active = false;
	}
	
	public void destroy(){
//		System.out.println("DESTROY");
		for(ImageData image: images){
			image.getImage().disabled = true;
			Engine.getEngine(this, this.getClass()).removeImage(layer, image.getImage());
		}
		active = false;
		this.anim.stop();
		EntityManager.getEntityManager().remove(this);
	}
	
	protected long timer;
	public void tick(){
		if(!inAir && canMove(0,1))inAir = true;
		
//		System.out.println(ySpeed+"|"+jump_start+"->"+inAir+"->"+canMove(0,1));
		
		if(inAir && jump_start!=-1){
			ySpeed-=Map.getMap().getAcceleration();
			if(ySpeed<=0 || !canMove(0, (int) -ySpeed)){
				ySpeed = 0;
				jump_start = -1;
			}else{
				this.setLocation(this.getX(), (int) -ySpeed+this.getY());
			}
		}
		if(inAir && jump_start==-1){
			ySpeed-=Map.getMap().getAcceleration();
			if(-ySpeed>maxFallSpeed)ySpeed = -maxFallSpeed;
			if(!canMove(0,(int) -ySpeed)){
				ySpeed = this.getY()-(Math.round((double)this.getY()/(double)Map.DEFAULT_SQUARESIZE))*Map.DEFAULT_SQUARESIZE;
				inAir = false;				
			}
			this.setLocation(this.getX(), (int) -ySpeed+this.getY());
			if(!inAir)ySpeed = 0;
		}
	}
	
	public boolean canReach(Location target) {
		for(int x = -1; x<=1; x++){
			int dy = 0;
			if(x == 0) dy = 1;
			for(int y = -1*dy; y<=dy; y++){
				if(y!=0 || x!=0){
//					System.out.println(x+"->"+y);
					Mapdata data = Map.getMap().getMapData(new Location(target.getX() + x, target.getY() + y))[Entity.DEFAULT_ENTITY_UP + Map.DEFAULT_BUILDLAYER];
//					System.out.println(data);
					if(data == null || data.canHost(this.getWidth(), this.getHeight())) return true;
				}
			}
		}
		return false;
	}
	
	public boolean move(Direction d){
//		if(hasType(EntityType.ItemEntity))System.out.println(canMove(d.getX()*speed, 0)+" -> "+this.getX()/Map.DEFAULT_SQUARESIZE+"|"+this.getY()/Map.DEFAULT_SQUARESIZE +"->"+hitbox.getDimension().toString());
		if((d.equals(Direction.LEFT) || d.equals(Direction.RIGHT))){
			if(canMove(d.getX()*speed, 0)){
				this.setLocation(-(int)(d.getX()*speed)+this.getX(), this.getY());
				return true;
			}else{
				int mx = ((int)((this.getX()+this.getWidth())/Map.DEFAULT_SQUARESIZE))*Map.DEFAULT_SQUARESIZE-(this.getX());
//				System.out.println(mx+"->"+this.getX());
				int dx = ((int)(mx/Map.DEFAULT_SQUARESIZE))*Map.DEFAULT_SQUARESIZE-mx;
				if(dx!= 0){
					int max = Map.DEFAULT_SQUARESIZE;
					if(this.getWidth()<Map.DEFAULT_SQUARESIZE)max = this.getWidth();
					if(dx==max)return false;
					while(mx<-max/2)mx+=max;
					while(mx> max/2)mx-=max;
					mx = Math.abs(mx);
//					System.out.println(mx);
					if(canMove(d.getX()*mx, 0)){
						this.setLocation(-(int)(d.getX()*mx)+this.getX(), this.getY());
						return true;
					}
				}
			}
		}else{
			if(d.equals(Direction.UP) && jump_start == -1 && ySpeed==0 && canMove(0, (int) -6.0)){
				this.inAir = true;
				this.jump_start = this.getY();
				ySpeed = 6.0;
				return true;
			}
		}
		return false;
	}

//	private boolean isOnGround() {
//		Location loc = new Location(Map.getMap().getXOver(this.getX())/Map.DEFAULT_SQUARESIZE, (int)  ((this.getY()+this.getHeight()-1)/Map.DEFAULT_SQUARESIZE));
////		if(hasType(EntityType.ItemEntity))System.out.println(this.getX()+"|"+this.getY()+"|"+getHeight()+" -> "+loc.toString());
//		Mapdata[] data = Map.getMap().getBlocks(loc);
//		return data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP] != null;
//	}

	protected boolean canMove(int mx, int my) {
		if(mx == 0 && my == 0)return true;
		
		int px = this.getX();
		int py = this.getY();
		this.hitbox.setLocation(this.hitbox.getX()-mx, this.hitbox.getY()+my);
		
		if(my==0){
			
			int x = 0;
			if(mx<=Direction.RIGHT.getX()) x = this.getWidth()-1;
			int maxY = (int) ((double)this.getHeight()/Map.DEFAULT_SQUARESIZE);
			for(int y = 0; y <= maxY; y++){
				
				int a = 1;
				if(y==maxY && maxY > 0)a=0;
				int dx = Map.getMap().getXOver(px-mx+x                                           );
				int dy = 					   py+my+this.getHeight()-(y*Map.DEFAULT_SQUARESIZE)-a;
				
//				if(this.hasType(EntityType.Drone))System.out.println("LEFT_RIGTH: "+dx+"|"+dy+"->"+px+"|"+py+"->"+mx+"|"+my);
				
				Mapdata[] data = Map.getMap().getBlocks(new Location(dx/Map.DEFAULT_SQUARESIZE, dy/Map.DEFAULT_SQUARESIZE));
				if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null && data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP].overlaps(this.hitbox)){
					this.hitbox.setLocation(this.hitbox.getX()+mx, this.hitbox.getY()-my);
					return false;
				}
				
			}
		}else{
			
			int y = 0;
			if(my>0) y = this.getHeight()-1;
			int maxX = (int) ((double)this.getWidth()/Map.DEFAULT_SQUARESIZE);
			for(int x = 0; x <= maxX; x++){ 
				
				int a = 1;
				if(x==maxX && maxX > 0)a=0;
				int dx = Map.getMap().getXOver(px-mx+this.getWidth() -(x*Map.DEFAULT_SQUARESIZE)-a);
				int dy = 					   py+my+y											    ;
				
//				if(this.hasType(EntityType.Drone))System.out.println("UP_DOWN: "+dx+"|"+dy+"->"+px+"|"+py+"->"+mx+"|"+my);
				
				Mapdata[] data = Map.getMap().getBlocks(new Location(dx/Map.DEFAULT_SQUARESIZE, dy/Map.DEFAULT_SQUARESIZE));
				if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null && data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP].overlaps(this.hitbox)){
					this.hitbox.setLocation(this.hitbox.getX()+mx, this.hitbox.getY()-my);
					return false;
				}
				
			}
		}
		
		this.hitbox.setLocation(this.hitbox.getX()+mx, this.hitbox.getY()-my);
//		System.out.println(this.h);
		/*
		if(mx<0 && my==0){
			for(int x = 1; x<= Math.ceil((double)this.getWidth()/Map.DEFAULT_SQUARESIZE); x++){
				for(int y = 1; y<= Math.ceil((double)this.getHeight()/Map.DEFAULT_SQUARESIZE); y++){
					Mapdata[] data = Map.getMap().getBlocks(new Location(Map.getMap().getXOver((int) (((double)(this.getX()-mx+(x*Map.DEFAULT_SQUARESIZE)-1)/(double)Map.DEFAULT_SQUARESIZE)*Map.DEFAULT_SQUARESIZE))/Map.DEFAULT_SQUARESIZE,
																		 (int) 			((double)(this.getY()+my+(y*Map.DEFAULT_SQUARESIZE)-1)/(double)Map.DEFAULT_SQUARESIZE)));
					if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null)return false;
				}
			}
		}else if(mx>0 && my==0){
			for(int x = 0; x< Math.ceil((double)this.getWidth()/Map.DEFAULT_SQUARESIZE); x++){
				for(int y = 1; y<= Math.ceil((double)this.getHeight()/Map.DEFAULT_SQUARESIZE); y++){
					Mapdata[] data = Map.getMap().getBlocks(new Location(Map.getMap().getXOver((int) (((double)(this.getX()-mx+(x*Map.DEFAULT_SQUARESIZE))/(double)Map.DEFAULT_SQUARESIZE)*Map.DEFAULT_SQUARESIZE))/Map.DEFAULT_SQUARESIZE,
																		 (int) 			((double)(this.getY()+my+(y*Map.DEFAULT_SQUARESIZE)-1)/(double)Map.DEFAULT_SQUARESIZE)));
					if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null)return false;
				}
			}
		}else if(mx==0 && my<0){
			for(int x = 0; x<= Math.ceil((double)this.getWidth()/Map.DEFAULT_SQUARESIZE); x++){
				for(int y = 1; y<= Math.ceil((double)this.getHeight()/(double)Map.DEFAULT_SQUARESIZE); y++){
					Mapdata[] data = Map.getMap().getBlocks(new Location(Map.getMap().getXOver((int) (((double)(this.getX()-mx+(x*Map.DEFAULT_SQUARESIZE)-x)/(double)Map.DEFAULT_SQUARESIZE)*Map.DEFAULT_SQUARESIZE))/Map.DEFAULT_SQUARESIZE,
																		 y+ (int) Math.round((double)(this.getY()+my)/(double)Map.DEFAULT_SQUARESIZE)));
					if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null)return false;
				}
			}
		}else if(mx==0 && my>0){
			for(int x = 0; x<= Math.ceil((double)this.getWidth()/Map.DEFAULT_SQUARESIZE); x++){
				for(int y = 0; y< Math.ceil(this.getHeight()/Map.DEFAULT_SQUARESIZE); y++){
					Mapdata[] data = Map.getMap().getBlocks(new Location(Map.getMap().getXOver((int) (((double)(this.getX()-mx+(x*Map.DEFAULT_SQUARESIZE)-x)/(double)Map.DEFAULT_SQUARESIZE)*Map.DEFAULT_SQUARESIZE))/Map.DEFAULT_SQUARESIZE,
																		 y+ (int) Math.round((this.getY()+my)/Map.DEFAULT_SQUARESIZE)));
					if(data[Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP]!=null)return false;
				}
			}
		}
		*/
		return true;
	}

	public int getX() {
		return hitbox.getX();
	}
	
	public int getY() {
		return hitbox.getY();
	}

	public void setLocation(int x, int y){
		x = Map.getMap().getXOver(x);
		if(x == hitbox.getX() && y == hitbox.getY())return;
		Engine engine = Engine.getEngine(this, this.getClass());
		for(int i = 0; i<this.images.length; i++){
			engine.updateImage(layer, this.images[i].getImage(), new Location(this.images[i].getLocation().getX()+x, this.images[i].getLocation().getY()+y));
		}
		this.hitbox.setLocation(x, y);
	}

	public Location getLocation() {
		return hitbox.getLocation();
	}

	public Location getBlockLocation() {
		return new Location((int) Math.round((double)hitbox.getX()/(double)Map.DEFAULT_SQUARESIZE), (int) (Math.round((double)hitbox.getY()/(double)Map.DEFAULT_SQUARESIZE)));
	}
	
	public Location getTopLeftBlockLocation() {
		return new Location((int) ((double)hitbox.getX()/(double)Map.DEFAULT_SQUARESIZE), (int) ((double)hitbox.getY()/(double)Map.DEFAULT_SQUARESIZE));
	}

	public boolean hasType(EntityType type) {
		return entityTypes.contains(type);
	}

	public boolean hasData(int dataCode) {
		for(EntityType type: this.entityTypes){
			if(type.hasData(dataCode)) return true;
		}
		return false;
	}

	public EntityData getData(int dataCode) {
		for(EntityType type: this.entityTypes){
			if(type.hasData(dataCode)) return type.getData(dataCode);
		}
		return null;
	}
	
	public void setLastTick(long tick){
		this.lastTick = tick;
	}
	
	public long getLastTick(){
		return lastTick;
	}
	
	public int getWidth(){
		return hitbox.getWidth();
	}
	
	public int getHeight(){
		return hitbox.getHeigth();
	}
	
	public int getLightLevel(){
		if(this.lightLevel==null)return 0;
		return this.lightLevel.getData();
	}
	
	public void setLightLevel(int lightLevel){
		 if(isSurface()){
			int currentSurfaceLightLevel = DayManager.getDayManager().getDayLightLevel();
	//		System.out.println(currentSurfaceLightLevel+" -> "+lightLevel);
			if(currentSurfaceLightLevel>=lightLevel){
				if(!hasCurrentlySurfaceLightLevel || this.lightLevel.getData()!=currentSurfaceLightLevel){
					hasCurrentlySurfaceLightLevel = true;
					this.lightLevel = DayManager.getDayManager().getDayLightLevelData();
					for(ImageData image:images)image.getImage().setSpriteID(this.lightLevel);
				}	
				return;	
			}else hasCurrentlySurfaceLightLevel  = false;
		}
		this.lightLevel = new DataObject<Integer>(lightLevel);
		for(ImageData image:images)image.getImage().setSpriteID(Lamp.DEFAULT_LIGHT_STATES-lightLevel-1);
	}

	private boolean isSurface() {
		Mapdata data = Map.getMap().getChunks()[this.getX()/(Map.DEFAULT_SQUARESIZE*Map.DEFAULT_CHUNKSIZE)][this.getY()/(Map.DEFAULT_SQUARESIZE*Map.DEFAULT_CHUNKSIZE)].getMapData(this.getTopLeftBlockLocation(), true)[0];
//		System.out.println(data);
		if(data!=null){
//			System.out.println(data.isSurface());
			return data.isSurface();
		}
		return false;
		
	}

	public Hitbox getHitbox() {
		return hitbox;
	}

	public int getSpeed() {
		return speed;
	}

	public int getYSpeed() {
		return (int) ySpeed;
	}
}
