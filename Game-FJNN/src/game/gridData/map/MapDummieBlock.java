package game.gridData.map;

import Data.Location;
import Data.Image.Image;
import data.ResourcePart;

public class MapDummieBlock extends Mapdata{

	private MapBlock block;
	private ResourcePart part;
	
	public MapDummieBlock(MapBlock block, ResourcePart part) {
		super(block.getResource(), block.layer, new Location(block.getLocation().x+part.getLocation().x, block.getLocation().y+part.getLocation().y));
//		System.out.println(layer);
		this.block = block;
		this.part = part;
		this.image = new Image(image.getLocation(), MapBlock.blockDimension, "", part.getSprites().getSpriteSheet(), null);
		this.image.setSpriteState(part.getSpriteIDs()[0]);
		if(block.getResource().hasAnimation()){
			this.anim = block.getResource().getAnimationType().newAnimation(false, layer, image, block.getResource());
		}
		this.loadImage();
	}
	
	public MapBlock getBlock() {
		return block;
	}

	public ResourcePart getResourcePart() {
		return part;
	}
	
	@Override
	public int getDefaultSpriteState(){
		return part.getSpriteIDs()[0];
	}

}
