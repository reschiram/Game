package events.map;

import events.GameEvent;
import game.gridData.map.MapBlock;

public abstract class MapEvent extends GameEvent{
	
	private MapBlock block;

	public MapEvent(MapBlock block) {
		super();
		this.block = block;
	}
	
	public MapBlock getMapBlock() {
		return block;
	}

}
