package game.entity.player.playerDrone.module;

import game.entity.player.playerDrone.DDroneTarget;
import game.entity.player.playerDrone.DroneTarget;

public class CTDModule extends CTModule{

	@Override
	public boolean addTarget(DroneTarget target) {
		if(target instanceof DDroneTarget)return super.addTarget(target);
		return false;
	}
}
