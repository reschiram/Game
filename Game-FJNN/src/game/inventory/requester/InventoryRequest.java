package game.inventory.requester;

import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;

public class InventoryRequest {
	
	private int invSlots;
	private int equipSlots;
	private InventoryAcceptor acceptor;
	
	public InventoryRequest(int invSlots, int equipSlots, InventoryAcceptor acceptor) {
		super();
		this.invSlots = invSlots;
		this.equipSlots = equipSlots;
		this.acceptor = acceptor;
	}
	
	public void acceptInventory(int invID) {
		if(equipSlots > 0) {
			acceptor.acceptInventory(new EquipmentInventory(invSlots, equipSlots, invID));
		}else {
			acceptor.acceptInventory(new Inventory(invSlots, invID));			
		}
	}

}
