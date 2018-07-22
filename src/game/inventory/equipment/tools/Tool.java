package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;

import game.inventory.equipment.Equipment;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;

public abstract class Tool extends Equipment{

	public Tool(EquipmentType type, EquipmentVisual visuals) {
		super(type, visuals, MouseEvent.BUTTON1);
	}

}
