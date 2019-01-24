package events.inventory;

import events.GameEvent;
import game.inventory.Inventory;

public abstract class InventoryEvent extends GameEvent{
	
	private Inventory inv;

	public InventoryEvent(Inventory inv) {
		super();
		this.inv = inv;
	}

	public Inventory getInv() {
		return inv;
	}

}
