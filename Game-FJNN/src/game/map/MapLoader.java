package game.map;

import java.util.ArrayList;

import Data.Location;
import data.MapResource;
import file.ktv.KTV_File;
import files.FileManager;
import game.gridData.map.MapBlock;

public class MapLoader {
	
	private Map map;
	private KTV_File mapFile;
	
	private double progress = 0.0;
	
	public MapLoader(KTV_File f, int seed){
		this.mapFile = f;
		if(f.get("Seed")==null || f.get("Seed").size()==0)createMap(seed);
	}
	
	private void createMap(int seed) {
		this.map = new MapGenerator().generateMap(seed);
		saveMap();
	}
	
	public double getProgress(){
//		System.out.println(progress);
		return progress;
	}

	public void saveMap() {
		mapFile.set("Seed", this.map.getSeed()+"");
		mapFile.set("Size.Width", this.map.getWidth()+"");
		mapFile.set("Size.Height", this.map.getHeight()+"");
		mapFile.remove("Ground");
		mapFile.remove("Build");
		long max = this.map.getWidth()*this.map.getHeight()*2;
		long current = 0;
		for(int x = 0; x<this.map.getChunks().length; x++){
			for(int y = 0; y<this.map.getChunks()[x].length; y++){
				MapChunk chunk = this.map.getChunks()[x][y];
				for(int cx = 0; cx < Map.DEFAULT_CHUNKSIZE && cx+x*Map.DEFAULT_CHUNKSIZE<this.map.getWidth(); cx++){
					for(int cy = 0; cy < Map.DEFAULT_CHUNKSIZE && cy+y*Map.DEFAULT_CHUNKSIZE<this.map.getHeight(); cy++){
						int px = cx+x*Map.DEFAULT_CHUNKSIZE;
						int py = cy+y*Map.DEFAULT_CHUNKSIZE;
						
						mapFile.remove("Ground."+py+"."+px);
						if(chunk!= null && chunk.getGround()[cx][cy][0]!=null && chunk.getGround()[cx][cy][0] instanceof MapBlock)mapFile.add("Ground."+py+"."+px, chunk.getGround()[cx][cy][0].getResource().getID()+"");
						else mapFile.add("Ground."+py+"."+px, "0");
						if(chunk!= null && chunk.getGround()[cx][cy][1]!=null && chunk.getGround()[cx][cy][1] instanceof MapBlock)mapFile.add("Ground."+py+"."+px, chunk.getGround()[cx][cy][1].getResource().getID()+"");
						else mapFile.add("Ground."+py+"."+px, "0");
						current++;
						
						mapFile.remove("Build."+py+"."+px);
						if(chunk!= null && chunk.getBuild()[cx][cy][0]!=null && chunk.getBuild()[cx][cy][0] instanceof MapBlock)mapFile.add("Build."+py+"."+px, chunk.getBuild()[cx][cy][0].getResource().getID()+"");
						else mapFile.add("Build."+py+"."+px, "0");
						if(chunk!= null && chunk.getBuild()[cx][cy][1]!=null && chunk.getBuild()[cx][cy][1] instanceof MapBlock)mapFile.add("Build."+py+"."+px, chunk.getBuild()[cx][cy][1].getResource().getID()+"");
						else mapFile.add("Build."+py+"."+px, "0");
						current++;
//						System.out.println(current+"/"+max+" ->"+(((double)((double)current/(double)max))*100.0));
					}
				}
			}
		}
		FileManager.getFileManager().saveFile(mapFile);
	}

	public void loadMap() {
		this.map = new Map(Integer.parseInt(mapFile.get("Size.Width").get(0)), Integer.parseInt(mapFile.get("Size.Height").get(0)), Integer.parseInt(mapFile.get("Seed").get(0)));
		long max = this.map.getWidth()*this.map.getHeight()*4;
		long current = 0;
		for(String y: mapFile.getSubkey("Ground")){
			for(String x: mapFile.getSubkey("Ground."+y)){
				ArrayList<String> ids = mapFile.get("Ground."+y+"."+x);
				if(ids!=null && ids.size()>0){
					for(int i = 0; i< ids.size(); i++)if(!ids.get(i).equals("0")){
						map.addToGround(Integer.parseInt(ids.get(i)), Integer.parseInt(x), Integer.parseInt(y));
					}
				}
				current+=2;
				this.progress = ((double)current)/((double)max);
//				System.out.println(current);
			}
		}
		for(String y: mapFile.getSubkey("Build")){
			for(String x: mapFile.getSubkey("Build."+y)){
				ArrayList<String> ids = mapFile.get("Build."+y+"."+x);
				if(ids!=null && ids.size()>0){
					for(int i = 0; i< ids.size(); i++){
//						System.out.println(x+"|"+y);
						int id = Integer.parseInt(ids.get(i));
						if(id!=0){
							map.addToBuild(id, Integer.parseInt(x), Integer.parseInt(y));
						}
					}
				}
				current+=2;
				this.progress = ((double)current)/((double)max);
//				System.out.println(current);
			}
		}
		map.finalize();
//		System.out.println(max);
	}

	public Map getMap(){
		return map;
	}

}
