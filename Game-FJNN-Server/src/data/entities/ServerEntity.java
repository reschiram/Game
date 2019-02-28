package data.entities;

import Data.Hitbox;
import Data.Location;
import data.map.ServerMap;
import game.entity.type.EntityType;
import game.map.Map;

public abstract class ServerEntity {
	
	private int id;
	private Hitbox pixelHitbox;
	private EntityType entityType;
	private boolean alive = true;
	
	private ServerMap serverMap;

	public ServerEntity(int id, Location pixeLoc, EntityType entityType, ServerMap serverMap) {
		super();
		this.id = id;
		this.pixelHitbox = new Hitbox(pixeLoc, entityType.getSize());
		this.entityType = entityType;
		this.serverMap = serverMap;
	}

	public abstract String getExtraInfos(long currentClientID) throws Exception;	
	
	public int getId() {
		return id;
	}
	
	public Location getPixeLocation() {
		return pixelHitbox.getLocation();
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
		this.pixelHitbox.setLocation(pixeLoc);
	}

	public Location getBlockLocation() {
		return new Location(pixelHitbox.getX() / Map.DEFAULT_SQUARESIZE, pixelHitbox.getY() / Map.DEFAULT_SQUARESIZE);
	}
	
	public void tick() {
		
	}

	public Hitbox getPixelHitbox() {
		return pixelHitbox;
	}

	public ServerMap getCurrentSMap() {
		return serverMap;
	}

	public boolean canReach(Location blockLocation) {
		for(int x = -1; x<=1; x++){
			int dy = 0;
			if(x == 0) dy = 1;
			for(int y = -1*dy; y<=dy; y++){
				if(y!=0 || x!=0){
					if(this.getCurrentSMap().canHost(this, new Location(blockLocation.getX() + x, blockLocation.getY() + y))) return true;
				}
			}
		}
		return false;
	}
}
