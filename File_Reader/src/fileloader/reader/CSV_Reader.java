package fileloader.reader;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import file.File;
import file.csv.CSV_File;

public class CSV_Reader extends FileReader{

	public CSV_Reader(String end) {
		super(end);
	}

	@Override
	public File read(String path, BufferedReader br, boolean absolute, String format) throws IOException {
		int row = 0;
		HashMap<Point, String> content = new HashMap<>();
		for(String line = br.readLine(); line!=null; line = br.readLine()){
			
			if(!line.equals("")){
				String[] lineData = line.split(""+CSV_File.SEPERATOR);
				
				for(int collum = 0; collum<lineData.length; collum++){
					content.put(new Point(collum, row), lineData[collum]);
				}
			}
			
			row++;
		}
		return new CSV_File(path, content, absolute);
	}

	@Override
	public File createEmptyFile(String path, boolean absolute, String format) {
		return new CSV_File(path, new HashMap<Point, String>(), absolute);
	}

}
