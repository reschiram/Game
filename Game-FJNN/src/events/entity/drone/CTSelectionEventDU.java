package events.entity.drone;

import game.entity.player.playerDrone.Drone;

public class CTSelectionEventDU extends DroneUpdateEvent{

	private int targetKey;
	private int targetType;

	public CTSelectionEventDU(Drone drone, int targetKey, int targetType) {
		super(drone);
		
		this.targetKey = targetKey;
		this.targetType = targetType;
	}

	public int getTargetKey() {
		return targetKey;
	}

	public int getTargetType() {
		return targetType;
	}
}
