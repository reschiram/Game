package main;

import java.util.ArrayList;

import data.MapResource;
import data.map.ServerMap;
import data.server.Lobby;
import game.entity.type.EntityType;
import game.inventory.items.ItemType;
import server.ValidatedUser;
import sprites.Sprites;

public class ServerStructureManager {
	
	private ServerMain sm;	
	private Lobby lobby;
	
	public ServerStructureManager(ServerMain sm) {
		this.sm = sm;
		
		Sprites.create();
		ItemType.Load();
		MapResource.create();
		EntityType.create();
	}

	private void loadLobby() {
		this.lobby = new Lobby(1l, sm.getGameSM());
		
		loadMaps();
	}

	private void loadMaps() {
		ArrayList<String> maps = this.sm.getFileManager().getServerDataFile().get("maps");
		if(maps == null || maps.size() == 0) {
			ServerMap map = new ServerMap(this.sm.getFileManager().getCSVFile("maps/Test"));
			map.generateMap(39485636);
			map.getMapFile().save(sm.getFileManager());
			return;
		}
		
		for(String mapName: maps) {
			ServerMap map = new ServerMap(this.sm.getFileManager().getCSVFile("maps/"+mapName));
			map.getMapFile().load();
			lobby.addMap(map);
		}
		
	}

	public void initServer() {		
		loadLobby();	
		
		sm.getConsoleManager().getGUI().println(getLoadingMessage());
	}

	private String getLoadingMessage() {
		String message = "Lobby " + this.lobby.getId() + " has been succesfully loaded.";
		message += "\n" + "Maps found for Lobby " + this.lobby.getId() + " : " + "\n	";
		for(ServerMap map : this.lobby.getMaps()) {
			message += "[\"" + map.getName() + "\" with seed : " + map.getSeed() +"] ";
		}
		return message;
	}

	public void logOut(String userID) {
		this.lobby.removePlayer(userID);
	}

	public void logIn(ValidatedUser validatedUser) {
		this.lobby.addPlayer(validatedUser);		
	}

	public Lobby getLobby() {
		return lobby;
	}

	public void tick() {
		this.lobby.tick();
	}

}
