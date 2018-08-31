package game.entity.player;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.Mouse;
import game.entity.Entity;
import game.entity.ItemEntity;
import game.gridData.map.Mapdata;
import game.map.Map;
import sprites.Sprites;

public class PlayerInterface {
	private static Point screenCenter = new Point(1920/2, 1080/2);
	
	private Player player;
	private Dimension ellipseSize;
	private Image pointer;
	
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
					this.player.getPlayerDrone().removeTarget(digLocation);
					if(data.getResource().hasItemType()){
						for(int i = 0; i<data.getResource().getItemAmount(); i++){
							new ItemEntity(data.getResource().getItemType(), new Location(digLocation.getX()*Map.DEFAULT_SQUARESIZE, digLocation.getY()*Map.DEFAULT_SQUARESIZE)).show();
						}
					}
				}
			}
		}
		movePointer();
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