package data.map;

import Data.Location;
import game.gridData.map.Mapdata;

public abstract class UpdatableBlockData extends BlockData{

	public UpdatableBlockData(String name) {
		super(name);
	}
	
	public void update(Mapdata data, Location location){
		
	}
	
	public abstract void update(Mapdata data, int x, int y);
}
