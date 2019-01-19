package game.menu;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.RotationSprites;
import sprites.Sprites;
import tick.TickManager;

public class ScrollBar {	
	
	private static int BarWidth = 8;
	private static int ButtonLength = 30;
	private static double ButtonSize = 0.5;
	private static int MaxScroll = 1000;
	
	
	private Image Bar;
	private Image ScrollButton;
	private boolean visible;
	private int layer;

	private Location startLocation;
	private int scrolled;
	private int scrollSpeed;
	private int move_x;
	private int move_y;
	
	
	public ScrollBar(Location loc, int length, int orientation, int scrollSpeed, int layer){
		this.layer = layer;
		if(orientation == RotationSprites.Orientation_LEFT || orientation == RotationSprites.Orientation_RIGHT){
			this.Bar			= new Image(loc											, new Dimension(length		, BarWidth					 	 ), "", RotationSprites.RotateSpriteSheet(Sprites.FactoryScrollBar		.getSpriteSheet(), RotationSprites.Orientation_LEFT), null);
			this.ScrollButton 	= new Image(new Location(loc.getX()-2, loc.getY()-2)	, new Dimension(ButtonLength, (int) (ButtonLength*ButtonSize)), "", RotationSprites.RotateSpriteSheet(Sprites.FactoryScrollButton	.getSpriteSheet(), RotationSprites.Orientation_LEFT), null);
			this.move_x = 1;
		}else if(orientation == RotationSprites.Orientation_UP || orientation == RotationSprites.Orientation_DOWN){
			this.Bar			= new Image(loc											, new Dimension(BarWidth					   , length		 ), "", Sprites.FactoryScrollBar.getSpriteSheet()																			, null);
			this.ScrollButton 	= new Image(new Location(loc.getX()-2, loc.getY()+10)	, new Dimension((int) (ButtonLength*ButtonSize), ButtonLength), "", Sprites.FactoryScrollButton.getSpriteSheet()																		, null);
			this.move_y = 1;
		}
		this.startLocation = this.ScrollButton.getLocation().clone();
		this.scrollSpeed = scrollSpeed;
		
		System.out.println(loc);
	}
	
	public void createVisuals(){
		this.Bar		 .disabled=false;
		this.ScrollButton.disabled=false;
		Engine engine = Engine.getEngine(this, this.getClass());
		engine.addImage(this.Bar		 , this.layer);
		engine.addImage(this.ScrollButton, this.layer);
		this.visible = true;
	}
	
	public void destroyVisuals(){
		this.Bar		 .disabled=true;
		this.ScrollButton.disabled=true;
		Engine engine = Engine.getEngine(this, this.getClass());
		engine.removeImage(this.layer, this.Bar			);
		engine.removeImage(this.layer, this.ScrollButton);
		this.visible = false;
	}
	
	public void show(){
		this.Bar		 .disabled=false;
		this.ScrollButton.disabled=false;
		this.visible = true;
	}
	
	public void hide(){
		this.Bar		 .disabled=true;
		this.ScrollButton.disabled=true;
		this.visible = false;
	}
	
	public boolean isVisble(){
		return visible;
	}
	
	public boolean tick(){
		int mouseWheelMove = Engine.getInputManager().getMouseWheelsMove((long) (TickManager.getTickDuration()*(TickManager.getLatency()+2)));
		if(mouseWheelMove!=0){
			this.scrolled = Math.max(Math.min(this.scrolled+mouseWheelMove*scrollSpeed, MaxScroll),0);
			this.ScrollButton.setLocation((int)	(this.startLocation.getX()-2+this.move_x*((double)this.scrolled/(double)MaxScroll)*(this.Bar.getWidth() -ButtonLength*1.5)),
										  (int)	(this.startLocation.getY()  +this.move_y*((double)this.scrolled/(double)MaxScroll)*(this.Bar.getHeigth()-ButtonLength*1.5)));
			return true;
		}
		return false;
	}

	public double getScrolled() {
		return (double)scrolled/(double)MaxScroll;
	}
}
