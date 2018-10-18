package game.entity.player.playerDrone.module;

import game.entity.player.playerDrone.Drone;
import game.inventory.Inventory;
import game.inventory.ItemCollector;

public class ICModule extends DroneModule{
	
	protected ItemCollector collector;
	protected int collectorRange;
	
	public ICModule(int collectorRange){
		this.collectorRange = collectorRange;
	}

	@Override
	public void tick() {
		this.collector.tick();
	}
	
	@Override
	public void register(Drone drone) {
		super.register(drone);
		InvModule module = (InvModule) this.drone.getModule(InvModule.class);
		Inventory inv = module.getInventory();
		this.collector = new ItemCollector(drone, inv, collectorRange);
	}

}
