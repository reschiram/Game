package data.map;

import Data.Location;
import data.MapResource;
import file.csv.CSV_File;
import game.map.MapGenerationData;
import game.map.MapGenerator;

public class ServerMap {
	
	int[][][] mapGround;
	int[][][] mapBuild;
	int width;
	int height;
	int seed;
	
	private ServerMapFile mapFile;
	private ServerEntityManager entityManager;
	private InventoryManager inventoryManager;
	
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
		if(x<0)x = width+x;
		else if(x>= width)x = x-width;
		return x;
	}

}
