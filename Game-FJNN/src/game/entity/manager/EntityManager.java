package game.entity.manager;

import java.awt.Dimension;
import java.util.ArrayList;

import Data.Location;
import game.GameManager;
import game.entity.Entity;
import game.entity.type.EntityType;
import game.map.Map;

public class EntityManager {
	
	private static EntityManager ENTITYMANAGER;
	public static EntityManager getEntityManager() {
		return ENTITYMANAGER;
	}
	
	private int lastID = 0;	
	private EntityChunk[][] entitys;

	public EntityManager(int width, int height){
		if(ENTITYMANAGER==null)ENTITYMANAGER = this;
		
		entitys = new EntityChunk[width][height];
	}

	public int register(Entity entity) {
		getPixelChunk(entity.getLocation()).entitys.add(entity);
		lastID++;
		return lastID;
	}

	public void remove(Entity entity) {
		getPixelChunk(entity.getLocation()).entitys.remove(entity);
	}
	
	public void tick(){
		for(int x = 0; x<entitys.length; x++){
			for(int y = 0; y<entitys[x].length; y++){
				if(entitys[x][y]!=null){
					for(int i = 0; i<entitys[x][y].entitys.size(); i++){
						Entity entity = entitys[x][y].entitys.get(i);
						if(entity.getLastTick()!=GameManager.TickManager.getCurrentTick()){
							entity.tick();
							entity.setLastTick(GameManager.TickManager.getCurrentTick());
							int ex = entity.getLocation().getX()/(Map.DEFAULT_SQUARESIZE*Map.DEFAULT_CHUNKSIZE);
							int ey = entity.getLocation().getY()/(Map.DEFAULT_SQUARESIZE*Map.DEFAULT_CHUNKSIZE);
							if(ex!=x || ey!=y){
								entitys[x][y].entitys.remove(entity);
								i--;
//								System.out.println(entity.hasType(EntityType.ItemEntity)+"|"+entity.getX()+"|"+entity.getY());
								getChunk(ex,ey).entitys.add(entity);
							}
						}
					}
				}
			}
		}
	}

	public ArrayList<Entity> getEntityBlockLocation(EntityType Type, Location location, Dimension dimension) {
		ArrayList<Entity> entitys = new ArrayList<>();
		for(EntityChunk chunk: getBlockChunks(location, dimension)){
			for(Entity entity: chunk.entitys){
				if(entity.hasType(Type))entitys.add(entity);
			}
		}
		return entitys;
	}

	private ArrayList<EntityChunk> getBlockChunks(Location location, Dimension dimension) {
		ArrayList<EntityChunk> chunks = new ArrayList<>();
		for(int x = location.getX()/Map.DEFAULT_CHUNKSIZE; x<(dimension.getWidth()+location.getX())/Map.DEFAULT_CHUNKSIZE; x++){
			for(int y = location.getY()/Map.DEFAULT_CHUNKSIZE; y<(dimension.getHeight()+location.getY())/Map.DEFAULT_CHUNKSIZE; y++){
				if(entitys[x][y]!=null)chunks.add(getChunk(x,y));
			}
		}
		return chunks;
	}

	public ArrayList<Entity> getEntityPixelLocation(EntityType Type, Location location, Dimension dimension) {
		ArrayList<Entity> entitys = new ArrayList<>();
		for(EntityChunk chunk: getPixelChunks(location, dimension)){
			for(Entity entity: chunk.entitys){
				if(entity.hasType(Type))entitys.add(entity);
			}
		}
//		System.out.println("<--->");
		return entitys;
	}

	private ArrayList<EntityChunk> getPixelChunks(Location location, Dimension dimension) {
		ArrayList<EntityChunk> chunks = new ArrayList<>();
		for(int x = location.getX()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE); x<(dimension.getWidth()+location.getX())/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE); x++){
			for(int y = location.getY()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE); y<(dimension.getHeight()+location.getY())/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE); y++){
				int mx = Map.getMap().getXOver(x*Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE);
				if(entitys[mx][y]!=null)chunks.add(getChunk(mx,y));
			}
		}
		return chunks;
	}

	public EntityChunk getBlockChunk(Location blockLocation) {
		EntityChunk chunk = entitys[blockLocation.getX()/Map.DEFAULT_CHUNKSIZE][blockLocation.getY()/Map.DEFAULT_CHUNKSIZE];
		if(chunk == null){
			chunk = new EntityChunk();
			entitys[blockLocation.getX()/Map.DEFAULT_CHUNKSIZE][blockLocation.getY()/Map.DEFAULT_CHUNKSIZE] = chunk;
		}
		return chunk;
	}
	
	private EntityChunk getPixelChunk(Location location){
		EntityChunk chunk = entitys[location.getX()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)][location.getY()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)];
//		System.out.println(location.toString()+" -> "+ (location.getX()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)+ "|" +(location.getY()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE))));
		if(chunk == null){
			chunk = new EntityChunk();
			entitys[location.getX()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)][location.getY()/(Map.DEFAULT_CHUNKSIZE*Map.DEFAULT_SQUARESIZE)] = chunk;
		}
//		System.out.println(entitys[3][1]);
		return chunk;
	}

	private EntityChunk getChunk(int x, int y) {
		EntityChunk chunk = entitys[x][y];
		if(chunk==null){
			chunk = new EntityChunk();
			entitys[x][y] = chunk;
		}
		return chunk;
	}
	
}
