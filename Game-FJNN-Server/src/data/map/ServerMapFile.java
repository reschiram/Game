package data.map;

import java.awt.Point;
import java.util.ArrayList;

import data.Resource;
import file.csv.CSV_File;
import game.map.MapLoader;
import main.ServerFileManager;

public class ServerMapFile {
	
	private ServerMap serverMap;
	private CSV_File mapFile;
	
	public ServerMapFile(ServerMap serverMap, CSV_File mapFile) {
		this.serverMap = serverMap;
		this.mapFile = mapFile;
	}

	public void load() {
		serverMap.seed = Integer.parseInt(mapFile.get(new Point(1, 1)));
		
		int width = Integer.parseInt(mapFile.get(new Point(1, 2)));
		int height = Integer.parseInt(mapFile.get(new Point(1, 3)));		
		serverMap.width = width;
		serverMap.height = height;		
		
		serverMap.mapGround = new int[width][height][2];
		serverMap.mapBuild = new int[width][height][2];
		int idLength = Resource.noResourceID.length();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				String squareData = mapFile.get(new Point(x + 2, y));
				for (int i = 0; i < 4; i++) {
					int from = i * (idLength + 1);
					Integer id = Integer.parseInt(squareData.substring(from, from + idLength));
					if (id != 0) {
						if (i >= 2) serverMap.mapBuild[x][y][i-2] = id;
						else serverMap.mapGround[x][y][i] = id;
					}
				}

			}
		}
	}
	
	public void save(ServerFileManager serverFileManager) {

		mapFile.set(new Point(0, 0), "Datatype");
		mapFile.set(new Point(1, 0), "Data");

		mapFile.set(new Point(0, 1), "Seed");
		mapFile.set(new Point(1, 1), serverMap.seed + "");

		mapFile.set(new Point(0, 2), "Width");
		mapFile.set(new Point(1, 2), serverMap.width + "");

		mapFile.set(new Point(0, 3), "Height");
		mapFile.set(new Point(1, 3), serverMap.height + "");

		while (mapFile.getMaxPosition().getX() >= 2) mapFile.removeColumn((int) mapFile.getMaxPosition().getX(), true);

		for (int x = 0; x < serverMap.width; x++) {
			for (int y = 0; y < serverMap.height; y++) {

				String ground = "";
				for (int i = 0; i < serverMap.mapGround[x][y].length; i++) {
					if (serverMap.mapGround[x][y][i] == 0)
						ground += Resource.noResourceID + MapLoader.seperator;
					else
						ground += serverMap.mapGround[x][y][i] + MapLoader.seperator;
				}
				ground = ground.substring(0, ground.length() - 1);

				String build = "";
				for (int i = 0; i < serverMap.mapBuild[x][y].length; i++) {
					if (serverMap.mapBuild[x][y][i] == 0) build += Resource.noResourceID + MapLoader.seperator;
					else build += serverMap.mapBuild[x][y][i] + MapLoader.seperator;
				}
				build = build.substring(0, build.length() - 1);

				mapFile.set(new Point(x + 2, y), ground + MapLoader.seperator + build);
			}
		}

		serverFileManager.saveFile(mapFile);
		
		ArrayList<String> mapNames = serverFileManager.getServerDataFile().get("maps");
		if (mapNames == null || !mapNames.contains(this.getFileName())) {
			serverFileManager.getServerDataFile().add("maps", this.getFileName());
			serverFileManager.saveFile(serverFileManager.getServerDataFile());
		}
	}

	public boolean canLoad() {
		return this.mapFile.getMaxPosition().getX() > 1;
	}

	public String getFileName() {
		return this.mapFile.getFileName();
	}

}
