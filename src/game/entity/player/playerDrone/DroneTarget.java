package game.entity.player.playerDrone;

import java.awt.Dimension;

import Data.Hitbox;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import game.entity.Entity;
import game.entity.ItemEntity;
import game.gridData.map.Mapdata;
import game.map.Map;
import game.tick.TickManager;
import sprites.Sprites;

public class DroneTarget {
	
	private static int MARKERLEVEL = 3;
	
	private Location location;
	private Image marker;
	private int buildID;
	
	private boolean done = false;
	
	public DroneTarget(Location location, int buildID){
		this.location = location.clone();
		this.marker = new Image(new Location(location.getX()*Map.DEFAULT_SQUARESIZE, location.getY()*Map.DEFAULT_SQUARESIZE), new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE),
				"", Sprites.Marker.getSpriteSheet(), null);
		if(buildID==0)marker.setSpriteState(1);
		this.buildID = buildID;
	}
	
	public DroneTarget createVisuals(){
		Engine.getEngine(this, this.getClass()).addImage(marker, MARKERLEVEL);
		this.done = false;
		return this;
	}
	
	public void hide(){
		marker.disabled = true;
	}
	
	public void show(){
		marker.disabled = false;
	}
	
	public void destroyVisulas(){
		Engine.getEngine(this, this.getClass()).removeImage(MARKERLEVEL, marker);
	}

	public boolean interact(){
		if(done)return true;
		if(this.buildID==0){
			Mapdata data = Map.getMap().getChunks()[location.x/Map.DEFAULT_CHUNKSIZE][location.y/Map.DEFAULT_CHUNKSIZE].getMapData(location, false)[Entity.DEFAULT_ENTITY_UP];
			data.damage(1);
			if(data.isDestroyed()){
				destroyVisulas();
				this.done = true;
				if(data.getResource().hasItemType()){
					for(int i = 0; i<data.getResource().getItemAmount(); i++){
						new ItemEntity(data.getResource().getItemType(), new Location(location.getX()*Map.DEFAULT_SQUARESIZE, location.getY()*Map.DEFAULT_SQUARESIZE)).show();
					}
				}
			}
		}
		return false;
	}

	public int getID() {
		return buildID;
	}

	public Location getLocation() {
		return location;
	}

	public Location getPixelLocation() {
		return new Location(location.getX() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2, location.getY() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2);
	}
}
