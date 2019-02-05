package game.entity.type.data;

public class EntityInventoryData extends EntityData{
	
	private int itemAmount;
	private int equipmentAmount;

	public EntityInventoryData( int itemAmount, int equipmentAmount) {
		super();
		this.itemAmount = itemAmount;
		this.equipmentAmount = equipmentAmount;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public int getEquipmentAmount() {
		return equipmentAmount;
	}

}
