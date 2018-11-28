package data;

import Data.Location;
import Data.Image.Image;

public class ImageData {
	
	private Location location;
	private Image image;
	
	public ImageData(Location loc, Image image){
		this.location = loc;
		this.image = image;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
