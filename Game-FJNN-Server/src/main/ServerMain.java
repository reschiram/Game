package main;

import console.ConsoleManager;
import server.GameSM;

public class ServerMain {
	
	private ConsoleManager consoleManager;
	private GameSM gameSM;
	private ServerStructureManager sdm;
	private ServerFileManager fileManager = new ServerFileManager();
	
	public ServerMain(String generalPassword) {
		this.consoleManager = new ConsoleManager();
		this.gameSM = new GameSM(generalPassword);
		
		sdm.initServer();
	}

	public void tick() {
		this.gameSM.tick();
	}

	public ServerFileManager getFileManager() {
		return fileManager;
	}

	public ConsoleManager getConsoleManager() {
		return consoleManager;		
	}
}
