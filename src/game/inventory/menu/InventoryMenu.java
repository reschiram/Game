package game.inventory.menu;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.ButtonTrigger;
import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;
import sprites.Sprites;

public class InventoryMenu {

	private static int ITEMSLOTCOUNTWIDTH = 12;
	
	static int ITEMSLOTDISTANCE = 8;	
	static Dimension InventoryMenuSize = new Dimension((ItemImage.DEFAULT_SLOTSIZE+ITEMSLOTDISTANCE)*ITEMSLOTCOUNTWIDTH + ITEMSLOTDISTANCE + 20, (ItemImage.DEFAULT_SLOTSIZE+ITEMSLOTDISTANCE)*8);
	
	private Inventory inventory;
	private Location dropLocation;
	
	private Image backGround;
	private ItemImage[] itemVisuals;
	private int layer;
	
	public InventoryMenu(Inventory inventory, Location dropLocation, int layer){
		this.inventory = inventory;
		this.dropLocation = dropLocation;
		this.layer = layer;
		
		this.backGround = new Image(new Location((1920-(int)InventoryMenuSize.getWidth())/2, (1080-(int)InventoryMenuSize.getHeight())/3), InventoryMenuSize, "",
				Sprites.Backgrounds.getSpriteSheet(), null);
		this.backGround.setSpriteState(1);
		
		int size = inventory.getSize();
		if(inventory instanceof EquipmentInventory)size = ((EquipmentInventory)inventory).getItemSize();
		this.itemVisuals = new ItemImage[size];
		
		for(int i = 0; i<size; i++){
			int y = i/ITEMSLOTCOUNTWIDTH;
			int x = i-y*ITEMSLOTCOUNTWIDTH;
			
			this.itemVisuals[i] = new ItemImage(inventory.getItem(i), layer,
					new Location(x*ItemImage.DEFAULT_SLOTSIZE + (x+1) * ITEMSLOTDISTANCE + backGround.getX() + 10, y*ItemImage.DEFAULT_SLOTSIZE + (y+1) * ITEMSLOTDISTANCE + backGround.getY() + 10));
		}
	}
	
	public InventoryMenu createVisual(){		
		Engine.getEngine(this, this.getClass()).addImage(backGround, layer);
		for(int i = 0; i<itemVisuals.length; i++){
			itemVisuals[i].createVisual();
		}
		
		return this;
	}
	
	public void show(){
		backGround.disabled = false;
		for(int i = 0; i<itemVisuals.length; i++){
			itemVisuals[i].show();
		}
	}
	
	public void hide(){
		backGround.disabled = true;
		for(int i = 0; i<itemVisuals.length; i++){
			itemVisuals[i].hide();
		}
	}
	
	public void destroyVisual(){
		Engine.getEngine(this, this.getClass()).removeImage(layer, backGround);
		for(int i = 0; i<itemVisuals.length; i++){
			itemVisuals[i].destroyVisual();
		}
	}
	
	public void update(){
		lastUpdate = System.currentTimeMillis();
		
		for(int i = 0; i<itemVisuals.length; i++){			
			ItemImage itemVisual = this.itemVisuals[i];
			if(itemVisual.getItem() != null && itemVisual.getItem().equals(inventory.getItem(i)))this.itemVisuals[i].update();
			else if(itemVisual.getItem() != null || inventory.getItem(i) != null){
				itemVisual.setItem(inventory.getItem(i));
			}
		}
	}
	
	public void setDropLocation(Location dropLocation){
		this.dropLocation = dropLocation;
	}
	
	private long lastUpdate = System.currentTimeMillis();
	private ButtonTrigger showHideTrigger = new ButtonTrigger(KeyEvent.VK_I); 
	public void tick(){
		showHideTrigger.tick();
		if(isVisible()){
			if(showHideTrigger.isTriggered() || Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_ESCAPE)){
				this.hide();
			}else{
				if(System.currentTimeMillis()-lastUpdate > 500){
					this.update();
				}	
			}
		}else{
			if(showHideTrigger.isTriggered()){
				this.show();
				update();
			}
		}
	}

	public boolean isVisible() {
		return backGround.disabled==false;
	}

	public Location getMenuLocation() {
		return backGround.getLocation();
	}

}
