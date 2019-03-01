package game.entity.player;

import java.util.ArrayList;
import java.util.HashMap;

import data.Velocity;
import game.entity.Entity;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneHost;
import game.entity.player.playerDrone.DroneTarget;
import game.entity.type.interfaces.EntityInventory;
import game.inventory.Inventory;
import game.inventory.ItemCollector;
import game.inventory.equipment.EquipmentInventory;

public class PlayerContext extends Entity implements EntityInventory, DroneHost{
	
	protected ItemCollector itemCollector;	
	protected ArrayList<Drone> drones = new ArrayList<>();
	
	private final HashMap<Integer, ArrayList<DroneTarget>> targets = new HashMap<>();
	
	public PlayerContext(){		
		super(new ArrayList<>());	
	}
	
	protected void load(EquipmentInventory inv) {
		this.itemCollector = new ItemCollector(this, inv, 3.0);	
	}

	@Override
	public Inventory getInventory() {
		return itemCollector.getInventory();
	}

	@Override
	public ArrayList<Drone> getPlayerDrones() {
		return drones;
	}

	@Override
	public void addDrone(Drone drone) {
		this.drones.add(drone);
	}

	public Velocity getVelocity() {
		return this.moveManager.getVelocity();
	}

	public void setVelocity(Velocity velocity) {
		this.moveManager.setVelocity(velocity);
	}
	
	@Override
	public void tick() {
		super.tick();
		itemCollector.tick();
	}

	@Override
	public HashMap<Integer, ArrayList<DroneTarget>> getTargets() {
		return targets;
	}
	
	
}
