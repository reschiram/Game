package main;

import console.ConsoleManager;
import data.exceptions.server.ServerPortException;
import server.GameSM;

public class ServerMain {
	
	private ConsoleManager consoleManager;
	private GameSM gameSM;
	private ServerStructureManager sdm;
	private ServerFileManager fileManager = new ServerFileManager();
	
	public ServerMain(String generalPassword) {
		this.consoleManager = new ConsoleManager();
		this.gameSM = new GameSM(generalPassword);
		this.sdm = new ServerStructureManager(this);
		
		sdm.initServer();
		try {
			gameSM.openConnection();
		} catch (ServerPortException e) {
			e.printStackTrace();
		}
		
		new TickManager(this);
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

	public GameSM getGameSM() {
		return gameSM;
	}
	
	public ServerStructureManager getServerStructureManager() {
		return this.sdm;
	}
}
