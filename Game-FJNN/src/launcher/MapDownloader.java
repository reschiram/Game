package launcher;

import client.GameCPM;
import game.GameManager;
import game.map.Map;
import game.map.MapGenerationData;
import launcher.lobby.Lobby;
import menu.LoadScreen;

public class MapDownloader {

	private Lobby launcher;

	private MapGenerationData mapData;
	private int packageAmount;

	private int currentPackage = -1;
	private boolean loaded = false;

	public MapDownloader(Lobby lobby) {
		this.launcher = lobby;
	}

	public MapDownloader load(int width, int height, int seed, int packageAmount) {
		if (isFinished() || mapData != null) {
			launcher.getGUI().println("Tried to start downloading Map(" + seed + "|" + width + "|" + height + "|"
					+ packageAmount + "), but Map is already downloaded ord currently downloading.");
			return this;
		}
		launcher.getGUI().println("Start Download of Map: (" + seed + "|" + width + "|" + height + "|" + packageAmount + ")");

		this.mapData = new MapGenerationData(new int[width][height][2], new int[width][height][2], seed);
		this.packageAmount = packageAmount;
		return this;
	}

	public boolean addData(int[] data, int dataID) {
		if (isFinished() || inavlidID(dataID)) {
			launcher.getGUI().println("MapData " + dataID + " was already downloaded");
			return false;
		}
		
		if(dataID > currentPackage+1) {
			System.out.println("Messages lost: " + currentPackage + " -> " + dataID);
			return false;
		}

		int index = dataID * GameCPM.MapDownloadData_DataCount;
		for (int i = 0; i < GameCPM.MapDownloadData_DataCount; i++) {
			int y = (index + i) / mapData.getHeight();
			int x = (index + i) - (y * mapData.getHeight());

			if (x < mapData.getWidth() && y < mapData.getHeight()) {
				mapData.getGroundData()[x][y][0] = data[(i * 4) + 0];
				mapData.getGroundData()[x][y][1] = data[(i * 4) + 1];
				mapData.getBuildData ()[x][y][0] = data[(i * 4) + 2];
				mapData.getBuildData ()[x][y][1] = data[(i * 4) + 3];
			}
		}

		if(currentPackage < dataID) this.currentPackage = dataID;

//		System.out.println("Download: (" + (dataID + 1) + " / " + packageAmount + ") ~ "
//				+ (((double) (dataID + 1)) / ((double) packageAmount)) + " => isFinished: " + isFinished());

		this.launcher.getGUI().updateProgressBar((((double) (dataID + 1)) / ((double) packageAmount)));
		
		if (isFinished()) {
			launcher.getGUI().println("Map: (" + mapData.getSeed() + "|" + mapData.getWidth() + "|"
					+ mapData.getHeight() + "|" + packageAmount + ") was already downloaded");			
		}
		return true;
	}

	private boolean inavlidID(int dataID) {
		if (currentPackage <= dataID) return false;
		return true;
	}

	public boolean isFinished() {
		return currentPackage == packageAmount - 1 && currentPackage != -1;
	}

	public Map getMap() {
		this.loaded = true;
		Map map = new Map(mapData.getWidth(), mapData.getHeight(), mapData.getSeed());
		double currentProgress = LoadScreen.loadScreen.getProgress();
		double max = map.getWidth() * map.getHeight();
		double current = 0;
		for(int x = 0; x < map.getWidth(); x++) {
			for(int y = 0; y < map.getHeight(); y++) {
				map.addDirect(mapData.getGroundData()[x][y][0] , x, y, false);
				map.addDirect(mapData.getGroundData()[x][y][1] , x, y, false);
				map.addDirect(mapData.getBuildData ()[x][y][0] , x, y, false);
				map.addDirect(mapData.getBuildData ()[x][y][1] , x, y, false);
				current++;
				LoadScreen.loadScreen.setProgress(currentProgress + (current / max));
			}
		}
		map.finalize();
		return map;
	}

	public boolean isLoaded() {
		return loaded;
	}

}
