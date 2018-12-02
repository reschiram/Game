package game.gridData.map;

import java.util.ArrayList;

import Data.Location;
import data.MapResource;
import data.ResourcePart;
import game.map.Map;

public class MapBlock extends Mapdata{
	
	public ArrayList<MapDummieBlock> blockParts = new ArrayList<>();
	
	public MapBlock(MapResource resource, int layer, Location loc){
		super(resource, layer,loc);
		this.loadImage();
	}
	
	public Mapdata[] create(){
		Mapdata[] mapdatas = new Mapdata[res.getResourceParts().length+1];
		mapdatas[0] = this;
		for(int i = 0; i<res.getResourceParts().length; i++){
			ResourcePart part = res.getResourceParts()[i];
			mapdatas[i+1] = new MapDummieBlock(this, part);
			blockParts.add((MapDummieBlock) mapdatas[i+1]);
		}
		return mapdatas;
	}
	
	@Override
	public Mapdata show(){	
		super.show();
		for(MapDummieBlock part: blockParts){
			part.show();
		}
		return this;
	}

	@Override
	public void destroyVisual(){
		super.destroyVisual();
		for(MapDummieBlock party: blockParts)party.destroyVisual();
	}

	@Override
	public void hide(){
		super.hide();
		for(MapDummieBlock party: blockParts)party.hide();
	}

	@Override
	public void setLocation(Location loc){
		super.setLocation(loc);
		for(MapDummieBlock party: blockParts)party.setLocation(new Location(loc.x+party.getLocation().getX()*Map.DEFAULT_SQUARESIZE, loc.y+party.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
	}
}
