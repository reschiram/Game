package data.map;

import data.MapResource;

public abstract class BlockData {
	
	private String name;

	public BlockData(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void load(MapResource resource){
		
	}

}
