package game.inventory.equipment.weapon;

import game.inventory.equipment.Equipment;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;

public abstract class Weapon extends Equipment{

	public Weapon(EquipmentType type, EquipmentVisual visuals) {
		super(type, visuals);
	}

}
