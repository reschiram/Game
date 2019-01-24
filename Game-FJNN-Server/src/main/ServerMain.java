package main;

import console.ConsoleManager;
import data.ServerMap;
import data.Tickable;
import data.exceptions.server.ServerPortException;
import server.GameSEM;
import server.GameSM;
import tick.TickManager;

public class ServerMain implements Tickable{
	
	private ConsoleManager consoleManager;
	private GameSM gameSM;
	private ServerStructureManager sdm;
	private ServerFileManager fileManager = new ServerFileManager();
	private GameSEM gameSEM;
	
	public ServerMain(String generalPassword) {
		this.consoleManager = new ConsoleManager(this);
		this.gameSM = new GameSM(generalPassword);
		this.sdm = new ServerStructureManager(this);
		
		sdm.initServer();
		try {
			gameSM.openConnection();
		} catch (ServerPortException e) {
			e.printStackTrace();
		}
		
		this.gameSEM = new GameSEM(this);
		new TickManager(this);
	}

	public void tick() {
		this.gameSM.tick();
		this.consoleManager.tick();
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
