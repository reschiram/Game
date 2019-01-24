package game.inventory.equipment;

import events.GameEventManager;
import events.inventory.ItemSetEvent;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class EquipmentInventory extends Inventory{

	private Equipment[] equipmentSlot;
	
	public EquipmentInventory(int size, int equipmentSlots) {
		super(size);
		this.equipmentSlot = new Equipment[equipmentSlots];
	}
	
	@Override
	public int addItem(Item item){
		int added = super.addItem(item);
		if(item instanceof Equipment){
			boolean addedToEquipment = addToEquipment(item, true, true);
			if(added!=0)return added;
			else if(addedToEquipment)return 0;
		}
		return added;
	}
	
	private boolean addToEquipment(Item item, boolean act, boolean remove){
		for(int i = 0; i<equipmentSlot.length; i++){
			if(equipmentSlot[i]==null){
				if(remove)removeItem(item);
				if(act)equipmentSlot[i] = (Equipment) item;
				return true;
			}
		}
		return false;
	}

	@Override
	public int removeItem(Item item){
		int removed = super.removeItem(item);
		if(item instanceof Equipment){
			boolean removedFromEquipment = removeFromEquipment(item, true);
			return (removed==0 || (removed!=0 && removedFromEquipment)) ? 0 : item.getAmount();
		}
		return removed;
	}
	
	private boolean removeFromEquipment(Item item, boolean act) {
		for(int i = 0; i<equipmentSlot.length; i++){
			if(equipmentSlot[i]!=null && equipmentSlot[i].isSimilar(item)){
				if(act){
					equipmentSlot[i] = null;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSize(){
		return super.getSize() + this.equipmentSlot.length;
	}
	
	public int getItemSize(){
		return super.getSize();
	}
	
	public int getEquipmentSize(){
		return this.equipmentSlot.length;
	}

	@Override
	public boolean canAdd(ItemType itemType) {
		boolean canAdd = super.canAdd(itemType);
		if(itemType instanceof EquipmentType){
			return canAdd || (!canAdd && addToEquipment(new Item(itemType), false, false));
		}
		return canAdd;
	}

	@Override
	public boolean addItem(ItemType itemType) {
		Item item = null;
		if(itemType instanceof EquipmentType) item = ((EquipmentType)itemType).createEquipment();
		else item = new Item(itemType);
		return this.addItem(item)!=0;
	}
	
	@Override
	public Item getItem(int index) {
		if(index<super.getSize())return super.getItem(index);
		else return equipmentSlot[index-super.getSize()];
	}
	
	public Equipment getEquipment(int index){
		return this.equipmentSlot[index];
	}


	@Override
	public void setItem(int slot, Item item) {
		if(slot<getItemSize())super.setItem(slot, item);
		else if(item instanceof Equipment) {
			equipmentSlot[slot-getItemSize()] = (Equipment) item;
			GameEventManager.getEventManager().publishEvent(new ItemSetEvent(this, item, slot));
		}
	}
}
