package game.menu;

import java.awt.Color;
import java.awt.Dimension;

import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Data.Image.Image;
import Engine.Engine;
import game.vehicle.data.VehicleArmorData;
import game.vehicle.data.VehicleData;
import game.vehicle.data.VehiclePropulsionData;
import game.vehicle.data.VehicleWeaponData;
import sprites.Sprites;

public class StatsMenu {
	
	private StringRenderOperation[] statsVisual;
	private Image background;
	private Location loc;
	private int layer = 0;
	private boolean created = false;
	
	public StatsMenu(Location loc, int layer){
		this.layer = layer;
		background = new Image(loc, new Dimension(200, 400), "", Sprites.StatsMenu.getSpriteSheet(), null);
		background.disabled = true;
		Engine.getEngine(this, this.getClass()).addImage(background, layer);
		this.loc = loc;		
	}
	
	public StatsMenu create(VehicleData data){
		if(statsVisual!=null) destroy();
//		System.out.println(data);
		if(data instanceof VehicleArmorData){
//			VehicleArmorData armorData = (VehicleArmorData) data;
			statsVisual = new StringRenderOperation[2];
		}else if(data instanceof VehiclePropulsionData){
			VehiclePropulsionData propulsionData = (VehiclePropulsionData) data;
			statsVisual = new StringRenderOperation[3];
			statsVisual[1] = new StringRenderOperation(new Location(loc.x+30, loc.y+150), new Dimension(100, 50), "Speed: "+propulsionData.getSpeed(), null, Color.WHITE);
		}else if(data instanceof VehicleWeaponData){
			VehicleWeaponData weaponData = (VehicleWeaponData) data;
			statsVisual = new StringRenderOperation[4];
			statsVisual[1] = new StringRenderOperation(new Location(loc.x+30, loc.y+150), new Dimension(100, 50), "Speed: " +weaponData.getSpeed() , null, Color.WHITE);
			statsVisual[1] = new StringRenderOperation(new Location(loc.x+30, loc.y+200), new Dimension(100, 50), "Damage: "+weaponData.getDamage(), null, Color.WHITE);
		}
		statsVisual[0] = new StringRenderOperation(new Location(loc.x+30, loc.y+ 30), new Dimension(100, 50), 	    data.getName(), null, Color.WHITE);
		statsVisual[1] = new StringRenderOperation(new Location(loc.x+30, loc.y+100), new Dimension(100, 50), "HP: "+data.getHP()  , null, Color.WHITE);
		return this;
	}
	
	public void destroy() {
		if(created){
			created = false;
			for(StringRenderOperation go:statsVisual)Engine.getEngine(this, this.getClass()).removeGraphicOperation(go, layer);
			statsVisual = null;
//			background.disabled = true;
		}
	}

	public void show(){
		if(!created && statsVisual!=null){
			for(StringRenderOperation go:statsVisual)Engine.getEngine(this, this.getClass()).addGraphicOperation(go, layer);
			created = true;
		}else if(created){
			for(StringRenderOperation go:statsVisual)go.disabled = false;
		}
		background.disabled = false;
	}
	
	public void hide(){
		if(created){
			for(StringRenderOperation go:statsVisual)go.disabled = true;
		}
		background.disabled = true;
	}

}
