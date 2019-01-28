package game.entity.requester;

import Data.Location;
import game.map.Map;

public abstract class EntityRequest {
	
	private static int lastRequestID = 0;
	
	private int requestID;
	private Location blockSpawn;
	
	public EntityRequest(Location blockSpawn) {
		lastRequestID++;
		this.requestID = lastRequestID;
		this.blockSpawn = blockSpawn;
	}

	public int getRequestID() {
		return requestID;
	}

	public Location getBlockSpawn() {
		return blockSpawn;
	}

	public int getPixelSpawn_X() {
		return blockSpawn.getX() * Map.DEFAULT_SQUARESIZE;
	}

	public int getPixelSpawn_Y() {
		return blockSpawn.getX() * Map.DEFAULT_SQUARESIZE;
	}

	public abstract void spawnEntity(int entityID, String extraInfos) throws Exception;

}
