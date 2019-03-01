package game.entity.player;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import client.ComsData;
import data.Mouse;
import events.GameEventManager;
import events.entity.drone.CTStatusEventDU;
import game.entity.Entity;
import game.entity.player.playerDrone.DroneTarget;
import game.gridData.map.Mapdata;
import game.inventory.equipment.Equipment;
import game.inventory.equipment.EquipmentInventory;
import game.map.Map;
import sprites.Sprites;

public class PlayerInterface {
	private static Point screenCenter = new Point(1920/2, 1080/2);
	
	private Player player;
	private Dimension ellipseSize;
	private Image pointer;
	
	private int currentEquipment;
	
	public PlayerInterface(Player player) {
		this.player = player;
		this.ellipseSize = new Dimension((int)(player.getWidth()+0.25*Map.DEFAULT_SQUARESIZE), (int)(player.getHeight()+0.25*Map.DEFAULT_SQUARESIZE));
		pointer = new Image(new Location((int) (screenCenter.getX()+5), (int) (screenCenter.getY()+5)), Sprites.Pointer.getSpriteDimension(), "", Sprites.Pointer.getSpriteSheet(), null);
		Engine.getEngine(this, this.getClass()).addImage(pointer, Entity.DEFAULT_ENTITY_LAYER+1);
	}
	
	public void tick() {
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_E)){
			Location digLocation = new Location(Map.getMap().getXOver(this.pointer.getX()+Map.getMap().getMoved().getX()+pointer.getWidth()/2)/Map.DEFAULT_SQUARESIZE,
												(int)(this.pointer.getY()+Map.getMap().getMoved().getY()+pointer.getHeigth()/2	)/Map.DEFAULT_SQUARESIZE);
			
			Mapdata data = Map.getMap().getChunks()[digLocation.x/Map.DEFAULT_CHUNKSIZE][digLocation.y/Map.DEFAULT_CHUNKSIZE].getMapData(digLocation, false)[Entity.DEFAULT_ENTITY_UP];
			if(data!=null){
				data.damage(2);
				if(data.isDestroyed()){
					int key = digLocation.getX() + (digLocation.getY() * Map.getMap().getWidth());
					if(this.player.hasTarget(key)) {
						for(DroneTarget target : this.player.getTargets(key)) {
							GameEventManager.getEventManager().publishEvent(new CTStatusEventDU(this.player, target, ComsData.ActionTarget_StatusUpdate_Type_Remove));
						}
					}
					if(data.getResource().hasDrops()){
						data.getResource().drop(digLocation);
					}
				}
			}
		}
		
		movePointer();
		
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_1)){
			this.currentEquipment = 0;
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_2)){
			this.currentEquipment = 1;
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_3)){			
			this.currentEquipment = 2;
		}
		
		if(((EquipmentInventory)player.getInventory()).getEquipment(currentEquipment)!=null){
			Equipment equip = ((EquipmentInventory)player.getInventory()).getEquipment(currentEquipment);
			if(equip.isTriggered())equip.use(player);
		}
	}
	
	private void movePointer() {
		Location MouseOff = Mouse.getMouse().getOff();
		
		double m = (double)MouseOff.getY()/(Math.abs(MouseOff.getX())+1.0);
		m*=m;
		double w = ellipseSize.getWidth()  * ellipseSize.getWidth() ;
		double h = ellipseSize.getHeight() * ellipseSize.getHeight();
		double e = (w-h);
		
		double x = Math.sqrt((w-e)/(m+((w-e)/w)));	
		int    y = (int) Math.round(Math.sqrt(m)*x);
		if(MouseOff.getX()>0)x*=-1;
		if(MouseOff.getY()>0)y*=-1;
		else if(x==0)y=(int) ellipseSize.getHeight();
		
		pointer.setLocation((int)(screenCenter.getX() + pointer.getWidth()/2 + x - Map.DEFAULT_SQUARESIZE/2 +10), (int)(screenCenter.getY() - pointer.getHeigth()/2 + y - Map.DEFAULT_SQUARESIZE/2));
	}

}






























/*
private void Dig() {
	Location MouseOff = Mouse.getMouse().getOff();
	if(MouseOff.getX() > 0 && MouseOff.getX() > Math.abs(MouseOff.getY())){
		Map.getMap().deleteBlock(new Location(Map.getMap().getXOver((this.getBlockLocation().getX()-1)*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE,  this.getBlockLocation().getY()  ), Entity.DEFAULT_ENTITY_UP, false);
	}else if(MouseOff.getX() < 0 && -MouseOff.getX() > Math.abs(MouseOff.getY())){
		Map.getMap().deleteBlock(new Location(this.getBlockLocation().getX()+1,  this.getBlockLocation().getY()  ), Entity.DEFAULT_ENTITY_UP, false);				
	}else if(MouseOff.getY() > 0 && MouseOff.getY() > Math.abs(MouseOff.getX())){
		Map.getMap().deleteBlock(new Location(this.getBlockLocation().getX()  ,  this.getBlockLocation().getY()-1), Entity.DEFAULT_ENTITY_UP, false);				
	}else if(MouseOff.getY() < 0 && -MouseOff.getY() > Math.abs(MouseOff.getX())){
		Map.getMap().deleteBlock(new Location(this.getBlockLocation().getX()  ,  this.getBlockLocation().getY()+1), Entity.DEFAULT_ENTITY_UP, false);				
	}
}
*/