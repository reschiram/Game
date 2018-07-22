package files;

import file.*;

public class FileManager {
	
	public static File UUID;
	public static File MAP_TEST;
	public static File PLAYER;
	
	private static file.FileManager FILEMANAGER;
	
	public static void Load(){
		FILEMANAGER = new file.FileManager(true);
		
		UUID = FILEMANAGER.createNewFile("UUID/UUID");
		MAP_TEST = FILEMANAGER.createNewFile("maps/test");
		PLAYER = FILEMANAGER.createNewFile("maps/entitys/player");
	}
	
	public static file.FileManager getFileManager(){
		return FILEMANAGER;
	}

}
