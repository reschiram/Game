package game.vehicle;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import data.VehicleResource;
import game.map.Map;

public class VehiclePart {
	
	private Image image;	
	private int HP;
	private VehicleResource vehicleResource;
	private boolean connected;

	public VehiclePart(VehicleResource vehicleResource, Location location){
		this.vehicleResource = vehicleResource;
		this.HP = vehicleResource.getData().getHP();
		this.image = new Image(location, new Dimension(vehicleResource.getSize().width*Map.DEFAULT_SQUARESIZE, vehicleResource.getSize().height*Map.DEFAULT_SQUARESIZE), "", vehicleResource.getSprites(), null);
		this.image.setSpriteState(this.vehicleResource.getSpriteIDs()[0]);
	}
	
	
	
	public boolean isDead(){
		return this.HP==0;
	}
	
	public int getHP(){
		return this.HP;
	}
	
	public int getMaxHP(){
		return vehicleResource.getData().getHP();
	}
	
	public void damage(int amount){
		this.HP-=amount;
		if(this.HP<0)this.HP=0;
	}
	
	public void heal(int amount){
		this.HP+=amount;
		if(this.HP>vehicleResource.getData().getHP())this.HP = vehicleResource.getData().getHP();
	}

	public Image getImage() {
		return image;
	}

	public VehicleResource getVehicleResource() {
		return vehicleResource;
	}

	public boolean isConnected() {
		return connected;
	}

	public VehiclePart setConnected(boolean connected) {
		this.connected = connected;
		return this;
	}
}
