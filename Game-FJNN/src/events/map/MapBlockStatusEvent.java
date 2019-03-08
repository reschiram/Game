package events.map;

import game.gridData.map.MapBlock;

public class MapBlockStatusEvent extends MapEvent {

	private int hp;

	public MapBlockStatusEvent(MapBlock block, int hp) {
		super(block);
		
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

}
