package data.map;

import Data.Location;

public abstract class InteractableBlockData extends BlockData{

	public InteractableBlockData(String name) {
		super(name);
	}
	
	public abstract void act(Location loc);
	
	public abstract void stop();

}
