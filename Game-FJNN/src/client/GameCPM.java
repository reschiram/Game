package client;

import data.DataPackage;
import data.PackageType;
import data.readableData.IntegerData;
import data.readableData.ReadableData;

public class GameCPM {
	
	public static final int DataPackage_MapDownloadInfos = 14;
	public static final int DataPackage_MapDownloadData = 16;
	
	public static final int MapDownloadData_DataCount = 3;
	
	public GameCPM() {

	}
	
	public void loadPackages() {
		DataPackage.setType(new PackageType(DataPackage_MapDownloadInfos, "MapDownloadInfos", new IntegerData("width"), new IntegerData("height"), new IntegerData("seed"), new IntegerData("PackageCount")));
		
		ReadableData<?>[] content = new ReadableData<?>[1 + (MapDownloadData_DataCount * 4)];
		content[0] = new IntegerData("id");
		for(int i = 0; i < MapDownloadData_DataCount; i++) {
			int base = (i * 4) + 1;
			content[base] 		= new IntegerData("Ground(0|" 	+ String.format("%02d", i));
			content[base + 1] 	= new IntegerData("Ground(1|" 	+ String.format("%02d", i));
			content[base + 2] 	= new IntegerData("Build(0|" 	+ String.format("%02d", i));
			content[base + 3] 	= new IntegerData("Build(1|" 	+ String.format("%02d", i));
		}
		DataPackage.setType(new PackageType(DataPackage_MapDownloadData, "MapDownloadData", content));
	}

}
