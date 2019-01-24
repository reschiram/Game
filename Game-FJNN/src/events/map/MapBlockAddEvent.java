package events.map;

import game.gridData.map.MapBlock;

public class MapBlockAddEvent extends MapEvent{

	public MapBlockAddEvent(MapBlock block) {
		super(block);
	}

}
