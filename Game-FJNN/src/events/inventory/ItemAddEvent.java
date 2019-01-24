package events.inventory;

import game.inventory.Inventory;
import game.inventory.items.Item;

public class ItemAddEvent extends InventoryEvent{
	
	private Item item;

	public ItemAddEvent(Inventory inv, Item item) {
		super(inv);
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

}
