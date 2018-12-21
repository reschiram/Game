package filemanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import file.File;
import file.csv.CSV_File;
import file.ktv.KTV_File;
import fileloader.FileLoader;
import fileloader.ReadableFileLoader;
import fileloader.reader.FileReader;

public class FileManager {
	
	private FileLoader fileLoader;	
	
	public FileManager(boolean inResourcedataBank){
		this.fileLoader = new ReadableFileLoader(inResourcedataBank);
		FileReader.load();
	}
	
	public KTV_File createNewKTVFile(String path, boolean absolute) {
		return (KTV_File) this.fileLoader.readFile(path, FileReader.KTV_READER, absolute);
	}
	
	public CSV_File createNewCSVFile(String path, boolean absolute) {
		return (CSV_File) this.fileLoader.readFile(path, FileReader.CSV_READER, absolute);
	}
	
	public File createNewFile(String path, String format, boolean absolute) {
		return this.fileLoader.readFile(path, new FileReader(format), absolute);
	}
	
	public java.io.File createFile(String path, String format, boolean absolute){
		String filePath = fileLoader.getFilePath(path, FileReader.KTV_READER, absolute);
		filePath = filePath.substring(0, filePath.length()-4)+"."+format;
		
		String folderPath = "";
		for(int i = filePath.length()-1; i>0; i--){
			if(filePath.charAt(i)=='\\' || filePath.charAt(i)=='/'){
				folderPath = filePath.substring(0, i);
			}
		}
		
		java.io.File file = new java.io.File(filePath);
		if(!file.exists()){
			java.io.File folder = new java.io.File(folderPath);
			folder.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error while creating file -> "+path+"."+format);
			}
		}
		
		return file;
	}
	
	public File[] getKTVFillesInFolder(String path, boolean absolute){
		
		String pat = fileLoader.getFilePath(path, FileReader.KTV_READER, absolute);
		
		java.io.File[] list = new java.io.File(pat.substring(0, pat.length()-4)).listFiles();
		File[] files = new File[list.length];
		for(int i = 0; i<list.length;i++){
			String pn = "";
			if(fileLoader.isResourceDataBank){
				pn = list[i].getPath().substring(ClassLoader.getSystemResource("").getFile().length()-1, list[i].getPath().length()-4);
			}else{
				pn = list[i].getPath().substring(new java.io.File(fileLoader.getFilePath("", FileReader.KTV_READER, absolute)).getPath().length()-4, list[i].getPath().length()-4);
			}
			files[i] = this.createNewKTVFile(pn, false);
		}
		return files;
	}
	
	public ArrayList<java.io.File> getAllFillesInFolderData(String path, boolean absolute){
		String filePath = fileLoader.getFilePath(path, FileReader.KTV_READER, absolute);
		return getAllFillesInFolderData(filePath.substring(0, filePath.length()-4), new ArrayList<>(), absolute);
	}
	
	private ArrayList<java.io.File> getAllFillesInFolderData(String path, ArrayList<java.io.File> files, boolean absolute){		
		java.io.File[] list = new java.io.File(path).listFiles();
		for(java.io.File file: list){
			if(file.isFile()){
				files.add(file);
			}else if(file.isDirectory()){
				files.addAll(getAllFillesInFolderData(file.getAbsolutePath(), absolute));
			}
		}
		return files;
	}
	
	public void saveFile(File file){
		this.fileLoader.save(file);
	}
	
	public void deleteFile(File file){
		fileLoader.deleteFile(file);
	}

}
