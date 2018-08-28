package game.gridData.map;

import Data.DataObject;
import Data.Location;
import data.LightSpriteSheet;
import data.MapResource;
import data.map.Lamp;
import game.gridData.GridData;
import game.map.Map;
import game.overlay.DayManager;
import game.tick.TickManager;

public abstract class Mapdata extends GridData{	
	
	private DataObject<Integer> lightLevel = new DataObject<Integer>(0);
	private int surfaceLevel = 0;
	private long lastLightUpdate;
	
	public Mapdata(MapResource resource, int layer, Location loc) {
		super(resource, layer, loc);
	}
	
	protected void loadImage(){
		this.image.setSpriteSheet(LightSpriteSheet.getLightSpriteSheet(this.image.getSpriteSheet()));
		this.updateImage();
	}
	
	public MapResource getResource() {
		return (MapResource) res;
	}
	
	public void setLightLevel(int lightLevel){
		if(isSurface()){
			int newLightLevel = DayManager.getDayManager().getDayLightLevel()/(Lamp.DEFAULT_SURFACE_LEVELS-surfaceLevel);
			if(newLightLevel>lightLevel)lightLevel = newLightLevel;
		}
		this.lightLevel = new DataObject<Integer>(lightLevel);
		lastLightUpdate = TickManager.getCurrentTick();
		updateImage();
	}
	
	@Override
	protected void updateImage(){
		this.image.setSpriteID(Lamp.DEFAULT_LIGHT_STATES-lightLevel.getData()-1);
		this.image.setSpriteState(this.getDefaultSpriteState());		
		this.damageLevel.setSpriteID(Lamp.DEFAULT_LIGHT_STATES-lightLevel.getData()-1);
	}
	
	public int getLightLevel(){
		return this.lightLevel.getData();
	}

	public boolean isSurface() {
		return surfaceLevel!=0;
	}

	public void setSurface(int surfaceLevel) {
		this.surfaceLevel = surfaceLevel;
		setLightLevel(0);
	}

	public boolean canHost(int width, int height) {
		return Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getWidth() >= width && Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getHeigth() >= height;
	}
	
	@Override
	public void damage(int amount){
		super.damage(amount);
		if(this.isDestroyed())Map.getMap().deleteBlock(location, this.getResource().getLayerUp(), this.getResource().isGround());
	}

	public long lastLightUpdate() {
		return lastLightUpdate;
	}

}
