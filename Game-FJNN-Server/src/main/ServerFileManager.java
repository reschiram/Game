package main;

import file.File;
import file.csv.CSV_File;
import file.ktv.KTV_File;
import filemanager.FileManager;

public class ServerFileManager {
	
	private FileManager fileManager;
	
	private KTV_File serverDataFile;
	
	public ServerFileManager() {
		this.fileManager = new FileManager(false);
	
		this.serverDataFile = fileManager.createNewKTVFile("serverData", false);
	}

	public KTV_File getServerDataFile() {
		return serverDataFile;
	}

	public void saveFile(File file) {
		this.fileManager.saveFile(file);
	}

	public CSV_File getCSVFile(String path) {
		return fileManager.createNewCSVFile(path, false);
	}

}
