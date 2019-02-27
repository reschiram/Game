package game.map;

import java.awt.Point;

import data.Resource;
import file.csv.CSV_File;
import files.FileManager;
import game.gridData.map.MapBlock;

public class MapLoader {
	
	public static String seperator = "|";
	
	private Map map;
	private CSV_File mapFile;
	
	private double progress = 0.0;
	
	public MapLoader(CSV_File f, int seed){
		this.mapFile = f;
		if(f.getMaxPosition().getX()<10)createMap(seed);
	}
	
	private void createMap(int seed) {
		this.map = new MapGenerator().generateMap(seed);
		saveMap();
	}
	
	public double getProgress(){
		return progress;
	}

	public void saveMap() {
		mapFile.set(new Point(0, 0), "Datatype");
		mapFile.set(new Point(1, 0), "Data");
		
		mapFile.set(new Point(0, 1), "Seed");
		mapFile.set(new Point(1, 1), this.map.getSeed()+"");

		mapFile.set(new Point(0, 2), "Width");
		mapFile.set(new Point(1, 2), this.map.getWidth()+"");
		
		mapFile.set(new Point(0, 3), "Height");
		mapFile.set(new Point(1, 3), this.map.getHeight()+"");
		
		while(mapFile.getMaxPosition().getX() >= 2)mapFile.removeColumn((int) mapFile.getMaxPosition().getX(), true);
		
		long max = this.map.getWidth()*this.map.getHeight()*2;
		long current = 0;
		
		for(int x = 0; x<this.map.getChunks().length; x++){
			for(int y = 0; y<this.map.getChunks()[x].length; y++){
				MapChunk chunk = this.map.getChunks()[x][y];
				for(int cx = 0; cx < Map.DEFAULT_CHUNKSIZE && cx+x*Map.DEFAULT_CHUNKSIZE<this.map.getWidth(); cx++){
					for(int cy = 0; cy < Map.DEFAULT_CHUNKSIZE && cy+y*Map.DEFAULT_CHUNKSIZE<this.map.getHeight(); cy++){
						int px = cx+x*Map.DEFAULT_CHUNKSIZE;
						int py = cy+y*Map.DEFAULT_CHUNKSIZE;
						
						String ground = "";						
						
						if(chunk!= null && chunk.getInfo()[cx][cy][0] != 0)ground += chunk.getInfo()[cx][cy][0] + seperator;
						else ground += Resource.noResourceID + seperator;
						if(chunk!= null && chunk.getInfo()[cx][cy][1] != 0)ground += chunk.getInfo()[cx][cy][1];
						else ground += Resource.noResourceID;
						current++;
						
						String build = "";						

						if(chunk!= null && chunk.getInfo()[cx][cy][2] != 0)build += chunk.getInfo()[cx][cy][2] + seperator;
						else build += Resource.noResourceID + seperator;
						if(chunk!= null && chunk.getInfo()[cx][cy][3] != 0)build += chunk.getInfo()[cx][cy][3];
						else build += Resource.noResourceID;
						current++;
						
						mapFile.set(new Point(px+2, py), ground + seperator + build);
						
						this.progress = ((double)current)/((double)max);
					}
				}
			}
		}
		FileManager.getFileManager().saveFile(mapFile);
	}

	public void loadMap() {

		int seed = Integer.parseInt(mapFile.get(new Point(1, 1)));
		int width = Integer.parseInt(mapFile.get(new Point(1, 2)));
		int height = Integer.parseInt(mapFile.get(new Point(1, 3)));
		
		long max = width*height;
		long current = 0;
		
		int[][][] mapInfo = new int[width][height][4];
		
		int idLength = Resource.noResourceID.length();		
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				
				String squareData = mapFile.get(new Point(x+2, y));	
				
				for(int i = 0; i < 4; i++) {
					int from = i*(idLength+1);
					Integer id = Integer.parseInt(squareData.substring(from, from+idLength)); 					
					mapInfo[x][y][i] = id;
				}
				
				current++;
				this.progress = ((double)current)/((double)max);
			}
		}
		
		this.map = new Map(mapInfo, seed);
	}

	public Map getMap(){
		return map;
	}

}
