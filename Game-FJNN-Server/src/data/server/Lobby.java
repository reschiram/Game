package data.server;

import java.util.ArrayList;

import data.ServerMap;
import server.ValidatedUser;

public class Lobby {
	
	private ArrayList<ServerMap> maps = new ArrayList<>();
	private ArrayList<ValidatedUser> players = new ArrayList<>();
	
	private long id;
	
	public Lobby(long id) {
		this.id = id;
	}
	
	public void addPlayer(ValidatedUser player) {
		this.players.add(player);
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

}
