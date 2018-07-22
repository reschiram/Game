package data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import Data.Image.Image;
import Data.Image.Sprite;
import Data.Image.SpriteSheet;
import data.map.Lamp;

public class LightSpriteSheet extends SpriteSheet{
	
	private static HashMap<SpriteSheet, LightSpriteSheet> TRANSFORMATIONS = new HashMap<>();
	private static BufferedImage emptyImage = Image.createEmptyTransperentImage(1,1);
	
	private LightSpriteSheet(SpriteSheet sheet){
		super(null, sheet.getSpriteSize(), null, sheet.isAntianalising());
		this.sprites = new Sprite[sheet.getSprites().length][sheet.getSprites()[0].length];
		
		for(int x = 0; x<sprites.length; x++){
			for(int y = 0; y<sprites[x].length; y++){
				this.sprites[x][y] = sheet.getSprites()[x][y].clone();
				BufferedImage originSprite = this.sprites[x][y].getImage(0);
				
				for(int i = 1; i<Lamp.DEFAULT_LIGHT_STATES; i++){
					BufferedImage bi = Image.cloneImage(this.sprites[x][y].getImage(0));
					double alpha = (Math.log((i)+1)/Math.log(Lamp.DEFAULT_LIGHT_STATES+1))-(Math.exp(-(i))/Math.exp(Lamp.DEFAULT_LIGHT_STATES));
//					System.out.println("ALPHA->"+alpha);
					if(i == Lamp.DEFAULT_LIGHT_STATES-1)alpha = 1.0;
					
					Color c = new Color(0, 0, 0, (int) (alpha*255));//(int)((((double)i)/((double)(Lamp.DEFAULT_LIGHT_STATES-1.0)))*255));
					emptyImage.setRGB(0, 0, c.getRGB());
					Graphics2D g = bi.createGraphics();
					
					for(int dx= 0; dx<originSprite.getWidth(); dx++){
						for(int dy = 0; dy<originSprite.getHeight(); dy++){
							int RGB = originSprite.getRGB(dx, dy);
							if(((RGB >> 24) & 0xff) != 0){
//								System.out.println(px+"|"+py);
								g.drawImage(emptyImage, dx, dy, null);
							}
						}
					}
					
					this.sprites[x][y].addImage(bi);
				}
			}
		}
	}
	
	public static LightSpriteSheet getLightSpriteSheet(SpriteSheet sheet){
		if(!TRANSFORMATIONS.containsKey(sheet)){
			TRANSFORMATIONS.put(sheet, new LightSpriteSheet(sheet));
		}
		return TRANSFORMATIONS.get(sheet);
	}

}
