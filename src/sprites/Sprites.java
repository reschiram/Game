package sprites;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import Data.Image.Image;
import Data.Image.SpriteSheet;
import game.map.Map;

public class Sprites {
	
	public static Sprites Ground;
	public static Sprites Blocks; 
	public static Sprites Vehicle; 
	
	public static Sprites Items; 
	public static Sprites Equipment;
	
	public static Sprites PlayerBody;
	public static Sprites PlayerHead;
	public static Sprites Player;
	
	public static Sprites Pointer;
	public static Sprites Marker;
	public static Sprites DamageLevel;
	
	public static Sprites FactoryMenu;
	public static Sprites FactoryFrame;
	public static Sprites FactoryScrollBar;
	public static Sprites FactoryScrollButton;
	public static Sprites FactoryScrollItem;
	public static Sprites Slot;
	public static Sprites Mouse;	
	public static Sprites Arrows;	
	public static Sprites Buttons; 
	public static Sprites Sidebar ;
	public static Sprites StatsMenu;
	public static Sprites Backgrounds;
	
	public static Sprites VehiclePartTypesIcons;
	
	public static Sprites MapGenerations;
	public static Sprites Edges;


	public static Sprites create() {
		
		Ground 		 			= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/ground.png"				, "ground"		, false);
		Blocks  				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/blocks.png"				, "blocks"		, false);  		
		Vehicle	  				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/vehicle.png"				, "vehicle"		, false);
		
		Items	  				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/blocks.png"				, "items"		, false); 
		Equipment  				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/blocks.png"				, "items"		, false);   
		
		PlayerHead 				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/playerhead.png"			, "player"		, false);
		PlayerBody				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/playerbody.png"			, "player"		, false);
		Player					= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, 50						), "/sprites/Player.png"				, "player"		, false);
		
		Pointer					= new Sprites(new Dimension(10						, 10						), new Dimension(8						, 8							), "/sprites/Pointer.png"				, "player"		, false);
		Marker					= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/Marker.png"				, "player"		, false);
		DamageLevel				= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/DestructionLevel.png"		, "map"			, false);
		
		Slot 					= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/slot.png"					, "slot"		, false);
		Mouse					= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), Image.createEmptyImage(40, 40)		, "mouse"		, false);
		Arrows					= new Sprites(new Dimension(30					 	, 30					  	), new Dimension(30					 	, 30					  	), "/sprites/arrows.png"				, "menu"		, false);
		Buttons 				= new Sprites(new Dimension(123					 	, 50					  	), new Dimension(123					, 50						), "/sprites/buttons.png"				, "menu"		, false);
		Sidebar 				= new Sprites(new Dimension(300					 	, 1049					  	), new Dimension(300					, 1049						), "/sprites/sidebar3.png"				, "menu"		, false);
		FactoryMenu				= new Sprites(new Dimension(294						, 394						), new Dimension(294					, 394						), "/sprites/FactoryMenu.png"			, "menu"		, false);
		FactoryFrame			= new Sprites(new Dimension(300						, 400						), new Dimension(300					, 400						), "/sprites/FactoryFrame.png"			, "menu"		, false);
		FactoryScrollBar		= new Sprites(new Dimension(4						, 230						), new Dimension(4						, 230						), "/sprites/FactoryScrollBar.png"		, "menu"		, false);
		FactoryScrollButton		= new Sprites(new Dimension(14						, 22						), new Dimension(14						, 22						), "/sprites/FactoryScrollButton.png"	, "menu"		, false);
		FactoryScrollItem		= new Sprites(new Dimension(264						, 31						), new Dimension(264					, 31						), "/sprites/FactoryScrollItem.png"		, "menu"		, false);
		StatsMenu				= new Sprites(new Dimension(200						, 400						), new Dimension(200					, 400						), "/sprites/StatsMenu.png"				, "menu"		, false);
		StatsMenu				= new Sprites(new Dimension(200						, 400						), new Dimension(200					, 400						), "/sprites/StatsMenu.png"				, "menu"		, false);
		Backgrounds				= new Sprites(new Dimension(1920					, 1080						), new Dimension(1920					, 1080						), "/sprites/Backgrounds.png"			, "background"	, false);
		
		VehiclePartTypesIcons	= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/VehiclePartTypesIcons.png"	, "Icon"		, false);
		
		MapGenerations			= new Sprites(new Dimension(20						, 21						), new Dimension(20						, 21						), "/sprites/MapGeneration.png"			, "map"			, false);
		Edges					= new Sprites(new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), new Dimension(Map.DEFAULT_SQUARESIZE	, Map.DEFAULT_SQUARESIZE	), "/sprites/Edges.png"					, "edges"		, false);
		return null;
	}
	
	private SpriteSheet spritesheet;
	private SpriteSheet shadowSpriteSheet;
	private Dimension size;
	private Dimension spriteSize;
	
	private Sprites(Dimension read, Dimension target, Object image, String name, boolean antialsing){
		this.spritesheet = new SpriteSheet(image, target, read, antialsing);
//		System.out.println(this.spritesheet.getSpriteAmount());
		if(image instanceof String){
			BufferedImage bi =Image.readImage((String)image, antialsing); 
			this.size = new Dimension(bi.getWidth(), bi.getHeight());
		}else if(image instanceof BufferedImage){
			this.size = new Dimension(((BufferedImage)image).getWidth(), ((BufferedImage)image).getHeight());
		}
		this.spriteSize = target; 
	}
	
	public SpriteSheet getSpriteSheet(){
		return spritesheet;	
	}
	
	public SpriteSheet getShadowSpriteSheet(){
		if(this.shadowSpriteSheet == null)this.shadowSpriteSheet = SpriteSheet.createShadowSpriteSheet(spritesheet);
		return shadowSpriteSheet;
	}

	public Dimension getDimension() {
		return size;
	}
	
	public Dimension getSpriteDimension(){
		return spriteSize;
	}
}
