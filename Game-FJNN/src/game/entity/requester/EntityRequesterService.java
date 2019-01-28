package game.entity.requester;

import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;

public class EntityRequesterService {

	private static EntityRequesterService entityRequesterService;

	public static EntityRequesterService getEntityRequesterService() {
		if (entityRequesterService == null) entityRequesterService = new EntityRequesterService();
		return entityRequesterService;
	}

	private static final int maxInvSlotsLength = 3;
	private static final int maxEquipSlotsLength = 2;
	private static final int maxInvIDLength = 5;

	public Inventory readInventory(String extraInfos, int start) throws Exception {
		if(start + maxInvIDLength + maxInvSlotsLength + maxEquipSlotsLength > extraInfos.length()) {
			throw new Exception("Invalid inventory information. String too short");
		}

		int invSlots = Integer.parseInt(extraInfos.substring(
				start,
				start + maxInvSlotsLength));
		int equipSlots = Integer.parseInt(extraInfos.substring(
				start + maxInvSlotsLength,
				start + maxInvSlotsLength + maxEquipSlotsLength));
		int invID = Integer.parseInt(extraInfos.substring(
				start + maxInvSlotsLength + maxEquipSlotsLength,
				start + maxInvSlotsLength + maxEquipSlotsLength + maxInvIDLength));
		
		Inventory inv = null;
		if(equipSlots > 0) {
			inv = new EquipmentInventory(invSlots, equipSlots, invID);
		}else {
			inv = new Inventory(invSlots, invID);			
		}
		return inv;		
	}
}
