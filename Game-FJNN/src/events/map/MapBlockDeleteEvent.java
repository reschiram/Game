package events.map;

import game.gridData.map.MapBlock;

public class MapBlockDeleteEvent extends MapEvent{

	public MapBlockDeleteEvent(MapBlock block) {
		super(block);
	}
}
