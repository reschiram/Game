package game.entity.player.playerDrone.module;

import Data.Lists.ArrayList;
import game.inventory.Inventory;
import game.inventory.items.Item;
import game.inventory.requester.InventoryAcceptor;
import game.inventory.requester.InventoryRequester;

public class InvModule extends DroneModule implements InventoryAcceptor{
	
	protected Inventory inv;
//	protected InventoryMenu invMenu;
//	protected ButtonTrigger trigger;
	
	public InvModule(int invSize) {
		InventoryRequester.getInventoryRequester().requestInventory(invSize, 0, this);
//		this.invMenu = new InventoryMenu(inv, new Location(0,0), 5);
//		this.invMenu.createVisual();
//		this.invMenu.hide();
//		this.trigger = new ButtonTrigger(KeyEvent.VK_M);
	}

	@Override
	public void tick() {
		if(inv == null) return;
		if(this.drone.getBlockLocation().distance_Math(this.drone.getHost().getBlockLocation()) == 0){
			transferInventory();
		}
//		this.trigger.tick();
//		if(this.trigger.isTriggered()){
//			if(this.invMenu.isVisible())this.invMenu.hide();
//			else this.invMenu.show();
//		}
//		if(this.invMenu.isVisible())this.invMenu.update();
	}

	private void transferInventory() {
		ArrayList<Item> ignoreItems = new ArrayList<>();
		IRModule module = (IRModule) this.drone.getModule(IRModule.class);
		if(module!=null){
			for(Item item: module.getAllItems()){
				ignoreItems.add(item);
			}
		}
		Inventory pInv = this.drone.getHost().getInventory();
		for(int i = 0; i<this.inv.getSize(); i++){
			Item item = this.inv.getItem(i);
			if(item!=null){
				int add = item.getAmount();
				for(int a = 0; a<ignoreItems.size(); a++){
					Item ignoreItem = ignoreItems.get(a);
					if(ignoreItem.isSimilar(item)){
						int amount = item.getAmount()-ignoreItem.getAmount();
						if(amount<=0){
							add = 0;
							ignoreItem.setAmount(Math.abs(amount));
							a=ignoreItems.size();
						}else{
							add = amount;
							ignoreItems.remove(a);
							a--;
						}
					}
				}
				if(add>0){
					Item transfer = new Item(item.getItemType(), add);
					this.inv.removeItem(transfer);
					int added = pInv.addItem(transfer);
					if(added>0)this.inv.addItem(new Item(item.getItemType(), added));
				}
			}
		}
	}

	public Inventory getInventory() {
		return this.inv;
	}

	@Override
	public void acceptInventory(Inventory inv) {
		this.inv = inv;
	}

}
