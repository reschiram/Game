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
						
						if(chunk!= null && chunk.getGround()[cx][cy][0]!=null && chunk.getGround()[cx][cy][0] instanceof MapBlock)ground += chunk.getGround()[cx][cy][0].getResource().getID() + seperator;
						else ground += Resource.noResourceID + seperator;
						if(chunk!= null && chunk.getGround()[cx][cy][1]!=null && chunk.getGround()[cx][cy][1] instanceof MapBlock)ground += chunk.getGround()[cx][cy][1].getResource().getID();
						else ground += Resource.noResourceID;
						current++;
						
						String build = "";						
						
						if(chunk!= null && chunk.getBuild()[cx][cy][0]!=null && chunk.getBuild()[cx][cy][0] instanceof MapBlock)build += chunk.getBuild()[cx][cy][0].getResource().getID() + seperator;
						else build += Resource.noResourceID + seperator;
						if(chunk!= null && chunk.getBuild()[cx][cy][1]!=null && chunk.getBuild()[cx][cy][1] instanceof MapBlock)build += chunk.getBuild()[cx][cy][1].getResource().getID();
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
		this.map = new Map(Integer.parseInt(mapFile.get(new Point(1, 2))), Integer.parseInt(mapFile.get(new Point(1, 3))), Integer.parseInt(mapFile.get(new Point(1, 1))));
		
		long max = this.map.getWidth()*this.map.getHeight();
		long current = 0;
		
		int idLength = Resource.noResourceID.length();		
		
		for(int y = 0; y < map.getHeight(); y++){
			for(int x = 0; x < map.getWidth(); x++){
				
				String squareData = mapFile.get(new Point(x+2, y));		
				for(int i = 0; i < 4; i++) {
					int from = i*(idLength+1);
					Integer id = Integer.parseInt(squareData.substring(from, from+idLength)); 					
					if(id != 0) {
						if (i >= 2) {
							map.addToBuild(id, x, y, false);
						} else {
							map.addToGround(id, x, y, false);
						}
					}
				}
				
				current++;
				this.progress = ((double)current)/((double)max);
			}
		}
		
		map.finalize();
	}

	public Map getMap(){
		return map;
	}

}
