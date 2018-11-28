package game.inventory.equipment;

import Data.Image.SpriteSheet;
import game.inventory.items.ItemType;

public class EquipmentType extends ItemType{
	
	private Class<?> equipmentClass;

	public EquipmentType(String id, int maxStackAmount, SpriteSheet sheet, Class<?> equipmentClass, int... ids) {
		super(id, maxStackAmount, sheet, ids);
		this.equipmentClass = equipmentClass;
	}

	public Equipment createEquipment() {
		try {
			return (Equipment) equipmentClass.getConstructor().newInstance();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
