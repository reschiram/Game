package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;

import Engine.Engine;
import data.ButtonTrigger;
import game.entity.Entity;
import game.inventory.equipment.Equipment;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;
import game.inventory.equipment.tools.menu.ToolMenu;

public abstract class Tool extends Equipment{
	
	protected ToolMenu menu;
	
	public Tool(EquipmentType type, EquipmentVisual visuals) {
		super(type, visuals);
		this.trigger.add(new ButtonTrigger(MouseEvent.BUTTON1));
		this.trigger.add(new ButtonTrigger(MouseEvent.BUTTON2));
		this.trigger.add(new ButtonTrigger(MouseEvent.BUTTON3));
	}

	public boolean tryOpenInventory(Entity user) {
		if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON2)){
			if(!this.menu.isVisible())this.menu.show();
			else this.menu.hide();
			return true;
		}else if(this.menu.isVisible()){
			this.menu.tick();
			return true;
		}
		return false;
	}

}
