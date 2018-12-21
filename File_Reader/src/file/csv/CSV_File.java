package file.csv;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import file.File;
import fileloader.FileLoader;
import fileloader.reader.FileReader;

public class CSV_File extends File{
	
	public static char SEPERATOR = ';';
	
	private HashMap<Point, String> content;
	private Point maxPos = new Point(-1, -1);

	public CSV_File(String path, HashMap<Point, String> content, boolean absolute) {
		super(path, absolute, FileReader.CSV_READER.getEnd());
		this.content = new HashMap<>();
		for(Point p: content.keySet())set(p, content.get(p));
	}
	
	public void set(Point p, String data){
		waitForSave();
		content.put(p, data);
		if(p.getX()>maxPos.getX())maxPos.x=(int) p.getX();
		if(p.getY()>maxPos.getY())maxPos.y=(int) p.getY();
	}
	
	public String get(Point p){
		waitForSave();
		if(!content.containsKey(p))return "";
		return content.get(p);
	}
	
	public void remove(Point p){
		waitForSave();
		content.remove(p);
		if(p.getX()==maxPos.getX())maxPos.x--;
		if(p.getY()==maxPos.getY())maxPos.y--;
	}
	
	public void removeLine(int y, boolean moveOthers){
		if(y<0 || y>maxPos.getY())return;
		waitForSave();
		for(int x = 0; x<=maxPos.getX(); x++){
			content.remove(new Point(x, y));
		}
		if(y==maxPos.getY())maxPos.y--;
		else if(moveOthers){
			for(int dy = y+1; dy<=maxPos.getY(); dy++){
				if(dy>0){
					for(int x = 0; x<=maxPos.getX(); x++){
						Point p = new Point(x, dy);
						String data = content.get(p);
						content.remove(p);
						content.put(new Point(x, dy-1), data);
					}
				}
			}
			maxPos.y--;
		}
	}
	
	@Override
	public HashMap<Point, String> getContent(){
		return content;
	}

	@Override
	protected void saveData(BufferedWriter bw, FileLoader loader) {
		for(int y = 0; y<=maxPos.getY(); y++){
			String row = "";
			for(int x = 0; x<=maxPos.getX(); x++){
				Point p = new Point(x, y);
						
				String data = "";
				if(content.containsKey(p))data = content.get(p);
				row+=data+SEPERATOR;
			}
			loader.writeInLine(bw, row);
			try {
				bw.newLine();
			} catch (IOException e) {
				System.out.println("Error while saving: "+path);
			}
		}
	}

	@Override
	public FileReader getReader() {
		return FileReader.CSV_READER;
	}
	
	public Point getMaxPosition(){
		return maxPos;
	}

}
