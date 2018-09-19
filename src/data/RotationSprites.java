package data;

import java.awt.image.BufferedImage;

import Data.Image.Image;
import Data.Image.Sprite;
import Data.Image.SpriteSheet;
import Data.Lists.ArrayList;

public class RotationSprites {

	
	public static int Orientation_LEFT =  1; 	
	public static int Orientation_RIGHT=  2;	
	public static int Orientation_DOWN =  3; 
	public static int Orientation_UP   =  4; 	
	
	private static class RotatedSpriteSheet{
		private SpriteSheet origin;
		private SpriteSheet rotated;
		private int direction;

		private RotatedSpriteSheet(SpriteSheet origin, SpriteSheet rotated, int direction) {
			this.origin = origin;
			this.rotated = rotated;
			this.direction = direction;
		}

		public SpriteSheet getOrigin() {
			return origin;
		}

		public SpriteSheet getRotated() {
			return rotated;
		}

		public int getDirection() {
			return direction;
		}
		
	}
	
	private static ArrayList<RotatedSpriteSheet> RotatedSpriteSheets = new ArrayList<>();
	
	public static SpriteSheet RotateSpriteSheet(SpriteSheet sheet, int orientation){
		for(int i = 0; i<RotatedSpriteSheets.size(); i++){
			RotatedSpriteSheet rotated = RotatedSpriteSheets.get(i);
			if(rotated.getOrigin().equals(sheet) && rotated.getDirection()==orientation)return rotated.getRotated();
		}
		
		Sprite[] sprites = new Sprite[sheet.getSpriteAmount()];
		for(int i = 0; i<sprites.length; i++){
			Sprite sprite = sheet.getSprite(i);
			if(sprite!=null){
				BufferedImage[] spriteStates = new BufferedImage[sprite.getImages().size()];
				for(int s = 0; s<spriteStates.length; s++){
					spriteStates[s] = Image.rotate(Image.cloneImage(sprite.getImage(s)), 90*orientation);
				}
				sprites[i] = new Sprite(false, spriteStates);
			}
		}
		SpriteSheet rotatedSheet = new SpriteSheet(sprites);
		RotatedSpriteSheets.add(new RotatedSpriteSheet(sheet, rotatedSheet, orientation));
		return rotatedSheet;
	}
}
