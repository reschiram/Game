package game.vehicle;

import java.awt.Point;
import java.util.ArrayList;

import file.ktv.KTV_File;
import files.FileManager;
import data.VehicleResource;

public class BluePrint {
	
	public static ArrayList<BluePrint> BLUEPRINTS = new ArrayList<>();
	
	private int[][] vehicleData;
	private String name;
	private KTV_File file;
	
	public BluePrint(int width, int height, String name){
		this.vehicleData = new int[width][height];
		for(int x = 0; x<width; x++){
			for(int y = 0; y<height; y++){
				vehicleData[x][y] = VehicleResource.getNONID();
			}
		}
		this.name = name;
		file = FileManager.getFileManager().createNewKTVFile("BluePrints/"+name, false);
		save();
	}

	public BluePrint(KTV_File f) {
		this.name = f.get("Name").get(0);
		this.vehicleData = new int[Integer.parseInt(f.get("Size.Width").get(0))][Integer.parseInt(f.get("Size.Height").get(0))];
		for(int x = 0; x<vehicleData.length; x++){
			for(int y = 0; y<vehicleData[x].length; y++){
				vehicleData[x][y] = Integer.parseInt(f.get("Data."+x+"."+y).get(0));
			}
		}
		this.file = f;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return vehicleData.length;
	}

	public int getHeigth() {
		return vehicleData[0].length;
	}
	
	public void save(){
		file.set("Name", name);
		file.set("Size.Width" , vehicleData   .length+"");
		file.set("Size.Height", vehicleData[0].length+"");
		file.remove("Data");
		for(int x = 0; x<vehicleData.length; x++){
			for(int y = 0; y<vehicleData[x].length; y++){
				file.add("Data."+x+"."+y, vehicleData[x][y]+"");
			}
		}
		FileManager.getFileManager().saveFile(file);
	}

	public void set(Point loc, int id) {
		vehicleData[loc.x][loc.y] = id;
	}

	public int get(Point p) {
		return vehicleData[p.x][p.y];
	}

}
