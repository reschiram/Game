package main;

import java.util.ArrayList;

import data.MapResource;
import data.ServerMap;
import data.server.Lobby;
import server.ValidatedUser;
import sprites.Sprites;

public class ServerStructureManager {
	
	private ServerMain sm;	
	private Lobby lobby;
	
	public ServerStructureManager(ServerMain sm) {
		this.sm = sm;
		
		Sprites.create();
		MapResource.create();
	}

	private void loadLobby() {
		this.lobby = new Lobby(1l);
		
		loadMaps();
	}

	private void loadMaps() {
		ArrayList<String> maps = this.sm.getFileManager().getServerDataFile().get("maps");
		if(maps == null || maps.size() == 0) {
			ServerMap map = new ServerMap(this.sm.getFileManager().getCSVFile("maps/Test"));
			map.generateMap(39485636);
			map.save(sm.getFileManager());
			return;
		}
		
		for(String mapName: maps) {
			ServerMap map = new ServerMap(this.sm.getFileManager().getCSVFile("maps/"+mapName));
			map.load();
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

	public void logOut(ValidatedUser validatedUser) {
		this.lobby.removePlayer(validatedUser);
	}

	public void logIn(ValidatedUser validatedUser) {
		this.lobby.addPlayer(validatedUser);		
		
		System.out.println("login");
		
		sm.getGameSM().sendMapToClient(validatedUser.getServerClientID(), this.lobby.getMaps().get(0));
	}

	public Lobby getLobby() {
		return lobby;
	}

}
