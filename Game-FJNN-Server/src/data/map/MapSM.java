package data.map;

import data.DataPackage;
import data.PackageType;
import data.SEPathManager;
import data.droneData.actionTarget.ActionTarget;
import data.entities.ServerDroneEntity;
import data.entities.ServerEntity;
import data.exceptions.server.InvalidServerClientIDException;
import data.server.Lobby;
import data.server.Player;
import server.GameSM;

public class MapSM {
	
	private Lobby lobby;
	private GameSM gameSM;
	
	public MapSM(Lobby lobby, GameSM gameSM) {
		this.lobby = lobby;
		this.gameSM = gameSM;
	}

	public void publishDroneEnergyUpdate(int droneID, double energyLoad, boolean isLoading) {
		try {
			PackageType msg = gameSM.getGameSPM().createDroneEnergyUpdateMessage(droneID, energyLoad, isLoading);
			
			for(Player player : lobby.getConnectedPlayers()) {
				gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(msg));
			}
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}
	}

	public void publishDroneTargetUpdate(SEPathManager sePathManager) {
		ServerEntity entity = sePathManager.getEntity();
		if(!(entity instanceof ServerDroneEntity)) return;
			
		try {
			PackageType msg = gameSM.getGameSPM().createDroneTargetUpdateMessage(entity.getId(), sePathManager.getCurrentBlockTarget(), sePathManager.getCurrentTargetLevel());
			
			for(Player player : lobby.getConnectedPlayers()) {
				gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(msg));
			}
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}
	}

	public void publishDroneCTUpdate(int droneID, ActionTarget nextTarget) {
		try {
			PackageType msg = gameSM.getGameSPM().createDroneActionTargetUpdateMessage(droneID, nextTarget.getKey(this.lobby.getCurrentMap()), nextTarget.getType().getTypeId());
			
			for(Player player : lobby.getConnectedPlayers()) {
				gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(msg));
			}
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}		
	}

}
