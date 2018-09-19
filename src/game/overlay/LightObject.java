package game.overlay;

import Data.Location;
import game.map.Map;

public class LightObject {
	
	private Location location;
	private Object master;
	private int lightDistance;
	private double lightStrength;
	
	public LightObject(Object Master, Location location, int LightDistance, double lightStrength){
		this.location = location;
		this.master = Master;
		this.lightDistance = LightDistance;
		this.lightStrength = lightStrength;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Object getMaster() {
		return master;
	}

	public int getLightDistance() {
		return lightDistance;
	}

	public double getLightStrength() {
		return lightStrength;
	}

	public boolean contains(Location loc) {
		int dy = Math.abs(this.getLocation().getY()-loc.getY());
		int dx = Math.abs(this.getLocation().getX()-loc.getX());
		if(dx>Map.getMap().getWidth()/2)dx=Map.getMap().getWidth()-dx;
		return (dx+dy)/lightStrength<lightDistance;
	}

}
