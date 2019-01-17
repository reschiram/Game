package main;

import java.util.ArrayList;

import data.ServerMap;
import data.server.Lobby;

public class ServerStructureManager {
	
	private ServerMain sm;	
	private Lobby lobby;
	
	public ServerStructureManager(ServerMain sm) {
		this.sm = sm;
	}

	private void loadLobby() {
		this.lobby = new Lobby(1l);
		
		loadMaps();
	}

	private void loadMaps() {
		ArrayList<String> maps = this.sm.getFileManager().getServerDataFile().get("maps");
		if(maps == null || maps.size() == 0) return;
		
		for(String mapName: maps) {
			lobby.addMap(new ServerMap(this.sm.getFileManager().getCSVFile("maps/"+mapName)));
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

}
