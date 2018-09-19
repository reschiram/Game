package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;

import Data.Location;
import Engine.Engine;
import data.ButtonTrigger;
import game.entity.Entity;
import game.entity.player.Player;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;
import game.inventory.equipment.tools.menu.BuildToolMenu;
import game.inventory.items.ItemType;
import game.map.Map;
import game.tick.TickManager;

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
		if(user instanceof Player){
			Player player = (Player)user;
			Location loc = new Location(Map.getMap().getXOver(	(int) Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX())/Map.DEFAULT_SQUARESIZE,
					 											(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
			if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON3)){
				player.getPlayerDrone().removeTarget(loc);
				return;
			}
			int mouseWheelMoved = Engine.getInputManager().getMouseWheelsMove((long) (TickManager.getTickDuration()*2));
			if(mouseWheelMoved!=0)((BuildToolMenu) this.menu).moveSelected(mouseWheelMoved);
			else if(((BuildToolMenu) this.menu).getSelected()!=0){
				player.getPlayerDrone().addInterActionLocation(((BuildToolMenu) this.menu).getSelected(), loc);
			}
		}
	}
	
	@Override
	public boolean isTriggered(){
		if(super.isTriggered())return true;
		else if(Engine.getInputManager().getMouseWheelsMove((long) TickManager.getTickDuration())!=0)return true;
		return false;
	}

}
