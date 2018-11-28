package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import Data.Hitbox;
import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Data.Image.Image;
import Engine.Engine;

public class ScrollItem<DataType>{
	
	private Image[] images;
	private StringRenderOperation nameGO;
	
	private boolean created = false;
	private int layer;
	private DataType obj; 
	
	public ScrollItem(DataType obj,Location location, String name, int layer, Image... images){
		this.layer = layer;
		this.obj = obj;
		this.images = images;
		this.nameGO = new StringRenderOperation(new Location(50+location.x,23+location.y), new Dimension(100, images[0].getHeigth()), name, new Font("TimesRoman", Font.PLAIN, 22), Color.WHITE);
	}

	public void hide() {
		if(created){
			for(Image image: images)image.disabled = true;
			nameGO.disabled = true;
		}
		deselect();
	}

	public void show() {
		if(created){
			for(Image image: images)image.disabled = false;
			nameGO.disabled = false;
		}else{
			for(Image image: images)Engine.getEngine(this, this.getClass()).addImage(image, layer);
			Engine.getEngine(this, this.getClass()).addGraphicOperation(nameGO, layer);
			this.created = true;
		}
	}
	
	public DataType getObjekt(){
		return obj;
	}
	
	public void setObjekt(DataType obj){
		this.obj = obj;
	}

	public boolean containsPoint(Point point) {
		return this.getHitbox().contains(point);
	}

	public Hitbox getHitbox() {
		if(this.images.length>0)return images[0].getHitbox();
		return this.nameGO.Hitbox;
	}

	public void deselect() {
		if(this.images.length>0 && this.images[0]!=null)this.images[0].setSpriteState(0);
	}

	public void select() {
		if(this.images.length>0 && this.images[0]!=null)this.images[0].setSpriteState(1);
	}

	public void move(int amount) {
		for(int i = 0; i<images.length; i++){
			images[i].setLocation(images[i].getX(), images[i].getY()+amount);
			nameGO.Hitbox.setY(nameGO.Hitbox.getY()+amount);
		}
	}

	public boolean isVisible() {
		return created && images[0].disabled==false;
	}

	public void setLocation(int x, int y) {
		nameGO.Hitbox.setLocation(x+50, y+23);		
		for(int i = 0; i<images.length; i++)images[i].setLocation(x, y);
	}
}
