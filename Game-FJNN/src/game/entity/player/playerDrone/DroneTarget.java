package game.entity.player.playerDrone;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import client.ComsData;
import events.GameEventManager;
import events.entity.drone.CTStatusEventDU;
import game.map.Map;
import sprites.Sprites;

public abstract class DroneTarget {
	
	private static int MARKERLEVEL = 3;
	
	protected Location blockLocation;
	protected Image marker;
	
	protected boolean done = false;

	protected DroneHost master;
	private int targetType;
	
	public DroneTarget(Location blockLocation, DroneHost master, int targetType){
		this.blockLocation = blockLocation.clone();
		this.master = master;
		this.targetType = targetType;
		this.marker = new Image(new Location(blockLocation.getX()*Map.DEFAULT_SQUARESIZE, blockLocation.getY()*Map.DEFAULT_SQUARESIZE), new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE),
				"", Sprites.Marker.getSpriteSheet(), null);
	}

	public DroneTarget createVisuals(){
		Engine.getEngine(this, this.getClass()).addImage(marker, MARKERLEVEL);
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
		if(done){
			GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this.master, this, ComsData.ActionTarget_StatusUpdate_Type_Remove));
			return true;
		}
		return false;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	public Location getPixelLocation() {
		return new Location(blockLocation.getX() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2, blockLocation.getY() * Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getKey() {
		return this.blockLocation.getX() + (this.blockLocation.getY() * Map.getMap().getWidth());
	}

	public int getTargetType() {
		return targetType;
	}
}
