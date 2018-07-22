package game.inventory.equipment;

import data.ButtonTrigger;
import game.entity.Entity;
import game.inventory.items.Item;

public class Equipment extends Item{
	
	private EquipmentVisual visuals;
	private ButtonTrigger trigger;
	
	public Equipment(EquipmentType type, EquipmentVisual visuals, int triggerKeyCode) {
		super(type);
		this.visuals = visuals;
		this.trigger = new ButtonTrigger(triggerKeyCode);
	}
	
	public void use(Entity user){
		
	}

	public void equip(Entity entity){
		visuals.create(entity).show();
	}

	public boolean isTriggered() {
		trigger.tick();
		boolean triggered = trigger.isTriggered();
//		System.out.println(triggered);
		return triggered;
	}
	
}
