package game.gridData.map;

import Data.DataObject;
import Data.Location;
import data.LightSpriteSheet;
import data.MapResource;
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
		this.surfaceLevel = surfaceLevel;
		if(isSurface())this.setLightLevel(1);
	}

	public boolean canHost(int width, int height) {
		return (Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getWidth() >= width && Map.DEFAULT_SQUARESIZE - this.res.getHitbox().getHeigth() >= height) || !((MapResource)this.res).isSolid();
	}
	
	@Override
	public void damage(int amount){
		super.damage(amount);
		if(this.isDestroyed())Map.getMap().deleteBlock(location, this.getResource().getLayerUp(), this.getResource().isGround());
	}

	public int getSurfaceLevel() {
		return this.surfaceLevel;
	}
}
