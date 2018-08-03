package data;

import sprites.Sprites;

import java.awt.Color;
import java.awt.Dimension;

import Data.Hitbox;
import Data.Image.SpriteSheet;
import anim.AnimationType;
import data.map.*;
import game.inventory.items.ItemType;
import game.map.Map;

public class MapResource extends Resource {
	public static MapResource Air_Background;
	public static MapResource Dirt_Background;
	public static MapResource Dirt_Grass_Background;
	
	public static MapResource Dirt;
	public static MapResource Dirt_Grass;
	public static MapResource Stone;
	
	public static MapResource Lamp;
	
	private static MapResource[] res;
	
	public static MapResource create(){		
		Air_Background 		 = new MapResource(1101, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), null																													, true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, null			, 0,  0);
		Dirt_Background		 = new MapResource(1102, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), null 																													, true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, null			, 0,  1);
		Dirt_Grass_Background= new MapResource(1103, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), new Grass("Grass")																										, true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, null			, 0, 10, 11, 12, 13, 14, 15, 16, 17, 18);
		
		Dirt				 = new MapResource(1201, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), new EdgeBlockData("Dirt", new Color(17, 8, 2), new Color(33, 19, 7), new Color(51, 25, 7), new Color(63, 36, 17)) 	    , false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, ItemType.Dirt	, 3,  0);
		Stone				 = new MapResource(1202, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), null 																													, false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, ItemType.Stone	, 1,  1);		
		Dirt_Grass			 = new MapResource(1203, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), new Grass("Grass")																										, false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{}, ItemType.Dirt	, 3,  10, 11, 12, 13, 14, 15, 16, 17, 18);
		
//		Water 		= new MapResource(1107, null 				 		, true , Sprites.Ground1,AnimationType.WATER	, 0,  new ResourcePart[]{}														, 60, 61, 62, 63, 64, 65, 66, 67);	
//		
//		Tree1		= new MapResource(1201, null 				 		, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20101, 21),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20102, 40),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20103, 41),}		, 20);	
//		Tree2		= new MapResource(1202, null 				 		, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20201, 23),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20202, 42),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20203, 43),}		, 22);	
//		Tree3		= new MapResource(1203, null 				 		, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20301, 25),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20302, 43),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20303, 45),}		, 24);	
//		Tree4		= new MapResource(1204, null 				 		, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20401, 27),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20402, 46),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20403, 47),}		, 26);	
//		Tree5		= new MapResource(1205, null 						, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20501, 29),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20502, 48),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20503, 49),}		, 28);	
//		Tree6		= new MapResource(1206, null 				 		, true , Sprites.Ground1,AnimationType.NONE		, 1,  new ResourcePart[]{new ResourcePart(new Location( 1,  0), Sprites.Ground1	, 20601, 31),
//																														new ResourcePart(new Location(	0,  1), Sprites.Ground1	, 20602, 50),
//																														new ResourcePart(new Location(	1,  1), Sprites.Ground1	, 20603, 51),}		, 30);	
//		Flower		= new MapResource(1207, null				 		, true , Sprites.Ground1,AnimationType.NONE 	, 1,  new ResourcePart[]{}														, 19);	
//	
//		Factory		= new MapResource(1301, new Factory("Factory")		, false, Sprites.Build	,AnimationType.NONE		, 0,  new ResourcePart[]{new ResourcePart(new Location( 1, 0), Sprites.Build	, 30101,  1),
//																														new ResourcePart(new Location(  2, 0), Sprites.Build	, 30102,  2),
//																														new ResourcePart(new Location(  0, 1), Sprites.Build	, 30103, 10),
//																														new ResourcePart(new Location(  1, 1), Sprites.Build	, 30104, 11),
//																														new ResourcePart(new Location(  2, 1), Sprites.Build	, 30105, 12),
//																														new ResourcePart(new Location(  0, 2), Sprites.Build	, 30106, 20),
//																														new ResourcePart(new Location(  1, 2), Sprites.Build	, 30107, 21),
//																														new ResourcePart(new Location(  2, 2), Sprites.Build	, 30108, 22),}		,  0);	
		
		Lamp		= new MapResource(1301, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), new Lamp("Lamp", 10, 1.0)																										, false, Sprites.Blocks.getSpriteSheet(),AnimationType.NONE 	, 0,  new ResourcePart[]{}, null			, 0,  0);	
		
		res = new MapResource[7];
		res[0] = Lamp;
		
		res[1] = Air_Background;
		res[2] = Dirt_Background;
		res[3] = Dirt_Grass_Background;
		
		res[4] = Dirt;
		res[5] = Dirt_Grass;
		res[6] = Stone;
		
		for(int i = 0; i<res.length; i++){
			if(res[i].hasData())res[i].getBlockData().load(res[i]);
		}
		return null;
	} 	

	public static MapResource getMapResource(int res) {
		for(MapResource r: MapResource.res){
			if(r.getID()==res)return r;
		}
		return null;
	}
	
	private int layerUp;
	private boolean ground; 
	private BlockData blockData;
	private ItemType itemType;
	private int itemAmount;
	
	private MapResource(int id, Hitbox hitbox, BlockData blockData, boolean ground, SpriteSheet sprites, AnimationType animType, int layerUp, ResourcePart[] resParts, ItemType type, int itemAmount, int... spriteIDs){
		super(id, animType, sprites, spriteIDs, resParts, hitbox);
		this.layerUp = layerUp;
		this.ground = ground;
		this.blockData = blockData;
		this.itemType = type;
		this.itemAmount = itemAmount;
	}
	
	public boolean isGround(){
		return ground;
	}
	
	public int getLayerUp(){
		return layerUp;
	}

	public static MapResource[] getMapResource() {
		return MapResource.res;
	}

	public boolean hasData() {
		return blockData!=null;
	}
	
	public BlockData getBlockData(){
		return this.blockData;
	}

	public void setSprites(SpriteSheet sheet, int[] ids) {
		this.sprites = sheet;
		this.spriteIds = ids;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public boolean hasItemType() {
		return itemType!=null;
	}
	
	public int getItemAmount(){
		return itemAmount;
	}
	
}