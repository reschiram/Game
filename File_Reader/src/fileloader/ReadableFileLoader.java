package fileloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

import fileloader.reader.FileReader;
import file.File;

public class ReadableFileLoader extends FileLoader{
	
	public ReadableFileLoader(boolean isResourceDataBank){
		super(isResourceDataBank);
		Timer timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				while(saveFile.size()>0){
					File file = saveFile.get(0);
					
					String path = getFilePath(file.getPath(), file.getReader(), file.hasAbsolutePath());
					if(path.equals("")){
						saveFile.remove(0);
						return;
					}
					System.out.println("Save->"+path);
					java.io.File f = new java.io.File(path);
					
					if(!f.exists()){
						f.getParentFile().mkdirs();
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					BufferedWriter bw;
					
					try {
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
						
						file.save(bw, getThis());
						
						bw.close();
						saveFile.remove(0);
						
					}catch (IOException e) {
						e.printStackTrace();
						System.out.println("Error: File not found");}
				} 
			}
			
		};
		timer.schedule(timerTask, 1000, 1000);
	}

	private FileLoader getThis() {
		return this;
	}
	
	@Override
	public void writeInLine(BufferedWriter bw, String text){
		try{
			for(char c:text.toCharArray()){
				bw.write(c);
			}
		}catch (Exception e) {
			System.out.println("Error: Error while writting to file");
		}
	}

	@Override
	public File readFile(String path, FileReader reader, boolean absolute) {		
		try {			
			String p = getFilePath(path, reader, absolute);
			if(path.equals(""))return null;
						
			System.out.println("Load-> "+path+" -> "+p);
			java.io.File f = new java.io.File(p);
			
			if(!f.exists()){
				System.out.println("NEW FILE");
				f.getParentFile().mkdirs();
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			
			File file = reader.read(path, br, absolute, reader.getEnd());

			br.close();
			
			return file;
		} catch (IOException e) {
			System.out.println("Error: File does not exist");
		}
		
		return reader.createEmptyFile(path, absolute, reader.getEnd());
	}	
	
	public void kill(){
		timerTask.cancel();
	}

	@Override
	public void deleteFile(File file) {
		java.io.File f = new java.io.File(this.getFilePath(file.getPath(), file.getReader(), file.hasAbsolutePath()));
		System.out.println("File: "+file.getPath()+" was deleted: "+f.delete());
	}
}
