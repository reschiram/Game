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
	private boolean surface = false;
	private boolean hasCurrentlySurfaceLightLevel = false;
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
		lastLightUpdate = TickManager.getCurrentTick();
//		System.out.println(location.toString()+" -> "+surface+"|"+lightLevel+"|"+this.lighlevel);
//		if(surface && lightLevel<=this.lighlevel)return;
//		this.lighlevel = lightLevel;
//		this.updateImage();
		 if(surface){
			int currentSurfaceLightLevel = DayManager.getDayManager().getDayLightLevel();
//			System.out.println(currentSurfaceLightLevel+" -> "+lightLevel);
			if(currentSurfaceLightLevel>=lightLevel){
				if(!hasCurrentlySurfaceLightLevel || this.lightLevel.getData()!=currentSurfaceLightLevel){
					hasCurrentlySurfaceLightLevel = true;
					this.lightLevel = DayManager.getDayManager().getDayLightLevelData();
					this.image.setSpriteState(this.getDefaultSpriteState());
					this.image.setSpriteID(this.lightLevel);
					this.damageLevel.setSpriteID(this.lightLevel);
				}	
				return;	
			}else hasCurrentlySurfaceLightLevel  = false;
		}
		this.lightLevel = new DataObject<Integer>(lightLevel);
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
		return surface;
	}

	public void setSurface(boolean surface) {
		this.surface = surface;
		setLightLevel(0);
		/*
		int groundDistance = this.getLocation().getY()-Map.DEFAULT_GROUNDLEVEL;
		if(groundDistance<0)groundDistance = 0;
		int lightlevel = Lamp.DEFAULT_LIGHT_STATES-2;
		if(lightlevel<=0){
			lightlevel = 0;
			this.surface = false;
		}
		if(surface)this.setLightLevel(lightlevel);
		else this.setLightLevel(0);
		*/
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
