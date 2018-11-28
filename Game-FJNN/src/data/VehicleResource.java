package data;

import java.awt.Dimension;

import Data.Hitbox;
import Data.Image.SpriteSheet;
import anim.AnimationType;
import game.map.Map;
import game.vehicle.data.VehicleArmorData;
import game.vehicle.data.VehicleData;
import sprites.Sprites;

public class VehicleResource extends Resource{

	public static VehicleResource BASICARMOR; 
	public static VehicleResource ADVANCEDARMOR; 
	public static VehicleResource SUPERIORARMOR; 
	public static VehicleResource EPICARMOR; 
	
	private static VehicleResource[] res;
	
	public static MapResource create(){			

		BASICARMOR 		= new VehicleResource(2101, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, 100, Sprites.Vehicle.getSpriteSheet()	,AnimationType.NONE	, VehicleArmorData.BASICARMOR 	 , new ResourcePart[]{}														,   0);	
		ADVANCEDARMOR 	= new VehicleResource(2102, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, 100, Sprites.Vehicle.getSpriteSheet()	,AnimationType.NONE	, VehicleArmorData.ADVANCEDARMOR , new ResourcePart[]{}														,   1);	
		SUPERIORARMOR 	= new VehicleResource(2103, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, 100, Sprites.Vehicle.getSpriteSheet()	,AnimationType.NONE	, VehicleArmorData.SUPERIORARMOR , new ResourcePart[]{}														,  20);	
		EPICARMOR 		= new VehicleResource(2104, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, 100, Sprites.Vehicle.getSpriteSheet()	,AnimationType.NONE	, VehicleArmorData.EPICARMOR	 , new ResourcePart[]{}														,  21);	
		
		res = new VehicleResource[4];		
		res[0] = BASICARMOR;	
		res[1] = ADVANCEDARMOR;	
		res[2] = SUPERIORARMOR;	
		res[3] = EPICARMOR;
		return null;
	} 	
	
	public static VehicleResource[] getVehicleResources(){
		return res;
	}
	

	public static int getNONID() {
		return 2000;
	}
	
	private VehicleData partData;
	private Dimension size;
	
	public VehicleResource(int id, Hitbox hitbox, boolean opaque, int hp, SpriteSheet sprites, AnimationType animtype, VehicleData partData, ResourcePart[] resParts, int... spriteIDs) {
		super(id, animtype, sprites, spriteIDs, resParts, hitbox, opaque, hp);
		this.partData = partData;
		int width  = 1;
		int height = 1;
		for(ResourcePart resPart: resParts){
			if(resPart.getLocation().getX()>width ) width  = resPart.getLocation().getX();
			if(resPart.getLocation().getY()>height) height = resPart.getLocation().getY();
		}
		this.size = new Dimension(width, height);
	}
	
	public VehicleData getData(){
		return partData;
	}

	public Dimension getSize() {
		return size;
	}

	public static VehicleResource getVehicleResource(int partID) {
		for(VehicleResource res: VehicleResource.res){
			if(res.getID()==partID)return res;
		}
		return null;
	}


}
