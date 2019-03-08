package game.entity.player.playerDrone;

import Data.Location;
import client.ComsData;
import game.entity.Entity;
import game.gridData.map.Mapdata;
import game.map.Map;

public class DDroneTarget extends DroneTarget{

	public DDroneTarget(Location location, DroneHost master) {
		super(location, master, ComsData.ActionTarget_Type_Destroy);
		this.marker.setSpriteState(1);
	}

	@Override
	public boolean interact() {
		Mapdata data = Map.getMap().getChunks()[blockLocation.x / Map.DEFAULT_CHUNKSIZE] [blockLocation.y / Map.DEFAULT_CHUNKSIZE].getMapData(blockLocation, false)[Entity.DEFAULT_ENTITY_UP];
		if (data == null) {
			return super.interact();
		}
		data.damage(1);
		if (data.isDestroyed()) {
			this.done = true;
		}
		return super.interact();
	}

}
