package events.inventory;

import game.inventory.Inventory;
import game.inventory.items.Item;

public class ItemRemoveEvent extends InventoryEvent{
	
	private Item item;

	public ItemRemoveEvent(Inventory inv, Item item) {
		super(inv);
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

}
