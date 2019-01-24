package data;

import java.awt.Point;
import java.util.ArrayList;

import file.csv.CSV_File;
import game.map.MapGenerationData;
import game.map.MapGenerator;
import game.map.MapLoader;
import main.ServerFileManager;

public class ServerMap {
	
	private int[][][] mapGround;
	private int[][][] mapBuild;
	private int width;
	private int height;
	
	private int seed;
	private CSV_File mapFile;
	
	public ServerMap(CSV_File mapFile) {
		this.mapFile = mapFile;		
	}
	
	public boolean canLoad() {
		return this.mapFile.getMaxPosition().getX() > 1;
	}
	
	public void load() {
		this.seed = Integer.parseInt(mapFile.get(new Point(1, 1)));
		this.width = Integer.parseInt(mapFile.get(new Point(1, 2)));
		this.height = Integer.parseInt(mapFile.get(new Point(1, 3)));
		
		this.mapGround = new int[width][height][2];
		this.mapBuild = new int[width][height][2];
		int idLength = Resource.noResourceID.length();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				String squareData = mapFile.get(new Point(x + 2, y));
				for (int i = 0; i < 4; i++) {
					int from = i * (idLength + 1);
					Integer id = Integer.parseInt(squareData.substring(from, from + idLength));
					if (id != 0) {
						if (i >= 2) mapBuild[x][y][i-2] = id;
						else mapGround[x][y][i] = id;
					}
				}

			}
		}
	}
	
	public void generateMap(int seed) {
		MapGenerator generator = new MapGenerator();
		
		MapGenerationData data = generator.generateMapData(seed);
		this.width = data.getWidth();
		this.height = data.getHeight();
		this.seed = seed;
		
		this.mapBuild = data.getBuildData();
		this.mapGround = data.getGroundData();		
	}
	
	public void save(ServerFileManager serverFileManager) {

		mapFile.set(new Point(0, 0), "Datatype");
		mapFile.set(new Point(1, 0), "Data");

		mapFile.set(new Point(0, 1), "Seed");
		mapFile.set(new Point(1, 1), seed + "");

		mapFile.set(new Point(0, 2), "Width");
		mapFile.set(new Point(1, 2), width + "");

		mapFile.set(new Point(0, 3), "Height");
		mapFile.set(new Point(1, 3), height + "");

		while (mapFile.getMaxPosition().getX() >= 2) mapFile.removeColumn((int) mapFile.getMaxPosition().getX(), true);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				String ground = "";
				for (int i = 0; i < mapGround[x][y].length; i++) {
					if (mapGround[x][y][i] == 0)
						ground += Resource.noResourceID + MapLoader.seperator;
					else
						ground += mapGround[x][y][i] + MapLoader.seperator;
				}
				ground = ground.substring(0, ground.length() - 1);

				String build = "";
				for (int i = 0; i < mapBuild[x][y].length; i++) {
					if (mapBuild[x][y][i] == 0) build += Resource.noResourceID + MapLoader.seperator;
					else build += mapBuild[x][y][i] + MapLoader.seperator;
				}
				build = build.substring(0, build.length() - 1);

				mapFile.set(new Point(x + 2, y), ground + MapLoader.seperator + build);
			}
		}

		serverFileManager.saveFile(mapFile);
		
		ArrayList<String> mapNames = serverFileManager.getServerDataFile().get("maps");
		if (mapNames == null || !mapNames.contains(this.getName())) {
			serverFileManager.getServerDataFile().add("maps", this.getName());
			serverFileManager.saveFile(serverFileManager.getServerDataFile());
		}
	}

	public String getName() {
		return this.mapFile.getFileName();
	}

	public int getSeed() {
		return seed;
	}

	public MapGenerationData getGenerationData() {
		return new MapGenerationData(mapGround, mapBuild, seed);
	}

}
