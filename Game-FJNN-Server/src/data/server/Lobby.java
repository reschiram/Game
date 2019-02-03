package data.server;

import java.util.ArrayList;

import data.map.ServerMap;
import server.GameSM;
import server.ValidatedUser;

public class Lobby {
	
	private ArrayList<ServerMap> maps = new ArrayList<>();
	private ArrayList<Player> players = new ArrayList<>();
	
	private GameSM gameSM;
	
	private long id;
	
	public Lobby(long id, GameSM gameSM) {
		this.id = id;
		new LobbyEM(this, gameSM);
		this.gameSM = gameSM;
	}
	
	public void addPlayer(ValidatedUser user) {
		Player p = new Player(user, false);
		this.players.add(p);
		
		gameSM.getServerManager().getEventManager().registerServerMessageEventListener(new PlayerCatchUpManager(p, this), 4);
	}

	public void addMap(ServerMap serverMap) {
		this.maps.add(serverMap);
	}

	public long getId() {
		return id;
	}

	public ArrayList<ServerMap> getMaps() {
		return this.maps;
	}

	public void removePlayer(String userID) {
		for(int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if(p.getValidatedUser().getID().equals(userID)) {
				players.remove(i);
				return;
			}
		}
	}

	public Player getPlayer(long clientID) {
		for(int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if(p.getValidatedUser().getServerClientID() == clientID) {
				return p;
			}
		}
		return null;
	}

	public ServerMap getMap(int seed) {
		for(int i = 0; i < maps.size(); i++) {
			if(maps.get(i).getSeed() == seed) return maps.get(i);
		}
		return null;
	}

	public ServerMap getCurrentMap() {
		return this.maps.get(0);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Player> getConnectedPlayers() {
		return (ArrayList<Player>) players.clone();
	}

	public GameSM getGameSM() {
		return gameSM;
	}

}
