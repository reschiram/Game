package events.map;

import Data.Location;
import data.MapResource;
import game.gridData.map.MapBlock;

public class MapBlockAddEvent extends MapEvent{
	
	private int resID;
	private Location blockLoc;

	public MapBlockAddEvent(MapBlock block) {
		super(block);
	}

	public MapBlockAddEvent(MapBlock block, int resourceID,Location blockLoc) {
		super(block);
		this.resID = resourceID;
		this.blockLoc = blockLoc;
	}

	public MapResource getResource() {
		return MapResource.getMapResource(resID);
	}

	public Location getBlockLocation() {
		return blockLoc;
	}

}
