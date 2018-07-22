package game.entity.type.data;

import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;

public class EntityInventoryData extends EntityData{
	
	private int itemAmount;
	private int equipmentAmount;

	public EntityInventoryData( int itemAmount, int equipmentAmount) {
		super();
		this.itemAmount = itemAmount;
		this.equipmentAmount = equipmentAmount;
	}
	
	public Inventory createInventory(){
		if(equipmentAmount>0) return new EquipmentInventory(itemAmount, equipmentAmount);
		return new Inventory(itemAmount);
	}

}
