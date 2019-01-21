package game.inventory.equipment.tools;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import Data.Location;
import Engine.Engine;
import data.ButtonTrigger;
import data.MapResource;
import game.entity.Entity;
import game.entity.player.Player;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.module.CTBModule;
import game.entity.player.playerDrone.module.CTDModule;
import game.gridData.map.Mapdata;
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
		if(user instanceof Player){
			Player player = (Player)user;
			
			int mouseWheelMoved = Engine.getInputManager().getMouseWheelsMove((long) (TickManager.TICK_DURATION*2));
			if(mouseWheelMoved!=0)((BuildToolMenu) this.menu).moveSelected(mouseWheelMoved);
			Location loc = new Location(Map.getMap().getXOver(	(int) Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX())/Map.DEFAULT_SQUARESIZE,
						(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
			
			//find BDrones|DDrones
			ArrayList<Drone> BDrones = new ArrayList<>();
			ArrayList<Drone> DDrones = new ArrayList<>();
			for(Drone drone: player.getPlayerDrones()){
				CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
				if(module!=null){
					BDrones.add(drone);
				}
				CTDModule dmodule = (CTDModule) drone.getModule(CTDModule.class);
				if(dmodule!=null){
					DDrones.add(drone);
				}
			}
			
			//add|remove BDroneTarget
			if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON3)){
				for(Drone drone: BDrones){
					CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
					module.removeTarget(loc);
				}
				return;
			}
			else if(((BuildToolMenu) this.menu).getSelected()!=0){
				BDroneTarget target = new BDroneTarget(loc, MapResource.getMapResource(((BuildToolMenu) this.menu).getSelected()));
				boolean exists = false;
				for(Drone drone: BDrones){
					CTBModule module = (CTBModule) drone.getModule(CTBModule.class);
					target.addDrone(drone);
					exists = module.addTarget(target);
				}
				if(exists)target.createVisuals();
			}
			
			//add DDroneTarget
			Mapdata data = Map.getMap().getMapData(loc)[Map.DEFAULT_BUILDLAYER];
			if(data!=null){
				if(((BuildToolMenu) this.menu).getSelected()!=0){
					DDroneTarget target = new DDroneTarget(loc);
					target.createVisuals();
					boolean exists = false;
					for(Drone drone: DDrones){
						CTDModule module = (CTDModule) drone.getModule(CTDModule.class);
						target.addDrone(drone);
						exists = module.addTarget(target);
					}
					if(exists)target.createVisuals();
				}
			}
		}
	}
	
	@Override
	public boolean isTriggered(){
		if(super.isTriggered())return true;
		else if(Engine.getInputManager().getMouseWheelsMove((long) TickManager.TICK_DURATION)!=0)return true;
		return false;
	}

}
