package game.entity.player.playerDrone;

import Data.Location;
import game.entity.Entity;
import game.gridData.map.Mapdata;
import game.map.Map;

public class DDroneTarget extends DroneTarget{

	public DDroneTarget(Location location, Drone... drones) {
		super(location, drones);
		this.marker.setSpriteState(1);
	}
	
	@Override
	public boolean interact() {
		Mapdata data = Map.getMap().getMapData(blockLocation)[Entity.DEFAULT_ENTITY_UP + Map.DEFAULT_BUILDLAYER];
		if(data==null){
			end(data);
			return super.interact();
		}
		data.damage(1);
		if(data.isDestroyed()){
			end(data);
			this.done = true;
		}
		return super.interact();
	}

	private void end(Mapdata data) {
		if(data!=null && data.getResource().hasDrops()){
			data.getResource().drop(blockLocation);
		}
	}

}
