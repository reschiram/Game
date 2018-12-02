package data;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Data.Image.Image;
import Data.Image.SpriteSheet;
import game.map.Map;

public class Functions {
	
	public static SpriteSheet FuseToOneSprite(MapResource res){
		int width = 0;
		int height = 0;
		for(int i = 0; i<res.getResourceParts().length; i++){
			if(width <res.getResourceParts()[i].getLocation().x)width  = res.getResourceParts()[i].getLocation().x;
			if(height<res.getResourceParts()[i].getLocation().y)height = res.getResourceParts()[i].getLocation().y; 
		}
		BufferedImage bi = Image.createEmptyImage((width+1)*Map.DEFAULT_SQUARESIZE, (height+1)*Map.DEFAULT_SQUARESIZE);
		Graphics2D g = bi.createGraphics();
		g.drawImage(res.getSprites().getSprite(res.getSpriteIDs()[0]).getImage(0), 0, 0, null);
		for(int i = 0; i<res.getResourceParts().length; i++){
			ResourcePart part = res.getResourceParts()[i];
			g.drawImage(part.getSprites().getSpriteSheet().getSprite(part.getSpriteIDs()[0]).getImage(0), part.getLocation().x*Map.DEFAULT_SQUARESIZE, part.getLocation().y*Map.DEFAULT_SQUARESIZE, null);
		}
		return new SpriteSheet(bi);
	}

}
