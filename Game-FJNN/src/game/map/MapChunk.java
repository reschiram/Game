package game.map;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import Data.Hitbox;
import game.gridData.map.MapBlock;
import game.gridData.map.Mapdata;

public class MapChunk {
	
	public final static Dimension size = new Dimension(Map.DEFAULT_CHUNKSIZE, Map.DEFAULT_CHUNKSIZE);
	
	private Hitbox hitbox;
	
	private Mapdata[][][] data;
	private int[][][] info;	
	
	private boolean finalized = false;
	
	private HashMap<Class<?>, ArrayList<MapBlock>> BlockData = new HashMap<>();
	
	
	public MapChunk(int chunkX, int chunkY, int[][][] mapInfo) {
		loadChunk(chunkX, chunkY);
		
		for (int x = hitbox.getX(); x < hitbox.getX() + size.getWidth(); x++) {
			for (int y = hitbox.getY(); y < hitbox.getY() + size.getHeight(); y++) {
				for (int i = 0; i < info.length; i++) {
					info[x - hitbox.getX()][y - hitbox.getY()][i] = mapInfo[x][y][i];
				}
			}
		}
	}
	
	public MapChunk(int chunkX, int chunkY, int[][][] groundData, int[][][] buildData) {
		loadChunk(chunkX, chunkY);
		
		for (int x = hitbox.getX(); x < hitbox.getX() + size.getWidth(); x++) {
			for (int y = hitbox.getY(); y < hitbox.getY() + size.getHeight(); y++) {
				for (int i = 0; i < info[0][0].length; i++) {
					if(i < groundData[x][y].length) info[x - hitbox.getX()][y - hitbox.getY()][i] = groundData[x][y][i];
					else info[x - hitbox.getX()][y - hitbox.getY()][i] = buildData[x][y][i - groundData[x][y].length];
				}
			}
		}
	}
	
	private void loadChunk(int chunkX, int chunkY) {
		this.hitbox = new Hitbox((int) (chunkX * size.getWidth()), (int) (chunkY * size.getHeight()),
				(int) size.getWidth(), (int) size.getHeight());

		this.data = new Mapdata	[(int) size.getWidth()][(int) size.getHeight()][4];
		this.info = new int		[(int) size.getWidth()][(int) size.getHeight()][4];
	}

	public void load(Map map) {
		if(finalized) return;
		
		for (int x = 0; x < size.getWidth(); x++) {
			for (int y = 0; y < size.getHeight(); y++) {
				for (int i = 0; i < info[x][y].length; i++) {
					if(info[x][y][i] != 0) map.addMapBlock(x + hitbox.getX(), y + hitbox.getY(), info[x][y][i], false);
				}
			}
		}
		
		map.finalize(this);
	}

	public void set(Mapdata mapdata) {
		int layer = mapdata.getResource().getLayerUp() + (mapdata.getResource().isGround() ? 0 : 2);
		
		int x = mapdata.getLocation().getX()-hitbox.getX();
		int y = mapdata.getLocation().getY()-hitbox.getY();
		
		this.data[x][y][layer] = mapdata;
		
		if(mapdata.getResource().hasData() && mapdata instanceof MapBlock){
			if(!BlockData.containsKey(mapdata.getResource().getBlockData().getClass()))BlockData.put(mapdata.getResource().getBlockData().getClass(), new ArrayList<>());
			BlockData.get(mapdata.getResource().getBlockData().getClass()).add((MapBlock) mapdata);
		}
	}

	public void remove(Mapdata mapdata) {
		int layer = mapdata.getResource().getLayerUp() + (mapdata.getResource().isGround() ? 0 : 2);
		
		int x = mapdata.getLocation().getX()-hitbox.getX();
		int y = mapdata.getLocation().getY()-hitbox.getY();
		
		this.data[x][y][layer] = null;
		if(mapdata.getResource().hasData() && mapdata instanceof MapBlock){
			if(BlockData.containsKey(mapdata.getResource().getBlockData().getClass())){
				ArrayList<MapBlock> blockdatas = BlockData.get(mapdata.getResource().getBlockData().getClass());
				if(blockdatas.size()==1)BlockData.remove(mapdata.getResource().getBlockData().getClass());
				else blockdatas.remove((MapBlock)mapdata);
			}
		}
	}

	public void hide() {
		for(int x = 0; x<hitbox.getWidth(); x++){
			for(int y = 0; y<hitbox.getHeigth(); y++){
				for(int i = 0; i<data.length; i++){
					if(this.data[x][y][i]!=null)this.data[x][y][i].hide();
				}
			}
		}
	}

	public void show() {
		for(int x = 0; x<hitbox.getWidth(); x++){
			for(int y = 0; y<hitbox.getHeigth(); y++){
				for(int i = 0; i<data.length; i++){
					if(this.data[x][y][i]!=null)this.data[x][y][i].show();
				}
			}
		}
	}

	public boolean hasType(Class<?> blockDataType) {
		return BlockData.containsKey(blockDataType);
	}

	public ArrayList<MapBlock> getBlockType(Class<?> blockDataType) {
		return BlockData.get(blockDataType);
	}

	public Mapdata[][][] getMapData() {
		return data;
	}

	public Mapdata[] getMapData(int x, int y) {
		return this.data[x-hitbox.getX()][y-hitbox.getY()];
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public int[][][] getInfo() {
		return info;
	}
	
	
}
