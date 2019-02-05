package game.gridData.map;

import Data.DataObject;
import Data.Location;
import data.LightSpriteSheet;
import data.MapResource;
import data.map.Lamp;
import game.Game;
import game.gridData.GridData;
import game.map.Map;
import game.overlay.DayManager;

public abstract class Mapdata extends GridData{	
	
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
	
	public void setLightLevel(int lightLevel){
		if(this.isSurface() && lightLevel<=DayManager.getDayManager().getDayLightLevel(this.surfaceLevel)){
			this.lightLevel = DayManager.getDayManager().getDayLightLevelData(this.surfaceLevel);
			updateImage();
			return;
		}
		this.lightLevel = new DataObject<Integer>(lightLevel);
		updateImage();
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
		if(isAlwaysSurface()){
			if(!this.isSurface()){
				this.surfaceLevel = Lamp.DEFAULT_SURFACE_LEVELS-1;
				this.setLightLevel(1);
			}
			else return;
		}
		boolean wasSurface = isSurface();
		this.surfaceLevel = surfaceLevel;
		if(isSurface())this.setLightLevel(1);
		else if(wasSurface){
			this.setLightLevel(1);
			Game.getLightOverlay().update(this, false);
		}
	}

	public boolean isAlwaysSurface() {
		if(this.getResource().isGround() && this.getResource().isAlwaysSurface()){
			Mapdata[] data = Map.getMap().getMapData(this.location);
			for(int i = Map.DEFAULT_BUILDLAYER; i<data.length; i++){
				if(data[i]!=null && data[i].getResource().isOpaque())return false;
			}
			return true;
		}		
		return false;
	}

	public boolean canHost(int width, int height) {
		return (Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getWidth() >= width && Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getHeigth() >= height) || !((MapResource)this.res).isSolid();
	}
	
	@Override
	public void damage(int amount){
		super.damage(amount);
		if(this.isDestroyed())Map.getMap().deleteBlock(location, this.getResource().getLayerUp(), this.getResource().isGround(), true);
	}

	public int getSurfaceLevel() {
		return this.surfaceLevel;
	}
}
