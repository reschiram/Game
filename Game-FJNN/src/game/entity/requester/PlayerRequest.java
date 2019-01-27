package game.entity.requester;

import Data.Location;
import game.entity.player.Player;

public class PlayerRequest extends EntityRequest {

	public PlayerRequest(Location blockSpawn) {
		super(blockSpawn);
	}

	@Override
	public void spawnEntity(int entityID) {
		new Player(new Location(getPixelSpawn_X(), getPixelSpawn_Y()), entityID);
	}

}
