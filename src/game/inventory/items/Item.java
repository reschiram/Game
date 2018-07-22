package game.inventory.items;

public class Item {
	
	private ItemType type;
	private int amount;
	
	public Item(ItemType type){
		this.type = type;
		this.amount = 1;
	}
	
	public Item(ItemType type, int amount){
		this.type = type;
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public ItemType getItemType(){
		return type;
	}

	public boolean canBeStackedWith(Item item) {
		if(item.getItemType().equals(this.type)){
			if(this.amount + item.getAmount() <= this.type.getMaxStackAmount()){
				return true;
			}
		}
		return false;
	}

	public boolean isSimilar(Item item) {
		return item.getItemType().equals(this.type);
	}

	public boolean canAdd(ItemType itemType) {
		return this.type.equals(itemType) && this.amount+1 <= this.type.getMaxStackAmount();
	}

}
