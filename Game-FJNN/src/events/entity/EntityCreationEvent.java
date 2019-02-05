package events.entity;

import Data.Location;
import game.map.Map;

public class EntityCreationEvent extends EntityEvent{

	private static int lastRrequestID = 0;
	
	private int entityType;
	private Location blockSpawnLocation;
	private int requestID;
	
	public EntityCreationEvent(int entityType, Location blockSpawnLocation) {
		super(null, new Location(
				blockSpawnLocation.getX() * Map.DEFAULT_SQUARESIZE,
				blockSpawnLocation.getY() * Map.DEFAULT_SQUARESIZE)
		);
		this.blockSpawnLocation = blockSpawnLocation;
		this.requestID = lastRrequestID+1;
		lastRrequestID++;
	}

	public Location getBlockSpawnLocation() {
		return blockSpawnLocation;
	}

	public int getRequestID() {
		return requestID;
	}

	public int getEntityType() {
		return entityType;
	}

}
