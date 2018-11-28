package game.inventory.equipment;

import java.util.ArrayList;

import data.ButtonTrigger;
import game.entity.Entity;
import game.inventory.items.Item;

public class Equipment extends Item{
	
	private EquipmentVisual visuals;
	
	protected ArrayList<ButtonTrigger> trigger = new ArrayList<>();
	
	public Equipment(EquipmentType type, EquipmentVisual visuals) {
		super(type);
		this.visuals = visuals;
	}
	
	public void use(Entity user){
		
	}

	public void equip(Entity entity){
		visuals.create(entity).show();
	}

	public boolean isTriggered() {
		boolean triggered = false;
		for(ButtonTrigger trigger: this.trigger){
			trigger.tick();
			if(trigger.isTriggered()){
				triggered = true;
			}
		}
		return triggered;
	}
	
}
