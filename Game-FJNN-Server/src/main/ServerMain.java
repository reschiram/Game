package main;

import console.ConsoleManager;
import data.Tickable;
import data.exceptions.server.ServerPortException;
import server.GameSEM;
import server.GameSM;
import tick.TickManager;

public class ServerMain implements Tickable{
	
	private static ServerMain serverMain;
	public static ServerMain getServerMain() {
		return serverMain;
	}
	
	private ConsoleManager consoleManager;
	private GameSM gameSM;
	private ServerStructureManager sdm;
	private ServerFileManager fileManager = new ServerFileManager();
	private TickManager tickManager;
	
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
		
		new GameSEM(this);
		
		serverMain = this;
		
		tickManager = new TickManager(this);
		tickManager.release();
	}

	@Override
	public void tick() {
		this.gameSM.tick();
		this.consoleManager.tick();
		
		sdm.tick();
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
