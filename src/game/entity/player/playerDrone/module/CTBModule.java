package game.entity.player.playerDrone.module;

import Data.Location;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.Drone;
import game.entity.player.playerDrone.DroneTarget;
import game.inventory.crafting.Recipe;
import game.inventory.items.Item;
import game.map.Map;

public class CTBModule extends CTModule{

	@Override
	public void removeTarget(Location loc) {
		int key = loc.getX()+loc.getY()*Map.getMap().getWidth();
		if(targets.containsKey(key)){
			DroneTarget target = this.targets.get(key);
			if(target instanceof BDroneTarget){
				BDroneTarget bTarget = (BDroneTarget) target;
				Recipe recipe = Recipe.getRecipe(bTarget.getID());
				if(recipe!=null){
					for(Item item: recipe.getItems()){
						IRModule module = (IRModule) this.drone.getModule(IRModule.class);
						module.removeItem(item.clone());
					}
				}
			}
		}
		super.removeTarget(loc);
	}
	
	@Override
	public boolean addTarget(DroneTarget target) {
		if(target instanceof BDroneTarget){
			BDroneTarget bTarget = (BDroneTarget) target;
			if(super.addTarget(target)){
				Recipe recipe = Recipe.getRecipe(bTarget.getID());
				if(recipe!=null){
					for(Item item: recipe.getItems()){
						IRModule module = (IRModule) this.drone.getModule(IRModule.class);
						module.addItem(item.clone());
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean canInteract(Location target) {
		return Map.getMap().getMapData(target)[Map.DEFAULT_BUILDLAYER]==null;
	}
	
	@Override
	public void register(Drone drone) {
		super.register(drone);
		
		int maxx = drone.getWidth()/2;
		int maxy = drone.getHeight()/2;
		
		maxDistanceFromTarget = Math.sqrt(maxx*maxx+maxy*maxy);
	}
}
