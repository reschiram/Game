package game.inventory.equipment.tools;

import Data.Location;
import Engine.Engine;
import game.entity.Entity;
import game.entity.player.Player;
import game.inventory.equipment.EquipmentType;
import game.inventory.equipment.EquipmentVisual;
import game.inventory.items.ItemType;
import game.map.Map;

public class DigTool extends Tool{

	private static EquipmentVisual createEquipmentVisuals(EquipmentType type) {
		return new EquipmentVisual(type);
	}
	
	public DigTool() {
		super(ItemType.DigTool, createEquipmentVisuals(ItemType.DigTool));
	}

	@Override
	public void use(Entity user) {
		if(user instanceof Player){
			Player player = (Player)user;
			Location loc = new Location(Map.getMap().getXOver(	(int) Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX())/Map.DEFAULT_SQUARESIZE,
					 											(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
			if(Map.getMap().getChunks()[loc.getX()/Map.DEFAULT_CHUNKSIZE][loc.getY()/Map.DEFAULT_CHUNKSIZE].getMapData(loc.getX(), loc.getY())[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER] != null){
				player.getPlayerDrone().addInterActionLocation(0, loc);
			}
		}
	}
	

}
