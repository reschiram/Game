package game.entity.requester;

import Data.Location;
import client.Request;
import game.map.Map;

public abstract class EntityRequest extends Request{
	private Location blockSpawn;
	
	public EntityRequest(Location blockSpawn) {
		super();
		this.blockSpawn = blockSpawn;
	}

	public Location getBlockSpawn() {
		return blockSpawn;
	}

	public int getPixelSpawn_X() {
		return blockSpawn.getX() * Map.DEFAULT_SQUARESIZE;
	}

	public int getPixelSpawn_Y() {
		return blockSpawn.getY() * Map.DEFAULT_SQUARESIZE;
	}

	public abstract void spawnEntity(int entityID, String extraInfos) throws Exception;

}
