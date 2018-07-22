package game.menu;

import Data.Events.Action;
import Data.Image.Image;

public class Button {
	
	private Action action;
	private Image image;
	private boolean active = false;
	
	public Button(Action action, Image image){
		this.setAction(action);
		this.setImage(image);
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
