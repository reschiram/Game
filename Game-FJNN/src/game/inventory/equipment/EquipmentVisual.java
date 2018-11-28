package game.inventory.equipment;

import Data.Image.Image;
import game.entity.Entity;

public class EquipmentVisual {
	
	private Image image;
	private EquipmentType type;
	
	public EquipmentVisual(EquipmentType type) {
	}

	public EquipmentVisual create(Entity entity) {
		return this;
	}
	
	public void show(){
		image.disabled = false;
	}

}
