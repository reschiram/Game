package files;

import file.csv.CSV_File;
import file.ktv.KTV_File;

public class FileManager {
	
	public static KTV_File UUID;
	public static CSV_File MAP_TEST;
	public static KTV_File PLAYER;
	
	private static filemanager.FileManager FILEMANAGER;
	
	public static void Load(){
		FILEMANAGER = new filemanager.FileManager(false);
		
		UUID = 		FILEMANAGER.createNewKTVFile("UUID/UUID"			, false);
		MAP_TEST = 	FILEMANAGER.createNewCSVFile("maps/test"			, false);
		PLAYER = 	FILEMANAGER.createNewKTVFile("maps/entitys/player"	, false);
	}
	
	public static filemanager.FileManager getFileManager(){
		return FILEMANAGER;
	}

}
