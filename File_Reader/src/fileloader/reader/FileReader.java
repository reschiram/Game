package fileloader.reader;

import java.io.BufferedReader;
import java.io.IOException;

import file.File;

public class FileReader {
	
	public static KTV_Reader KTV_READER;
	public static CSV_Reader CSV_READER;
	
	public static void load(){
		KTV_READER = new KTV_Reader("ktv");
		CSV_READER = new CSV_Reader("csv");
	}

	private String end = "";
	
	public FileReader(String end){
		this.end = end;
	}

	public File read(String path, BufferedReader br, boolean absolute, String format) throws IOException{
		String content = "";
		String line;
		while((line = br.readLine()) != null){
			content+=line+System.getProperty("line.separator").charAt(0);
		}
		return new File(path, absolute, format, content);
	}

	public File createEmptyFile(String path, boolean absolute, String format){
		return new File(path, absolute, format);
	}
	
	public String getEnd() {
		return end;
	}

}
