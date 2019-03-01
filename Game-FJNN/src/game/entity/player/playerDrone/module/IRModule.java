package game.entity.player.playerDrone.module;

import java.util.HashMap;

import client.ComsData;
import events.GameEventManager;
import events.entity.drone.TargetEventDU;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.items.ItemType;

public class IRModule extends DroneModule{
	
	private HashMap<ItemType, Integer> requestedItems = new HashMap<>();

	@Override
	public void tick() {
		if(!this.requestedItems.isEmpty() && collectItems()){
			if(this.drone.getBlockLocation().distance_Math(this.drone.getHost().getBlockLocation()) != 0){
				if(!this.drone.getPathController().hasTarget() || ((this.drone.getPathController().reachedDestination() && !this.drone.isWorking()) || !this.drone.getPathController().getBlockTarget().isEqual(this.drone.getHost().getBlockLocation()))){
					
					GameEventManager.getEventManager().publishEvent(new TargetEventDU(this.drone, this.drone.getHost().getBlockLocation(), ComsData.TargetLevel_Drone_ItemRequest));
				}
			}else{
//				GameEventManager.getEventManager().publishEvent(new TargetEventDU(this.drone, null, ComsData.TargetLevel_Reset));
				
				Inventory pInv = this.drone.getHost().getInventory();
				Inventory dInv = ((InvModule)this.drone.getModule(InvModule.class)).getInventory();
				
				for(ItemType type: requestedItems.keySet()){
					int requested = requestedItems.get(type);
					for(int i = 0; i<dInv.getSize(); i++){
						Item item = dInv.getItem(i);
						if(item!=null && item.getItemType().getID().equals(type.getID())){
							requested-=item.getAmount();
							if(requested<=0)i=dInv.getSize();
						}
					}
					if(requested>0){
						int removed = pInv.removeItem(new Item(type, requested));
						int added = dInv.addItem(new Item(type, requested-removed));
						if(added>0)pInv.addItem(new Item(type, added));
					}
				}
			}
		}
	}

	private boolean collectItems() {		
		Inventory pInv = this.drone.getHost().getInventory();
		Inventory dInv = ((InvModule)this.drone.getModule(InvModule.class)).getInventory();
		
		for(ItemType type: requestedItems.keySet()){
			int requested = requestedItems.get(type);
			for(int i = 0; i<dInv.getSize(); i++){
				Item item = dInv.getItem(i);
				if(item!=null && item.getItemType().getID().equals(type.getID())){
					requested-=item.getAmount();
					if(requested<=0)i=dInv.getSize();
				}
			}
			if(requested>0){
				for(int i = 0; i<pInv.getSize(); i++){
					Item item = pInv.getItem(i);
					if(item!=null && item.getItemType().getID().equals(type.getID())){
						requested-=item.getAmount();
						if(requested<=0)i=pInv.getSize();
					}
				}
				if(requested<0)return true;
			}
		}
		
		return false;
	}

	public void removeItem(Item item) {
		if(this.requestedItems.containsKey(item.getItemType())){
			int currentAmount = this.requestedItems.get(item.getItemType());
			if(currentAmount - item.getAmount() <= 0)this.requestedItems.remove(item.getItemType());
			else this.requestedItems.put(item.getItemType(), currentAmount-item.getAmount());
		}
	}
	
	public void addItem(Item item) {
		if(this.requestedItems.containsKey(item.getItemType())){
			int currentAmount = this.requestedItems.get(item.getItemType());
			this.requestedItems.put(item.getItemType(), currentAmount+item.getAmount());
		}else this.requestedItems.put(item.getItemType(), item.getAmount());
	}

	public Item[] getAllItems() {
		Item[] items = new Item[this.requestedItems.size()];
		int i = 0;
		for(ItemType type: this.requestedItems.keySet()){
			items[i] = new Item(type, this.requestedItems.get(type));
			i++;
		}
		return items;
	} 

}
