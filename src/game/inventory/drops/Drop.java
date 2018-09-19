package game.inventory.drops;

import game.inventory.items.ItemType;

public class Drop{
		
	private ItemType type;
	private int minAmount;
	private int maxAmount;		
		
	public Drop(ItemType type, int minAmount, int maxAmount) {
		this.type = type;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}
		
	public ItemType getType() {
		return type;
	}

	public int getMinAmount() {
		return minAmount;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
}