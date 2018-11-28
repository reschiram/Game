package game.map;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import Data.Hitbox;
import Data.Location;
import game.gridData.map.MapBlock;
import game.gridData.map.Mapdata;

public class MapChunk {
	
	private Hitbox hitbox;

	private Mapdata[][][] ground;
	private Mapdata[][][] build;
	
	private Mapdata[][][] data;
	
	private HashMap<Class<?>, ArrayList<MapBlock>> BlockData = new HashMap<>();
	
	public MapChunk(Location location, Dimension dimension){
		create(location.x, location.y, dimension.width, dimension.height);
	}
	
	public MapChunk(int x, int y, int w, int h){
		create(x, y, w, h);
	}
	
	private void create(int x, int y, int w, int h){
		this.hitbox = new Hitbox(x, y, w, h);
		this.ground  = new Mapdata [w][h][2];
		this.build   = new Mapdata [w][h][2];		
		this.data	 = new Mapdata [w][h][4];
	}
	
	public Dimension getDimension(){
		return this.hitbox.getDimension();
	}
	
	public Mapdata[][][] getGround() {
		return ground;
	}	

	public Mapdata[][][] getBuild() {
		return build;
	}

	public void set(Mapdata mapdata) {
		Mapdata[][][] data = this.build;
		if(mapdata.getResource().isGround()){
			data = this.ground;
			this.data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()] = mapdata;
		}else this.data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()+this.ground[0][0].length] = mapdata;
		data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()] = mapdata;
		if(mapdata.getResource().hasData() && mapdata instanceof MapBlock){
			if(!BlockData.containsKey(mapdata.getResource().getBlockData().getClass()))BlockData.put(mapdata.getResource().getBlockData().getClass(), new ArrayList<>());
//			System.out.println(mapdata.getResource().getBlockData().getClass());
			BlockData.get(mapdata.getResource().getBlockData().getClass()).add((MapBlock) mapdata);
		}
	}

	public Mapdata[] getMapData(Location location, boolean isGround) {
		return getMapData(location.x, location.y, isGround);
	}

	public void remove(Mapdata mapdata) {
		Mapdata[][][] data = this.build;
		if(mapdata.getResource().isGround()){
			data = this.ground;
			this.data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()] = null;
		}else this.data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()+this.ground[0][0].length] = null;
		data[mapdata.getLocation().getX()-hitbox.getX()][mapdata.getLocation().getY()-hitbox.getY()][mapdata.getResource().getLayerUp()] = null;
		if(mapdata.getResource().hasData() && mapdata instanceof MapBlock){
			if(BlockData.containsKey(mapdata.getResource().getBlockData().getClass())){
				ArrayList<MapBlock> blockdatas = BlockData.get(mapdata.getResource().getBlockData().getClass());
				if(blockdatas.size()==1)BlockData.remove(mapdata.getResource().getBlockData().getClass());
				else blockdatas.remove((MapBlock)mapdata);
			}
		}
	}

	public Mapdata[] getMapData(int x, int y, boolean isGround) {
//		System.out.println(x+"|"+y+" -> "+hitbox.getLocation().toString());
		if(isGround) return this.ground[x-hitbox.getX()][y-hitbox.getY()];
		return this.build[x-hitbox.getX()][y-hitbox.getY()];
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
	
	
}
