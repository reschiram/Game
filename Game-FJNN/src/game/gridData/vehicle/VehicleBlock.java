package game.gridData.vehicle;

import java.util.ArrayList;

import Data.Location;
import Data.Animation.AnimationManager;
import Engine.Engine;
import data.Resource;
import data.ResourcePart;
import game.map.Map;

public class VehicleBlock extends VehicleData{
	
	public ArrayList<VehicleDummieBlock> blockParts = new ArrayList<>();
	
	public VehicleBlock(Resource resource, int layer, Location loc){
		super(resource, layer,loc);
	}
	
	public VehicleData[] create(){
		VehicleData[] mapdatas = new VehicleData[res.getResourceParts().length+1];
		mapdatas[0] = this;
		for(int i = 0; i<res.getResourceParts().length; i++){
			ResourcePart part = res.getResourceParts()[i];
			mapdatas[i+1] = new VehicleDummieBlock(this, part);
			blockParts.add((VehicleDummieBlock) mapdatas[i+1]);
		}
		return mapdatas;
	}
	
	@Override
	public VehicleData show(){	
		if(!created){
			Engine.getEngine(this, this.getClass()).addImage(image, layer);
			created = true;
		}else image.disabled = false;
		if(anim!=null)anim.start();
		for(VehicleDummieBlock part: blockParts){
			part.show();
		}
		return this;
	}

	@Override
	public void destroyVisual(){
		Engine.getEngine(this, this.getClass()).removeImage(layer, image);
		if(anim!=null){
			anim.stop();
			AnimationManager.remove(anim);
		}
		for(VehicleDummieBlock party: blockParts)party.destroyVisual();
	}

	@Override
	public void hide(){
		if(anim!=null)anim.stop();
		image.disabled = true;
		for(VehicleDummieBlock party: blockParts)party.hide();
	}

	@Override
	public void setLocation(Location loc){
		this.image.setLocation(loc);
		for(VehicleDummieBlock party: blockParts)party.setLocation(new Location(loc.x+party.getLocation().getX()*Map.DEFAULT_SQUARESIZE, loc.y+party.getLocation().getY()*Map.DEFAULT_SQUARESIZE));
	}
}
