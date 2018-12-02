package game.inventory.equipment.tools.menu;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import sprites.Sprites;

public abstract class ToolMenu {

	protected static int Defaut_Layer = 6;
	
	protected Image Background;
	
	public ToolMenu(Location location, Dimension size) {
		Background = new Image(location, size, "", Sprites.Backgrounds.getSpriteSheet(), null);
		Background.setSpriteState(1);
	}
	
	public void hide(){
		Background.disabled = true;
	}
	
	public void show(){
		Background.disabled = false;
	}
	
	public void createVisuals(){
		Background.disabled = false;
		Engine.getEngine(this, this.getClass()).addImage(Background, Defaut_Layer);
	}
	
	public void destroyVisuals(){
		Background.disabled = true;
		Engine.getEngine(this, this.getClass()).removeImage(Defaut_Layer, Background);
	}

	public boolean isVisible() {
		return !Background.disabled;
	}
	
	public abstract void tick();

}
