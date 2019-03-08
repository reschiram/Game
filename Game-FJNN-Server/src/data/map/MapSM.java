package data.map;

import data.DataPackage;
import data.PackageType;
import data.SEPathManager;
import data.droneData.actionTarget.ActionTarget;
import data.entities.ServerDroneEntity;
import data.entities.ServerEntity;
import data.entities.ServerItemEntity;
import data.exceptions.server.InvalidServerClientIDException;
import data.server.Lobby;
import data.server.Player;
import data.server.request.ServerEntityRequest;
import server.GameSM;

public class MapSM {
	
	private Lobby lobby;
	private GameSM gameSM;
	private MapSEM mapSEM;
	
	public MapSM(MapSEM mapSEM, Lobby lobby, GameSM gameSM) {
		this.lobby = lobby;
		this.gameSM = gameSM;
		this.mapSEM = mapSEM;
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

	public void publishDroneCTSelectionUpdate(int droneID, ActionTarget nextTarget) {
		try {
			PackageType msg = gameSM.getGameSPM().createDroneCTSelectionUpdateMessage(droneID, nextTarget.getKey(this.lobby.getCurrentMap()), nextTarget.getType().getTypeId());
			
			for(Player player : lobby.getConnectedPlayers()) {
				gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(msg));
				System.out.println("targetSelection published to: " + player.getID());
			}
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}		
	}

	public void publishDrop(ServerItemEntity drop) {
		ServerEntityRequest request = new ServerEntityRequest(-1, drop);
		mapSEM.publishRequest(-1l, request, -1, -1);
	}

	public void publishMapBlockUpdate(SMapBlock mapBlock) {
		try {
			PackageType msg = gameSM.getGameSPM().createMapBlockUpdateMessage(mapBlock);
			
			for(Player player : lobby.getConnectedPlayers()) {
				gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(msg));
				System.out.println("mapBlockUpdate published to: " + player.getID());
			}
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}		
	}

}
