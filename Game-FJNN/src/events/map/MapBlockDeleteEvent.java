package events.map;

import game.gridData.map.MapBlock;

public class MapBlockDeleteEvent extends MapEvent{

	private boolean doDrops;
	
	public MapBlockDeleteEvent(MapBlock block, boolean doDrops) {
		super(block);
		this.doDrops = doDrops;
	}

	public boolean doDrops() {
		return doDrops;
	}
}
