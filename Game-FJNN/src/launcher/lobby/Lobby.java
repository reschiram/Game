package launcher.lobby;

import java.util.ArrayList;

import client.GameCM;
import client.GameCPM;
import client.LobbyCEM;
import data.DataPackage;
import data.PackageType;
import data.Tickable;
import game.GameManager;
import launcher.MapDownloader;
import tick.TickManager;

public class Lobby implements Tickable{
	
	public static final int playerStatus_Add = 0;
	public static final int playerStatus_Remove = 1;
	public static final int playerStatus_None = 2;
	
	private GameCM gameCM;
	private LobbyGUI lobbyGUI;
	private TickManager tickManager;
	private MapDownloader mapDownloader;
	
	private String playerUsername;	
	private ArrayList<String> players = new ArrayList<>();
	
	public Lobby(GameCM gameCM, String playerUsername) {
		this.gameCM = gameCM;
		this.playerUsername = playerUsername;
		
		this.lobbyGUI = new LobbyGUI(this);
		this.tickManager = new TickManager(this);
		
		this.mapDownloader = new MapDownloader(this);
		
		this.gameCM.getClientManager().getEventManager().registerClientMessageEventListener(new LobbyCEM(this), 4);
	}

	@Override
	public void tick() {
		this.gameCM.tick();
	}

	public LobbyGUI getGUI() {
		return lobbyGUI;
	}

	public MapDownloader getMapDownloader() {
		return mapDownloader;
	}

	public void sendStartGame() {
		try {
			this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(PackageType.readPackageData(GameCPM.DataPackage_StartGame, 0l)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		if(message.length() > GameCPM.maxMessageLength) return;
		try {
			this.gameCM.getClientManager().sendToServer(DataPackage.getPackage(PackageType.readPackageData(GameCPM.DataPackage_PlayerMessage, playerUsername, message)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.lobbyGUI.println("[" + playerUsername + "] : " + message);
	}

	public void updatPlayerStatus(String username, boolean isHost, int status) {
		if (status == playerStatus_Add) {
			if (players.contains(username)) {
				System.out.println("Error: Player-Add: Player already connected");
			} else {
				players.add(username);
				if (username.equals(this.playerUsername)) {
					this.lobbyGUI.enableStart(isHost);
				}
			}
		} else if (status == playerStatus_Remove) {
			if (!players.contains(username)) {
				System.out.println("Error: Player-Remove: Player not connected");
			} else {
				players.remove(username);
			}
		} else if (status == playerStatus_None) {
			this.lobbyGUI.enableStart(isHost);
		}
		this.lobbyGUI.updatePlayers(players);
	}

	public void startGame(long time) {
		if(this.mapDownloader.isFinished() && !this.mapDownloader.isLoaded()) {
			this.lobbyGUI.destroy();
			tickManager.kill();
			new GameManager(mapDownloader, gameCM);
		}
	}

}
