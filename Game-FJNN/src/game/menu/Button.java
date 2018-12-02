package game.menu;

import java.awt.event.MouseEvent;

import Data.Location;
import Data.Events.Action;
import Data.Image.Image;
import Engine.Engine;
import data.ButtonTrigger;

public class Button {
	
	private Action action;
	private Image[] images;
	private int layer;
	private boolean visible = false;
	private boolean active = false;
	
	public Button(Action action, int layer, Image... images){
		this.images = images;
		this.layer = layer;
		this.action = action;
	}
	
	public void tick(){
		if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1)){
			for(int i = 0; i<images.length; i++){
				if(images[i].getHitbox().contains(Engine.getInputManager().MousePosition()))this.action.act(this);
			}
		}
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setImage(int i, Image image, boolean show){
		removeImage(i);
		this.images[i] = image;
		if(show)addImage(i);
	}
	
	private void addImage(int i) {
		Image image = this.images[i];
		if(image!=null){
			image.disabled = false;
			Engine.getEngine(this, this.getClass()).addImage(image, layer);
		}
		visible = true;
	}

	public void removeImage(int i) {
		Image image = this.images[i];
		if(image!=null){
			image.disabled = true;
			Engine.getEngine(this, this.getClass()).removeImage(layer, image);
			this.images[i] = null;
		}
		if(!hasVisibleImage())this.visible = false;
	}
	
	private boolean hasVisibleImage() {
		for(int i = 0; i<this.images.length; i++){
			Image image = this.images[i];
			if(image!=null && !image.disabled){
				return true;
			}
		}
		return false;
	}

	public void createVisuals(){
		for(int i = 0; i<this.images.length; i++)addImage(i);
		this.visible = true;
	}
	
	public void destroyVisuals(){
		for(int i = 0; i<this.images.length; i++){
			Image image = this.images[i];
			if(image!=null){
				image.disabled = true;
				Engine.getEngine(this, this.getClass()).removeImage(layer, image);
			}
		}
		this.visible = false;
	}
	
	public void hide(){
		for(int i = 0; i<this.images.length; i++){
			Image image = this.images[i];
			if(image!=null){
				image.disabled = true;
			}
		}
		this.visible = false;
	}
	
	public void show(){
		for(int i = 0; i<this.images.length; i++){
			Image image = this.images[i];
			if(image!=null){
				image.disabled = false;
			}
		}
		this.visible = true;
	}

	public int getImageAmount(){
		return images.length;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Image getImage(int i) {
		return this.images[i];
	}

	public boolean isVisible(){
		return visible;
	}

	public Location getLocation() {
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		for(int i = 0; i<this.images.length; i++){
			x = Math.min(x, this.images[i].getX());
			y = Math.min(y, this.images[i].getY());
		}
		return new Location(x, y);
	}
	
	public void setLocation(Location new_loc) {
		Location loc = getLocation();
		int dx = new_loc.getX()-loc.getX();
		int dy = new_loc.getY()-loc.getY();
		for(int i = 0; i<this.images.length; i++){
			this.images[i].setLocation(this.images[i].getX()+dx, this.images[i].getY()+dy);
		}
	}
}
