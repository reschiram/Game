package events.inventory;

import game.inventory.Inventory;
import game.inventory.items.Item;

public class ItemSetEvent extends InventoryEvent {

	private int slot;
	private Item item;

	public ItemSetEvent(Inventory inv, Item item, int slot) {
		super(inv);
		this.slot = slot;
		this.item = item;
	}

	public int getSlot() {
		return slot;
	}

	public Item getItem() {
		return item;
	}

}
