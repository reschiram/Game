package data.map;

import java.util.HashMap;

import game.inventory.Inventory;

public class InventoryManager {
	
	private HashMap<Integer, Inventory> invs = new HashMap<>();
	private int lastID = 0;

	public InventoryManager() {
		
	}
	
	public Inventory getInventory(int id) {
		return this.invs.get(id);
	}
	
	public int addInventory(Inventory inv) {
		int id = this.lastID + 1;
		this.lastID++;
		
		inv.setInvID(id);
		this.invs.put(inv.getInvID(), inv);
		return id;
	}
}
