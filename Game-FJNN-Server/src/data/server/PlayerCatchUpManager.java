package data.server;

import java.util.ArrayList;

import data.DataPackage;
import data.PackageType;
import data.entities.ServerDroneEntity;
import data.entities.ServerEntity;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.server.InvalidServerClientIDException;
import data.server.request.ServerEntityRequest;
import server.GameSPM;

public class PlayerCatchUpManager implements ToServerMessageEventListener{
	
	private Player player;
	private Lobby lobby;
	
	public PlayerCatchUpManager(Player player, Lobby lobby) {
		super();
		this.player = player;
		this.lobby = lobby;
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		if(event.getMessage().getId() == GameSPM.DataPackage_MapDownloadFinished) {
			startCatchUp();
		}
	}

	private void startCatchUp() {		
		ArrayList<ServerEntity> dependEntitys = new ArrayList<>();
		
		for(ServerEntity entity : lobby.getCurrentMap().getEntityManager().getAllEntitys()) {
			if(!this.player.knowsEntity(entity)) {
				if(entity instanceof ServerDroneEntity) {
					dependEntitys.add(entity);
				} else {
					sendEntityToPlayer(entity);
				}
			}
		}
		
		for(ServerEntity entity : dependEntitys) {
			if(!this.player.knowsEntity(entity)) {
				sendEntityToPlayer(entity);
			}
		}
		
		player.setHasCatchedUp(true);
	}

	private void sendEntityToPlayer(ServerEntity entity) {
		ServerEntityRequest request = new ServerEntityRequest(-1, entity);				
		try {
			PackageType message = lobby.getGameSM().getGameSPM().passRequestMessage(request);
			lobby.getGameSM().getServerManager().sendMessage(player.getValidatedUser().getServerClientID(), DataPackage.getPackage(message));
		} catch (Exception | InvalidServerClientIDException e) {
			e.printStackTrace();
		}
	}

}
