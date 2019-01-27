package events.entity;

import Data.Location;

public class EntityCreationEvent extends EntityEvent{

	private static int lastRrequestID = 0;
	
	private int entityType;
	private Location blockSpawnLocation;
	private int requestID;
	
	public EntityCreationEvent(int entityType, Location blockSpawnLocation) {
		super(null);
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
