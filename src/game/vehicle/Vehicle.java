package game.vehicle;

import java.awt.Point;

import Data.Location;
import Engine.Engine;
import data.VehicleResource;
import game.map.Map;

public class Vehicle {
	
	private VehiclePart[][][] vehicleParts;
	private Location location;
	private int layer;
	
	private Point startVehiclePart;
	
	public Vehicle(int width, int height, Location location, int layer){
		this.location = location;
		this.vehicleParts = new VehiclePart[width][height][1];
		this.layer = layer;
	}
	
	public VehiclePart getPart(int x, int y){
		return vehicleParts[x][y][0];
	}
	
	public void setVehiclePart(int x, int y, int partID){
		VehicleResource res = VehicleResource.getVehicleResource(partID);
		if(vehicleParts[x][y][0] == null){
			if(res==null) return;
			if(startVehiclePart==null)startVehiclePart = new Point(x, y);
			vehicleParts[x][y][0] = new VehiclePart(res, new Location(this.location.x+x*Map.DEFAULT_SQUARESIZE, this.location.y+y*Map.DEFAULT_SQUARESIZE)).setConnected(checkConnection(x,y));
			Engine.getEngine(this, this.getClass()).addImage(this.vehicleParts[x][y][0].getImage(), this.layer);
		}else{
			if(vehicleParts[x][y][0].getVehicleResource().equals(res))return;
			if(res!=null){
				vehicleParts[x][y][0].getImage().setSpriteSheet(res.getSprites());
				vehicleParts[x][y][0].getImage().setSpriteState(res.getSpriteIDs()[0]);
				Engine.getEngine(this, this.getClass()).addImage(this.vehicleParts[x][y][0].getImage(), this.layer);
			}else{
				Engine.getEngine(this, this.getClass()).removeImage(this.layer, this.vehicleParts[x][y][0].getImage());
				if(startVehiclePart.getX() == x && startVehiclePart.y == y){
					startVehiclePart = null;
					updateConnection(x, y);
				}else updateConnection((int)startVehiclePart.getX(), (int)startVehiclePart.getY());
				vehicleParts[x][y][0] = null;
			}
		}
	}

	private boolean updateConnection(int x, int y) {
		if(vehicleParts.length >= x || vehicleParts[x].length >= y)return false;
		boolean connected = false;
		if(startVehiclePart==null)startVehiclePart = new Point(x, y);
		if(startVehiclePart.getX()==x && startVehiclePart.getY() == y)connected = true;
		if(vehicleParts[x+1][y]  [0] != null && updateConnection(x+1, y  )) connected = true;
		if(vehicleParts[x-1][y]  [0] != null && updateConnection(x-1, y  )) connected = true;
		if(vehicleParts[x]  [y+1][0] != null && updateConnection(x  , y+1)) connected = true;
		if(vehicleParts[x]  [y-1][0] != null && updateConnection(x  , y-1)) connected = true;
		vehicleParts[x][y][0].setConnected(connected);
		return connected;
	}

	private boolean checkConnection(int x, int y) {
		if(vehicleParts.length >= x || vehicleParts[x].length >= y)return false;
		if(startVehiclePart.getX() == x && startVehiclePart.y == y)return true;
		else{
			if(vehicleParts[x+1][y]  [0] != null) return vehicleParts[x+1][y]  [0].isConnected();
			else if(checkConnection(x + 1, y    )) return true;
			if(vehicleParts[x-1][y]  [0] != null) return vehicleParts[x-1][y]  [0].isConnected();
			else if(checkConnection(x - 1, y    )) return true;
			if(vehicleParts[x]  [y+1][0] != null) return vehicleParts[x]  [y+1][0].isConnected();
			else if(checkConnection(x    , y +1 )) return true;
			if(vehicleParts[x]  [y-1][0] != null) return vehicleParts[x]  [y-1][0].isConnected();
			else if(checkConnection(x    , y -1 )) return true;
		}
		return false;
	}
}
