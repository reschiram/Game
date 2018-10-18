package game.inventory;

import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class Inventory {
	
	private Item[] items;	
	
	public Inventory(int size){
		this.items = new Item[size];
	}
	
	public int addItem(Item item){
		int freeSpace = -1;
		for(int i = 0; i<items.length; i++){
			if(items[i]!=null){
				int amount = items[i].canBeStackedWith(item);
				if(amount!=0){
					items[i].setAmount(items[i].getAmount()+amount);
					if(amount!=item.getAmount()){
						item.setAmount(item.getAmount()-amount);
						return addItem(item);
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
	
	public int removeItem(Item item){
		for(int i = 0; i < this.items.length; i++){
			if(items[i]!=null && items[i].isSimilar(item)){
				int amount = items[i].getAmount()-item.getAmount();
				if(amount<=0){
					items[i] = null;
					if(amount<0){
						return removeItem(new Item(item.getItemType(), Math.abs(amount)));
					}else return 0;
				}else items[i].setAmount(amount);
				return 0;
			}
		}
		return item.getAmount();
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

}
