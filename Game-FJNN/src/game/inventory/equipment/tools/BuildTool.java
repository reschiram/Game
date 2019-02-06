package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;

import Data.Location;
import Engine.Engine;
import data.ButtonTrigger;
import game.entity.Entity;
import game.entity.player.playerDrone.DroneHost;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;
import game.inventory.equipment.tools.menu.BuildToolMenu;
import game.inventory.items.ItemType;
import game.map.Map;
import tick.TickManager;

public class BuildTool extends Tool{

	private static EquipmentVisual createEquipmentVisuals(EquipmentType type) {
		return new EquipmentVisual(type);
	}
	
	public BuildTool() {
		super(ItemType.BuildTool, createEquipmentVisuals(ItemType.BuildTool));
		this.trigger.add(ButtonTrigger.WheelTrigger);
		this.menu = new BuildToolMenu();
		this.menu.createVisuals();
		this.menu.hide();
	}

	@Override
	public void use(Entity user) {
		if(super.tryOpenInventory(user))return;
		if(user instanceof DroneHost){
			DroneHost player = (DroneHost)user;
			
			int mouseWheelMoved = Engine.getInputManager().getMouseWheelsMove((long) (TickManager.TICK_DURATION*2));
			if(mouseWheelMoved!=0)((BuildToolMenu) this.menu).moveSelected(mouseWheelMoved);
			Location loc = new Location(Map.getMap().getXOver(	(int) Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX())/Map.DEFAULT_SQUARESIZE,
						(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
			
			boolean isAdd = !Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON3);
			int resID = (((BuildToolMenu) this.menu).getSelected());
			player.addBuildTarget(loc, isAdd ? resID : -1);
		}
	}
	
	@Override
	public boolean isTriggered(){
		if(super.isTriggered())return true;
		else if(Engine.getInputManager().getMouseWheelsMove((long) TickManager.TICK_DURATION)!=0)return true;
		return false;
	}

}
