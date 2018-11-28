package game.gridData.vehicle;

import Data.Location;
import Data.Image.Image;
import data.ResourcePart;

public class VehicleDummieBlock extends VehicleData{

	private VehicleBlock block;
	private ResourcePart part;
	
	public VehicleDummieBlock(VehicleBlock block, ResourcePart part) {
		super(block.getResource(), block.layer, new Location(block.getLocation().x+part.getLocation().x, block.getLocation().y+part.getLocation().y));
//		System.out.println(layer);
		this.block = block;
		this.part = part;
		this.image = new Image(image.getLocation(), blockDimension, "", part.getSprites().getSpriteSheet(), null);
		this.image.setSpriteState(part.getSpriteIDs()[0]);
		if(block.getResource().hasAnimation()){
			this.anim = block.getResource().getAnimationType().newAnimation(false, layer, image, block.getResource());
		}
	}
	
	public VehicleBlock getBlock() {
		return block;
	}

	public ResourcePart getResourcePart() {
		return part;
	}

}
