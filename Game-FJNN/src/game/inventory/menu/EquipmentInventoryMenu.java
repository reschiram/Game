package game.inventory.menu;

import java.awt.event.KeyEvent;

import Data.Location;
import Engine.Engine;
import data.ButtonTrigger;
import game.inventory.equipment.EquipmentInventory;

public class EquipmentInventoryMenu{
	
	private InventoryMenu invMenu;
	private EquipmentInventory inventory;

	private ItemImage[] equipmentSlots;
	
	public EquipmentInventoryMenu(EquipmentInventory inventory, Location dropLocation, int layer) {
		invMenu = new InventoryMenu(inventory, dropLocation, layer);
		
		equipmentSlots = new ItemImage[inventory.getEquipmentSize()];
		for(int i = 0; i<equipmentSlots.length; i++){
			equipmentSlots[i] = new ItemImage(inventory.getEquipment(i), layer, new Location(
					1920/2 - (equipmentSlots.length*(ItemImage.DEFAULT_SLOTSIZE+InventoryMenu.ITEMSLOTDISTANCE))/2 + i*(ItemImage.DEFAULT_SLOTSIZE+InventoryMenu.ITEMSLOTDISTANCE),
					invMenu.getMenuLocation().getY() + (int) InventoryMenu.InventoryMenuSize.getHeight() + 20));
		}
		
		this.inventory = inventory;
	}
	
	public EquipmentInventoryMenu createVisuals(){
		this.invMenu.createVisual();
		for(int i = 0; i<equipmentSlots.length; i++)equipmentSlots[i].createVisual();
	
		show();
		return this;
	}
	
	public void destroyVisuals(){
		this.invMenu.destroyVisual();
		for(int i = 0; i<equipmentSlots.length; i++)equipmentSlots[i].destroyVisual();
		
		hide();
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
	
	public void show() {
		invMenu.show();
		for(int i = 0; i<equipmentSlots.length; i++)equipmentSlots[i].show();
	}
	
	public void update() {
		lastUpdate = System.currentTimeMillis();
		invMenu.update();
		
		for(int i = 0; i<equipmentSlots.length; i++){			
			ItemImage itemVisual = this.equipmentSlots[i];
			if(itemVisual.getItem() != null && itemVisual.getItem().equals(inventory.getEquipment(i)))this.equipmentSlots[i].update();
			else if(itemVisual.getItem() != null || inventory.getEquipment(i) != null){
				itemVisual.setItem(inventory.getEquipment(i));
			}
		}
	}
	
	public void hide() {
		invMenu.hide();
		for(int i = 0; i<equipmentSlots.length; i++)equipmentSlots[i].hide();
	}
	
	public boolean isVisible() {
		return invMenu.isVisible();
	}

}
