package game.entity.player.playerDrone;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.MapResource;
import game.entity.Entity;
import game.entity.ItemEntity;
import game.gridData.map.Mapdata;
import game.inventory.crafting.Recipe;
import game.map.Map;
import sprites.Sprites;

public class DroneTarget {
	
	private static int MARKERLEVEL = 3;
	
	private Location location;
	private Image marker;
	private int buildID;
	
	private boolean done = false;

	private PlayerDrone drone;
	
	public DroneTarget(PlayerDrone drone,Location location, int buildID){
		this.location = location.clone();
		this.drone = drone;
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
				if(data.getResource().hasDrops()){
					data.getResource().drop(location);
				}
			}
		}else{
			Mapdata data = Map.getMap().getChunks()[location.x/Map.DEFAULT_CHUNKSIZE][location.y/Map.DEFAULT_CHUNKSIZE].getMapData(location, false)[Entity.DEFAULT_ENTITY_UP];
			if(data!=null){
				data.damage(1);
				if(data.isDestroyed()){
					if(data.getResource().hasDrops()){
						data.getResource().drop(location);
					}
				}
			}else{
				Recipe recipe = Recipe.getRecipe(buildID);
				if(recipe!=null && recipe.craft(drone.getInventory())){
					Map.getMap().add(buildID, location, MapResource.getMapResource(buildID).isGround());
					this.done = true;
					drone.removeTarget(this.getLocation());
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
