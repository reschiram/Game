package file;

import java.io.BufferedWriter;
import java.io.IOException;

import fileloader.FileLoader;
import fileloader.reader.FileReader;

public class File {

	 protected String path = "";
	 protected boolean absolute;
	 protected String content = "";
	 
	 private String format;
	 private Boolean save = false;
	 
	 public File(String path, boolean absolute, String format){
		 this.path = path;
		 this.absolute = absolute;
		 this.format = format;
	 }

	 public File(String path, boolean absolute, String format, String content){
		 this.path = path;
		 this.absolute = absolute;
		 this.content = content;
		 this.format = format;
	 }
	 	
	public String getPath(){
		return this.path;
	}
	
	protected void waitForSave(){
		if(save)synchronized (this.save) {
			try {
				save.wait();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	public void save(BufferedWriter bw, FileLoader loader) {
		this.save = true;
		
		saveData(bw, loader);
		
		synchronized (save) {
			save.notify();
			save = false;
		}
	}

	protected void saveData(BufferedWriter bw, FileLoader loader){
		try {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setContent(String content){
		waitForSave();
		this.content = content;
	}
	
	public Object getContent(){
		return content;
	}

	public FileReader getReader(){
		return new FileReader(format);
	}
		
	public boolean hasAbsolutePath(){
		return absolute;
	}
}
