package game.overlay;

import Data.Location;
import game.GameManager;
import game.map.Map;

public class LightObject {

	public static final int State_UNCHANGED = 0;
	public static final int State_IGNORE = 1;	
	public static final int State_NEW = 2;
	public static final int State_REMOVED = 3;
	public static final int State_EDIT = 4;
	
	private Location location;
	private Object master;
	private int lightDistance;
	private double lightStrength;
	
	private int state = State_NEW;
	
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void changeState(int state) {
		if(state != State_REMOVED && state != State_EDIT)return;
		
		if(this.state == State_UNCHANGED){
			this.state = state;
		}else if(this.state == State_IGNORE){
			this.state = state;
		}else if(this.state == State_NEW){
			if(state == State_REMOVED)this.state = State_IGNORE;
		}else if(this.state == State_REMOVED){
			if(state == State_NEW)this.state = State_NEW;
		}else if(this.state == State_EDIT){
			this.state = state;
		}
	}

	public double getMaxDistance() {
		return Math.sqrt(this.lightDistance*lightDistance*2.0);
	}

}
