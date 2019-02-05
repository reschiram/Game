package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;

import Data.Location;
import Engine.Engine;
import events.DroneTargetEvent;
import events.GameEventManager;
import game.entity.Entity;
import game.entity.player.playerDrone.DroneHost;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;
import game.inventory.equipment.tools.menu.DigToolMenu;
import game.inventory.items.ItemType;
import game.map.Map;

public class DigTool extends Tool{

	private static EquipmentVisual createEquipmentVisuals(EquipmentType type) {
		return new EquipmentVisual(type);
	}
	
	public DigTool() {
		super(ItemType.DigTool, createEquipmentVisuals(ItemType.DigTool));
		this.menu = new DigToolMenu();
		this.menu.createVisuals();
		this.menu.hide();
	}

	@Override
	public void use(Entity user) {
		if(super.tryOpenInventory(user))return;
		if(user instanceof DroneHost){
			DroneHost player = (DroneHost)user;
			Location loc = new Location(Map.getMap().getXOver(	(int) Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX())/Map.DEFAULT_SQUARESIZE,
					 											(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
			
			boolean isAdd = !Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON3);
			player.addDestructionTarget(loc, isAdd);
			
			GameEventManager.getEventManager().publishEvent(new DroneTargetEvent(player, loc, true, isAdd, 0));
		}
	}
	

}
