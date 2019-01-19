package server;

import client.GameCPM;
import data.PackageType;
import game.map.MapGenerationData;

public class GameSPM extends GameCPM{
	
	public GameSPM() {
		super();
	}
	
	public PackageType[] getMapMessages(MapGenerationData mapData) throws Exception {
		int width = mapData.getWidth();
		int height = mapData.getHeight();

		int messageCount = (int) Math.ceil(((double) (width * height)) / ((double) MapDownloadData_DataCount));

		PackageType[] data = new PackageType[messageCount + 1];
		data[0] = PackageType.readPackageData(DataPackage_MapDownloadInfos, width, height, mapData.getSeed(), messageCount);

		for (int i = 0; i < messageCount; i++) {
			Integer[] content = new Integer[1 + (MapDownloadData_DataCount * 4)];
			content[0] = i;
			for (int a = 0; a < MapDownloadData_DataCount; a++) {
				int index = (i * MapDownloadData_DataCount) + a;
				int y = index / width;
				int x = index - (y * width);
				
				if(x < width && y < height) {
					content[1 + (a * 4) + 0] = mapData.getGroundData()[x][y][0];
					content[1 + (a * 4) + 1] = mapData.getGroundData()[x][y][1];
					content[1 + (a * 4) + 2] = mapData.getBuildData ()[x][y][0];
					content[1 + (a * 4) + 3] = mapData.getBuildData ()[x][y][1];
				}else {
					content[1 + (a * 4) + 0] = 0;
					content[1 + (a * 4) + 1] = 0;
					content[1 + (a * 4) + 2] = 0;
					content[1 + (a * 4) + 3] = 0;
				}
			}
			data[i + 1] = PackageType.readPackageData(DataPackage_MapDownloadData, content); 
		}
		
		System.out.println("Test null PackageTypes: ");
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) System.out.println("Null: "+i);
			else {
				String message = "Null: "+i+" at:";
				for(int a = 0; a < data[i].getDataStructures().length; a++) {
					if(data[i].getDataStructures()[a] == null || data[i].getDataStructures()[a].getData() == null)message+=a+","; 
				}
				if(!message.equals("Null: "+i+" at:"))System.out.println(message);
			}
		}
		System.out.println("=======");
		
		return data;
	}

}