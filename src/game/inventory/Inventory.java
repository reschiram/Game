package game.inventory;

import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class Inventory {
	
	private Item[] items;	
	
	public Inventory(int size){
		this.items = new Item[size];
	}
	
	public boolean addItem(Item item){
		int freeSpace = -1;
		for(int i = 0; i<items.length; i++){
			if(items[i]!=null){
				if(items[i].canBeStackedWith(item)){
					items[i].setAmount(items[i].getAmount()+item.getAmount());
					return true;
				}
			}else if(freeSpace == -1)freeSpace = i;
		}
		if(freeSpace!=-1){
			items[freeSpace] = item;
			return true;
		}
		return false;
	}
	
	public boolean removeItem(Item item){
		for(int i = 0; i < this.items.length; i++){
			if(items[i]!=null && items[i].isSimilar(item)){
				int amount = items[i].getAmount()-item.getAmount();
//				System.out.println(amount +"->"+i);
				if(amount<=0)items[i] = null;
				else items[i].setAmount(amount);
				return true;
			}
		}
		return false;
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

	public boolean addItem(ItemType itemType) {
		int freeSpace = -1;
		for(int i = 0; i<items.length; i++){
			if(items[i]!=null){
				if(items[i].canAdd(itemType)){
					items[i].setAmount(items[i].getAmount()+1);
					return true;
				}
			}else if(freeSpace == -1)freeSpace = i;
		}
		if(freeSpace!=-1){
			items[freeSpace] = new Item(itemType);
			return true;
		}
		return false;
	}

	public Item getItem(int index) {
		return this.items[index];
	}

	public void setItem(int slot, Item item) {
		this.items[slot] = item;
	}

}
