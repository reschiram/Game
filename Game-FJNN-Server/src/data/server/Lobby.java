package data.server;

import java.util.ArrayList;

import data.map.ServerMap;
import server.GameSM;
import server.ValidatedUser;

public class Lobby {
	
	private ArrayList<ServerMap> maps = new ArrayList<>();
	private ArrayList<Player> players = new ArrayList<>();
	
	private GameSM gameSM;
	private LobbyEM lobbyEM;
	
	private long id;
	
	public Lobby(long id, GameSM gameSM) {
		this.id = id;
		this.gameSM = gameSM;
		
		this.lobbyEM = new LobbyEM(this, gameSM);
	}
	
	public void addPlayer(ValidatedUser user) {
		Player player = new Player(user, this.players.size() == 0);
		player.setCurrentSMap(this.getCurrentMap());
		this.players.add(player);
		gameSM.sendMapToClient(user.getServerClientID(), this.getCurrentMap());
		
		lobbyEM.sendPlayerAdd(player);
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
			if(p.getID().equals(userID)) {
				players.remove(i);				
				lobbyEM.sendPlayerRemove(p);
				return;
			}
		}
	}

	public Player getPlayer(long clientID) {
		for(int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if(p.getServerClientID() == clientID) {
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
	
	public void startGame() {
		this.getCurrentMap().start(this, gameSM);
	}

	public void tick() {
		this.getCurrentMap().tick();
	}

}
