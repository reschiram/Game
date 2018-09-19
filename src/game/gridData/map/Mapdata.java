package game.gridData.map;

import Data.DataObject;
import Data.Location;
import data.LightSpriteSheet;
import data.MapResource;
import data.map.Lamp;
import game.gridData.GridData;
import game.map.Map;
import game.overlay.DayManager;
import game.overlay.LightOverlay;
import game.tick.TickManager;

public abstract class Mapdata extends GridData{	
	
	private long lastLightUpdate;
	private DataObject<Integer> lightLevel = new DataObject<Integer>(1);
	private int surfaceLevel = 0;
	
	public Mapdata(MapResource resource, int layer, Location loc) {
		super(resource, layer, loc);
		if(resource.hasData()){
			resource.getBlockData().load(this);
		}
	}
	
	protected void loadImage(){
		this.image.setSpriteSheet(LightSpriteSheet.getLightSpriteSheet(this.image.getSpriteSheet()));
		this.updateImage();
	}
	
	public MapResource getResource() {
		return (MapResource) res;
	}
	
	public int setLightLevel(int lightLevel){
		if(isSurface()){
			int newLightLevel = DayManager.getDayManager().getDayLightLevel(this.surfaceLevel)/(Lamp.DEFAULT_SURFACE_LEVELS-surfaceLevel);
			if(newLightLevel>lightLevel){
				this.lightLevel = DayManager.getDayManager().getDayLightLevelData(this.surfaceLevel);
				updateImage();
				return 2;
			}
		}
		if(((this.lastLightUpdate != TickManager.getCurrentTick() || this.lastLightUpdate == 0) && LightOverlay.IsGenerellUpdate()) || lightLevel>this.lightLevel.getData()){
			this.lightLevel = new DataObject<Integer>(lightLevel+1);
			updateImage();
			lastLightUpdate = TickManager.getCurrentTick();
			return 2;
		}else if(lightLevel==this.lightLevel.getData()-1)return 2;
		return 0;
	}
	
	@Override
	protected void updateImage(){
		this.image.setSpriteID(lightLevel);
		this.image.setSpriteState(this.getDefaultSpriteState());		
		this.damageLevel.setSpriteID(lightLevel);
	}
	
	public int getLightLevel(){
		return this.lightLevel.getData();
	}

	public boolean isSurface() {
		return surfaceLevel!=0;
	}

	public void setSurface(int surfaceLevel) {
		boolean surface = isSurface();
		this.surfaceLevel = surfaceLevel;
		if(isSurface()){
			setLightLevel(0);
		}else{
			if(surface)resetLight();
			else setLightLevel(0);
		}
	}

	public boolean canHost(int width, int height) {
		return (Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getWidth() >= width && Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getHeigth() >= height) || !((MapResource)this.res).isSolid();
	}
	
	@Override
	public void damage(int amount){
		super.damage(amount);
		if(this.isDestroyed())Map.getMap().deleteBlock(location, this.getResource().getLayerUp(), this.getResource().isGround());
	}

	public void resetLight() {
		if(!this.isSurface()){
			this.lightLevel = new DataObject<Integer>(1);
			updateImage();
			lastLightUpdate = TickManager.getCurrentTick();
		}
	}
}
