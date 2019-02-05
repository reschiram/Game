package data.entities;

import Data.Location;
import game.entity.type.EntityType;
import game.map.Map;

public abstract class ServerEntity {
	
	private int id;
	private Location pixeLoc;
	private EntityType entityType;
	private boolean alive = true;

	public ServerEntity(int id, Location pixeLoc, EntityType entityType) {
		super();
		this.id = id;
		this.pixeLoc = pixeLoc;
		this.entityType = entityType;
	}

	public abstract String getExtraInfos(long currentClientID) throws Exception;	
	
	public int getId() {
		return id;
	}
	
	public Location getPixeLocation() {
		return pixeLoc;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void destroy() {
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setPixelLocation(Location pixeLoc) {
		this.pixeLoc.setLocation(pixeLoc);
	}

	public Location getBlockLocation() {
		return new Location(pixeLoc.getX() / Map.DEFAULT_SQUARESIZE, pixeLoc.getY() / Map.DEFAULT_SQUARESIZE);
	}
}
