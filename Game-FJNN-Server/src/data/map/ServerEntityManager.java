package data.map;

import java.util.Collection;
import java.util.HashMap;

import data.entities.ServerEntity;

public class ServerEntityManager {
	
	private HashMap<Integer, ServerEntity> entities = new HashMap<>();
	private int lastID = 0;

	public ServerEntityManager() {
		
	}
	
	public ServerEntity getEntity(int id) {
		return this.entities.get(id);
	}
	
	public int addEntity(ServerEntity entity) {
		int id = this.lastID + 1;
		this.lastID++;
		
		entity.setID(id);
		this.entities.put(entity.getId(), entity);
		return id;
	}

	public Collection<ServerEntity> getAllEntitys() {
		return entities.values();
	}

}
