package data.map;

import data.MapResource;
import game.gridData.map.Mapdata;

public abstract class BlockData {
	
	private String name;

	public BlockData(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void loadData(MapResource res){
		
	}
	
	public void load(Mapdata mapdata){
		
	}

}
