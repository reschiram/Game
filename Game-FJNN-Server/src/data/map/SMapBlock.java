package data.map;

import Data.Location;
import data.MapResource;

public class SMapBlock {

	private int hp;
	private Location blockLocation;
	private int layer;
	private MapResource res;

	public SMapBlock(Location blockLocation, int layer, int resId) {
		this.blockLocation = blockLocation;
		this.layer = layer;
		this.res = MapResource.getMapResource(resId);
		this.hp = res.getHP();
	}

	public int getHp() {
		return hp;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	public int getLayer() {
		return layer;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public MapResource getResource() {
		return res;
	}
	
	@Override
	public String toString() {
		return "SMapBlock(" + blockLocation + " , " + layer + " , " + res.getID() + " , " + hp + ")";
	}
}
