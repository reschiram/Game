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
import game.entity.manager.EntityManager;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
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
	protected EntityMoveManager moveManager;
	
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
		this.moveManager = new EntityMoveManager(this);
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
		for(ImageData image: images){
			image.getImage().disabled = true;
			Engine.getEngine(this, this.getClass()).removeImage(layer, image.getImage());
		}
		active = false;
		this.anim.stop();
		EntityManager.getEntityManager().remove(this);
	}
	
	public void tick(){
		this.moveManager.tick();
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
		return new Location((int)(((double)Map.getMap().getXOver(hitbox.getX()+getWidth()/2-1))/(double)Map.DEFAULT_SQUARESIZE), (int) (((double)(hitbox.getY()+getHeight()/2-1)/(double)Map.DEFAULT_SQUARESIZE)));
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

	public int getMaxSpeed() {
		return speed;
	}
	
	public void move(Direction d){
		this.moveManager.move(d);
	}
}
