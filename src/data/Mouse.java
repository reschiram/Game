package data;

import java.awt.Dimension;
import java.awt.Point;

import Data.Location;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import Engine.Engine;

public class Mouse {
	
	public static int XOff = 0;
	public static int YOff = 0;
	
	private Point p = new Point(0,0);
	private Image img;
	private int layer;
	
	private static Mouse mouse;
	public static Mouse getMouse(){
		return Mouse.mouse;
	}
	
	public Mouse(int layer){
		this.layer = layer;
		img = new Image(new Location(p), new Dimension(40,40), "", new SpriteSheet(Image.createEmptyImage(1, 1)), null);
		Engine.getEngine(this, this.getClass()).addImage(img, layer);
		Mouse.mouse = this;
	}
	
	public void tick(){
		Point ep = Engine.getInputManager().MousePosition();
		if(ep.x != img.getX() || ep.y != img.getY()){
//			Location l = img.getLocation().clone();
//			img.setLocation(ep.x, ep.y);
			Engine.getEngine(this, this.getClass()).updateImage(layer, img, new Location(ep.x, ep.y));
		}
	}
	
	public void setImage(SpriteSheet s, int id){
		img.setSpriteSheet(s);
		img.setSpriteState(id);
	}

	public Location getPosition() {
		return new Location(img.getX()+XOff, img.getY()+YOff);
	}

	public SpriteSheet getSpriteSheet() {
		return img.getSpriteSheet();
	}
	
	public int getSpriteState(){
		return img.getSpriteState();
	}

	public Location getOff() {
		int x =  (int) ((1920/2)-Engine.getInputManager().MousePosition().getX());
		int y =  (int) ((1080/2)-Engine.getInputManager().MousePosition().getY());
		return new Location(x, y);
	}
}
