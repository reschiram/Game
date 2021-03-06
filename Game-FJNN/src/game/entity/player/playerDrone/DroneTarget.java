package game.entity.player.playerDrone;

import java.awt.Dimension;
import java.util.ArrayList;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.entity.player.playerDrone.module.CTModule;
import game.map.Map;
import sprites.Sprites;

public abstract class DroneTarget {
	
	private static int MARKERLEVEL = 3;
	
	protected Location blockLocation;
	protected Image marker;
	
	protected boolean done = false;

	protected ArrayList<Drone> drones = new ArrayList<>();
	
	public DroneTarget(Location blockLocation, Drone... drones){
		this.blockLocation = blockLocation.clone();
		for(Drone drone: drones)addDrone(drone);
		this.marker = new Image(new Location(blockLocation.getX()*Map.DEFAULT_SQUARESIZE, blockLocation.getY()*Map.DEFAULT_SQUARESIZE), new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE),
				"", Sprites.Marker.getSpriteSheet(), null);
	}
	
	public void addDrone(Drone drone) {
		this.drones.add(drone);
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
			while(this.drones.size()>0){
				Drone drone = this.drones.get(0);
				CTModule module = (CTModule) drone.getModule(CTBModule.class);
				if(module != null)module.removeTarget(this.blockLocation);
				module = (CTModule) drone.getModule(CTDModule.class);
				if(module!=null)module.removeTarget(this.blockLocation);
			}
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

	public void removeDrone(Drone drone) {
		this.drones.remove(drone);
		if(this.drones.isEmpty()){
			this.destroyVisulas();
		}
	}

}
