package data;

import sprites.Sprites;

import java.awt.Color;

import Data.Hitbox;
import Data.Location;
import Data.Image.SpriteSheet;
import anim.AnimationType;
import data.map.*;
import game.inventory.drops.Drops;
import game.inventory.drops.Drop;
import game.inventory.items.ItemType;
import game.map.Map;

public class MapResource extends Resource {
	public static MapResource Air_Background;
	public static MapResource Dirt_Background;
	public static MapResource Dirt_Grass_Background;
	
	public static MapResource Dirt;
	public static MapResource Dirt_Grass;
	public static MapResource Stone;
	public static MapResource Iron_Ore;
	public static MapResource Gold_Ore;
	public static MapResource Silver_Ore;
	public static MapResource Coal;
	
	public static MapResource Lamp;
	
	private static MapResource[] res;
	
	public static MapResource create(){		
		//<--- Background --->
		Air_Background 		 	= new MapResource(1101, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, null,
										true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										null,
										0);
		Dirt_Background		 	= new MapResource(1102, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, null,
										true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										null,
										1);
		
		Dirt_Grass_Background	= new MapResource(1103, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new Grass("Grass"),
										true , Sprites.Ground.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										null,
										10, 11, 12, 13, 14, 15, 16, 17, 18);
		
		//<--- Build --->
		Dirt					= new MapResource(1201, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Dirt"			, new Color( 21, 12,  3), new Color( 33, 19,  7), new Color( 51, 25,  7), new Color( 63, 36, 17)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Dirt		, 1, 3)),
										0);
		
		Dirt_Grass				= new MapResource(1202, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new Grass("Grass"),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Dirt		, 1, 3)),
										10, 11, 12, 13, 14, 15, 16, 17, 18);
		
		Stone					= new MapResource(1203, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Stone"		, new Color( 30, 30, 30), new Color( 40, 40, 40), new Color( 50, 50, 50), new Color( 60, 60, 60)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Stone		, 1, 3)),
										1);	
		
		Iron_Ore				= new MapResource(1204, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Iron_Ore"		, new Color( 30, 30, 30), new Color( 40, 40, 40), new Color( 50, 50, 50), new Color( 60, 60, 60)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Iron_Ore	, 1, 2)),
										20);		
		
		Gold_Ore				= new MapResource(1205, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Gold_Ore"		, new Color( 30, 30, 30), new Color( 40, 40, 40), new Color( 50, 50, 50), new Color( 60, 60, 60)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE 	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Gold_Ore	, 1, 2)),
										21);
		
		Silver_Ore				= new MapResource(1206, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Silver_Ore"	, new Color( 30, 30, 30), new Color( 40, 40, 40), new Color( 50, 50, 50), new Color( 60, 60, 60)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Silver_Ore	, 1, 2)),
										22);	
		
		Coal					= new MapResource(1207, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), true, true, 100, new EdgeBlockData("Coal"			, new Color( 30, 30, 30), new Color( 40, 40, 40), new Color( 50, 50, 50), new Color( 60, 60, 60)),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Coal		, 1, 3)),
										23);	
		
		//<--- Only crafting --->
		Lamp					= new MapResource(1301, new Hitbox(0, 0, Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE),false,false, 100, new Lamp("Lamp", 10, 0.9),
										false, Sprites.Blocks.getSpriteSheet(), AnimationType.NONE	, 0,  new ResourcePart[]{},
										new Drops(new Drop(ItemType.Stone		, 3, 3), new Drop(ItemType.Coal		, 1, 1)),
										30);	

		
		res = new MapResource[11];
		res[ 0] = Lamp;
		
		res[ 1] = Air_Background;
		res[ 2] = Dirt_Background;
		res[ 3] = Dirt_Grass_Background;
		
		res[ 4] = Dirt;
		res[ 5] = Dirt_Grass;

		res[ 6] = Stone;
		res[ 7] = Iron_Ore;
		res[ 8] = Gold_Ore;
		res[ 9] = Silver_Ore;
		res[10] = Coal;
		
		for(int i = 0; i<res.length; i++){
			if(res[i].hasData())res[i].getBlockData().loadData(res[i]);
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
	private Drops drops;
	private boolean solid;
	
	private MapResource(int id, Hitbox hitbox, boolean solid, boolean opaque, int hp, BlockData blockData, boolean ground, SpriteSheet sprites, AnimationType animType, int layerUp, ResourcePart[] resParts, Drops drops, int... spriteIDs){
		super(id, animType, sprites, spriteIDs, resParts, hitbox, opaque, hp);
		this.layerUp = layerUp;
		this.ground = ground;
		this.blockData = blockData;
		this.drops = drops;
		this.solid = solid;
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

	public void drop(Location loc) {
		this.drops.drop(loc);
	}

	public boolean hasDrops() {
		return drops!=null;
	}
	

	public boolean isSolid() {
		return solid;
	}
	
}
