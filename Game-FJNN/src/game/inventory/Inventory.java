package game.inventory;

import java.util.HashMap;

import events.GameEventManager;
import events.inventory.ItemAddEvent;
import events.inventory.ItemRemoveEvent;
import events.inventory.ItemSetEvent;
import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class Inventory {
	
	private static final HashMap<Integer, Inventory> inventories = new HashMap<>();
	public static Inventory getInventory(int invID) {
		return inventories.get(invID);
	}
	
	private Item[] items;	
	private int invID;
	
	public Inventory(int size, int invID){
		this.items = new Item[size];
		this.invID = invID;
		
		inventories.put(invID, this);
	}
	
	public int addItemFunktion(Item item) {
		int freeSpace = -1;
		for(int i = 0; i<items.length; i++){
			if(items[i]!=null){
				int amount = items[i].canBeStackedWith(item);
				if(amount!=0){
					items[i].setAmount(items[i].getAmount()+amount);
					if(amount!=item.getAmount()){
						item.setAmount(item.getAmount()-amount);
						return addItemFunktion(item);
					}else return 0;
				}
			}else if(freeSpace == -1)freeSpace = i;
		}
		if(freeSpace!=-1){
			items[freeSpace] = item;
			return 0;
		}
		return item.getAmount();
	}
	
	public int addItem(Item item){
		GameEventManager.getEventManager().publishEvent(new ItemAddEvent(this, item));
		return addItemFunktion(item);
	}

	public int removeItemFunktion(Item item){
		for(int i = 0; i < this.items.length; i++){
			if(items[i]!=null && items[i].isSimilar(item)){
				int amount = items[i].getAmount()-item.getAmount();
				if(amount<=0){
					items[i] = null;
					if(amount<0){
						return removeItemFunktion(new Item(item.getItemType(), Math.abs(amount)));
					}else return 0;
				}else items[i].setAmount(amount);
				return 0;
			}
		}
		return item.getAmount();		
	}
	
	public int removeItem(Item item){
		GameEventManager.getEventManager().publishEvent(new ItemRemoveEvent(this, item));
		return removeItemFunktion(item);
	}
	
	public int getSize(){
		return this.items.length;
	}

	public boolean canAdd(ItemType itemType) {
		int freeSpace = -1;
		for(int i = 0; i<items.length; i++){
			if(items[i]!=null){
				if(items[i].canAdd(itemType))return true;
			}else if(freeSpace == -1)freeSpace = i;
		}
		if(freeSpace!=-1)return true;
		return false;
	}

	public Item getItem(int index) {
		return this.items[index];
	}

	public void setItemFunktion(int slot, Item item) {
		this.items[slot] = item;
	}

	public void setItem(int slot, Item item) {
		GameEventManager.getEventManager().publishEvent(new ItemSetEvent(this, item, slot));
		setItemFunktion(slot, item);
	}

	public boolean hasItem(Item item) {
		int amount = item.getAmount();
		for(Item inv_item: items){
			if(inv_item!=null && inv_item.getItemType().equals(item.getItemType())){
				amount-=inv_item.getAmount();
				if(amount<=0)return true;
			}
		}
		return false;
	}

	public int getInvID() {
		return invID;
	}

	public void setInvID(int id) {
		this.invID = id;
	}

}
