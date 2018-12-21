package fileloader;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.TimerTask;

import file.File;
import fileloader.reader.FileReader;

public abstract class FileLoader {
	
	protected ArrayList<File> saveFile = new ArrayList<>();
	protected TimerTask timerTask;
	public boolean isResourceDataBank;
	
	
	public FileLoader(boolean isResourceDataBank){
		this.isResourceDataBank = isResourceDataBank;
	}	
	
	public abstract File readFile(String path, FileReader reader, boolean absolute);

	public void save(File file) {
		if(!this.saveFile.contains(file))this.saveFile.add(file);
	}

	public abstract void deleteFile(File file);
	
	public String getFilePath(String path, FileReader reader, boolean absolute){
		System.out.println(reader.getEnd());
		String Filepath = "";
		if(isResourceDataBank){
			Filepath = ClassLoader.getSystemResource("").getFile().substring(0,ClassLoader.getSystemResource("").getFile().length()-4)+"res/"+path+"."+reader.getEnd();
		}else{
			if(absolute) Filepath = path+"."+reader.getEnd();
			else Filepath = "./data/"+path+"."+reader.getEnd();
		}
		if(Filepath.equals("")){
			System.out.println("Error while accessing File: "+path+"."+reader.getEnd() + " File could not be found");
			return "";
		}else return Filepath;
	}

	public abstract void writeInLine(BufferedWriter bw, String string);

}
