package files;

import file.*;
import file.ktv.KTV_File;

public class FileManager {
	
	public static KTV_File UUID;
	public static KTV_File MAP_TEST;
	public static KTV_File PLAYER;
	
	private static filemanager.FileManager FILEMANAGER;
	
	public static void Load(){
		FILEMANAGER = new filemanager.FileManager(true);
		
		UUID = 		FILEMANAGER.createNewKTVFile("UUID/UUID"			, false);
		MAP_TEST = 	FILEMANAGER.createNewKTVFile("maps/test"			, false);
		PLAYER = 	FILEMANAGER.createNewKTVFile("maps/entitys/player"	, false);
	}
	
	public static filemanager.FileManager getFileManager(){
		return FILEMANAGER;
	}

}
