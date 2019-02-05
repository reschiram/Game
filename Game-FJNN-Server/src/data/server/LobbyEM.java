package data.server;

import data.DataPackage;
import data.PackageType;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.server.InvalidServerClientIDException;
import server.GameSM;
import server.GameSPM;

public class LobbyEM implements ToServerMessageEventListener{
	
	private Lobby lobby;
	private GameSM gameSM;

	public LobbyEM(Lobby lobby, GameSM gameSM) {
		this.lobby = lobby;
		this.gameSM = gameSM;
		
		gameSM.getServerManager().getEventManager().registerServerMessageEventListener(this, 4);
	}
	
	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		Player user = lobby.getPlayer(event.getClientID());
		if(user != null) {
			if(event.getMessage().getId() == GameSPM.DataPackage_PlayerMessage) {
				for(Player player : lobby.getConnectedPlayers()) {
					if(player.getServerClientID() != event.getClientID()) {
						try {
							gameSM.getServerManager().sendMessage(player.getServerClientID(), DataPackage.getPackage(event.getMessage()));
						} catch (InvalidServerClientIDException | Exception e) {
							e.printStackTrace();
						}
					}
				}
				event.setActive(false);
			} else if(event.getMessage().getId() == GameSPM.DataPackage_StartGame) {
				lobby.startGame();
				if(user.isHost()) {
					for(Player player : lobby.getConnectedPlayers()) {
						try {
							gameSM.getServerManager().sendMessage(player.getServerClientID(),
									DataPackage.getPackage(PackageType.readPackageData(
									GameSPM.DataPackage_StartGame, System.currentTimeMillis())))
							;
						} catch (InvalidServerClientIDException | Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void sendPlayerAdd(Player player) {
		for(Player cp : lobby.getConnectedPlayers()) {
			try {
				gameSM.getServerManager().sendMessage(cp.getServerClientID(),
						DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_LobbyPlayerStatus,
								player.getUsername(), player.isHost(), launcher.lobby.Lobby.playerStatus_Add)))
				;
				
				if(!player.getID().equals(cp.getID())) {
					gameSM.getServerManager().sendMessage(player.getServerClientID(),
						DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_LobbyPlayerStatus,
								cp.getUsername(), cp.isHost(), launcher.lobby.Lobby.playerStatus_Add)))
					;
					
					gameSM.getServerManager().sendMessage(cp.getServerClientID(),
							DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_PlayerMessage,
									"Server", "User: " + player.getUsername() + " has joined the lobby")))
					;
				}
			} catch (InvalidServerClientIDException | Exception e) {
				e.printStackTrace();
			}		
		}
	}

	public void sendPlayerRemove(Player player) {		
		Player newHost = null;		
		if(player.isHost() && lobby.getConnectedPlayers().size() > 0) {
			newHost = lobby.getConnectedPlayers().get(0);
			newHost.setHost(true);
		}
		
		for(Player cp : lobby.getConnectedPlayers()) {
			try {
				gameSM.getServerManager().sendMessage(cp.getServerClientID(),
						DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_LobbyPlayerStatus,
								player.getUsername(), player.isHost(), launcher.lobby.Lobby.playerStatus_Remove)))
				;
				
				if(!player.getID().equals(cp.getID())) {					
					gameSM.getServerManager().sendMessage(cp.getServerClientID(),
							DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_PlayerMessage,
									"Server", "User: " + player.getUsername() + " has left the lobby")))
					;
				}
				
				if(newHost != null) {
					try {
						gameSM.getServerManager().sendMessage(cp.getServerClientID(),
								DataPackage.getPackage(PackageType.readPackageData(GameSPM.DataPackage_LobbyPlayerStatus,
										newHost.getUsername(), newHost.isHost(), launcher.lobby.Lobby.playerStatus_None)))
						;
					} catch (InvalidServerClientIDException | Exception e) {
						e.printStackTrace();
					}
				}
			} catch (InvalidServerClientIDException | Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
