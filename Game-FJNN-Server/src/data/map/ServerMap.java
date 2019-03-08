package data.map;

import Data.Hitbox;
import Data.Location;
import data.MapResource;
import data.entities.ServerEntity;
import data.entities.ServerItemEntity;
import data.server.Lobby;
import file.csv.CSV_File;
import game.entity.Entity;
import game.entity.type.EntityType;
import game.inventory.drops.Drop;
import game.map.Map;
import game.map.MapGenerationData;
import game.map.MapGenerator;
import server.GameSM;

public class ServerMap {
	
	SMapBlock[][][] mapBlocks;
	
	int[][][] mapGround;
	int[][][] mapBuild;
	int width;
	int height;
	int seed;
	
	private ServerMapFile mapFile;
	private ServerEntityManager entityManager;
	private InventoryManager inventoryManager;
	
	private MapSM mapSM;
	
	public ServerMap(CSV_File mapFile) {
		this.mapFile = new ServerMapFile(this, mapFile);	
		this.entityManager = new ServerEntityManager();	
		this.inventoryManager = new InventoryManager();
	}
	
	public void generateMap(int seed) {
		MapGenerator generator = new MapGenerator();
		
		MapGenerationData data = generator.generateMapData(seed);
		this.width = data.getWidth();
		this.height = data.getHeight();
		this.seed = seed;
		
		this.mapBuild = data.getBuildData();
		this.mapGround = data.getGroundData();

		this.mapBlocks = new SMapBlock[width][height][mapGround[0][0].length + mapGround[0][0].length];
	}
	
	public ServerMapFile getMapFile() {
		return this.mapFile;
	}

	public String getName() {
		return this.mapFile.getFileName();
	}

	public int getSeed() {
		return seed;
	}

	public MapGenerationData getGenerationData() {
		return new MapGenerationData(mapGround, mapBuild, seed);
	}

	public ServerEntityManager getEntityManager() {
		return entityManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public void addMapBlock(MapResource res, Location blockLoc) {
		int[][][] data = null;
		if(res.isGround()) data = mapGround;
		else data = mapBuild;
		
		int oldResID = data[getBlockXOver(blockLoc.getX())][blockLoc.getY()][res.getLayerUp()];			
		if(oldResID != Integer.parseInt(MapResource.noResourceID)) removeMapBlock(blockLoc, res.getLayerUp());
		data[getBlockXOver(blockLoc.getX())][blockLoc.getY()][res.getLayerUp()] = res.getID();
		
		//TODO: involve mapBlock <-> blockPart  
	}

	public void removeMapBlock(Location blockLoc, int layer) {
		int[][][] data = null;
		if(layer < mapGround[0][0].length) data = mapGround;
		else {
			data = mapBuild;
			layer -= mapGround[0][0].length;
		}
					
		data[getBlockXOver(blockLoc.getX())][blockLoc.getY()][layer] = 0;
	}
	
	public int getBlockXOver(int x) {
		while (x < 0) x = width + x;
		while (x >= width) x = x - width;
		return x;
	}

	public void start(Lobby lobby, GameSM gameSM) {
		MapSEM mapSEM  = new MapSEM(lobby, gameSM);
		
		this.mapSM = new MapSM(mapSEM, lobby, gameSM);
	}

	public void tick() {
		this.entityManager.tick();
	}

	public MapSM getMapSM() {
		return mapSM;
	}

	public boolean canHost(ServerEntity entity, Location blockLocation) {
		int bx = getBlockXOver(blockLocation.getX());
		int by = blockLocation.getY();
		
		int resId = this.mapBuild[by][by][Entity.DEFAULT_ENTITY_UP];
		if(resId == 0) return true;
		
		MapResource res = MapResource.getMapResource(resId);
		if(res == null) return true;
		
		Hitbox hb = new Hitbox(
				new Location(	(bx * Map.DEFAULT_SQUARESIZE) + res.getHitbox().getX(),
								(by * Map.DEFAULT_SQUARESIZE) + res.getHitbox().getY()),
				res.getHitbox().getDimension());
		
		return !hb.overlaps(entity.getPixelHitbox());
	}

	public int getWidth() {
		return width;
	}

	public boolean hasBuildingBlock(Location blockTarget) {
		int resId = this.mapBuild[blockTarget.getX()][blockTarget.getY()][Entity.DEFAULT_ENTITY_UP];
		return resId != 0;
	}

	public int getHeight() {
		return height;
	}

	public int getXOver(int x) {
		while (x < 0) x = (width * Map.DEFAULT_SQUARESIZE) + x;
		while (x >= (width * Map.DEFAULT_SQUARESIZE)) x = x - (width * Map.DEFAULT_SQUARESIZE);
		return x;
	}

	public void updateMapBlock(Location blockLocation, int layer, int hp) {
		SMapBlock mapBlock = getSMapBlock(blockLocation, layer);
		
		if (mapBlock != null) {
			System.out.println("MapBlock-Update: " + hp + " -> " + mapBlock);
			
			
			if (hp > mapBlock.getHp()) {
				System.out.println("Old Update: " + hp + " -> " + mapBlock);
			} else mapBlock.setHp(hp);
			
			this.mapSM.publishMapBlockUpdate(mapBlock);
			
			if (mapBlock.getHp() == 0) this.destroyMapBlock(blockLocation, layer);
		} else System.out.println("Update on non existing MapBlock: " + blockLocation + " - " + layer);
	}

	private void destroyMapBlock(Location blockLocation, int layer) {
		SMapBlock mapBlock = getSMapBlock(blockLocation, layer);
		
		int bx = getBlockXOver(blockLocation.getX());
		int by = blockLocation.getY();
		
		if (mapBlock != null) {
			
			this.mapBlocks[bx][by][layer] = null;
			if (layer >= this.mapGround[bx][by].length) mapBuild[bx][by][layer - this.mapGround[bx][by].length] = 0;
			else mapGround[bx][by][layer] = 0;
			
			
			if (mapBlock.getResource().hasDrops()) {
				for (Drop drop : mapBlock.getResource().getDrops().getDrops()) {
					int amount = (int) (
							Math.round(((double) (drop.getMaxAmount() - drop.getMinAmount())) * Math.random())
							+ drop.getMinAmount()
					);
					
					System.out.println("Drop: " + amount + " x " + drop.getType().getID());
					
					for(int i = 0; i < amount; i++) {						
						ServerItemEntity entity = new ServerItemEntity(-1,
								new Location(bx * Map.DEFAULT_SQUARESIZE, by * Map.DEFAULT_SQUARESIZE),
								EntityType.ItemEntity, drop.getType(), this);
						
						this.entityManager.addEntity(entity);						
						this.mapSM.publishDrop(entity);
					}
				}
			}
		} else System.out.println("Tried to delete non existing MapBlock");
	}

	private SMapBlock getSMapBlock(Location blockLocation, int layer) {
		int bx = getBlockXOver(blockLocation.getX());
		int by = blockLocation.getY();
		
		SMapBlock mapBlock = this.mapBlocks[bx][by][layer];
		
		if (mapBlock == null) {
			if (layer >= this.mapGround[bx][by].length) {
				int index = layer - this.mapGround[bx][by].length;
				
				if(mapBuild[bx][by][index] != 0) {
					mapBlock = new SMapBlock(new Location(bx, by), layer , mapBuild[bx][by][index]);
					this.mapBlocks[bx][by][layer] = mapBlock;
				}
			} else {
				if(mapGround[bx][by][layer] != 0) {
					mapBlock = new SMapBlock(new Location(bx, by), layer , mapGround[bx][by][layer]);
					this.mapGround[bx][by][layer] = layer;
				}
			}
		}
		
		return mapBlock;
	}

}
