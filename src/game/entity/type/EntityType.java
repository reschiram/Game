package game.entity.type;

import java.awt.Dimension;

import Data.Animation.Animation;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import anim.AnimationType;
import data.LightSpriteSheet;
import game.entity.type.data.EntityData;
import game.entity.type.data.EntityInventoryData;
import game.entity.type.data.LightEntityData;
import game.map.Map;
import sprites.Sprites;

public class EntityType {
	
	public static EntityType LightEntity;
	public static EntityType Entity;
	public static EntityType ItemEntity;
	public static EntityType Player;
	public static EntityType Drone;
	
	public static void create(){
		Entity		 	= new EntityType( 0, null																				 , null																	,
								null				, new Integer[]{0});
		LightEntity	 	= new EntityType( 0, null																				 , null																	,
								null				, new Integer[]{0});
		Drone 			= new EntityType( 5, new Dimension(20								, 20								), LightSpriteSheet.getLightSpriteSheet(Sprites.Player.getSpriteSheet()),
								AnimationType.NONE	, new Integer[]{0}, new LightEntityData(10, 0.8), new EntityInventoryData( 9, 0));
		Player		 	= new EntityType( 4, new Dimension(38, 38), LightSpriteSheet.getLightSpriteSheet(Sprites.Player.getSpriteSheet()),
								AnimationType.NONE	, new Integer[]{0}, new LightEntityData(3, 0.8), new EntityInventoryData(24, 4));
		ItemEntity 		= new EntityType(10, new Dimension((int)(Map.DEFAULT_SQUARESIZE*0.5), (int)(Map.DEFAULT_SQUARESIZE*0.5)	), null																	,
								AnimationType.ITEM	, new Integer[]{0});
	}
	
	private int speed;
	private Dimension size;
	private SpriteSheet sheet;
	private AnimationType animType;
	private Integer[] spriteIds;
	private EntityData[] datas;	
	
	public EntityType(int speed, Dimension size, SpriteSheet sheet, AnimationType animType, Integer[] spriteIds, EntityData... datas){
		this.speed = speed;
		this.size = size;
		this.sheet = sheet;
		this.animType = animType;
		this.spriteIds = spriteIds;
		this.datas  = datas;
	}

	public int getSpeed() {
		return speed;
	}

	public Dimension getSize() {
		return size;
	}

	public Animation createAnimation(boolean newImage, int layer, Image image) {
		return animType.newAnimation(newImage, layer, image, spriteIds);
	}	

	public SpriteSheet getSpriteSheet() {
		return sheet;
	}

	public Integer[] getSpriteIds() {
		return spriteIds;
	}
	
	public boolean hasData(int dataCode){
		for(EntityData data: datas){
			if(data.getCode() == dataCode) return true;
		}
		return false;
	}
	
	public EntityData getData(int dataCode){
		for(EntityData data: datas){
			if(data.getCode() == dataCode) return data;
		}
		return null;
	}
	
}
