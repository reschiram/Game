package file.ktv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import file.File;
import fileloader.FileLoader;
import fileloader.reader.FileReader;

public class KTV_File extends File{
	
	private Tree<String> content = new Tree<>();
	
    public KTV_File(String path, Tree<String> content, boolean absolute){
		super(path, absolute, FileReader.KTV_READER.getEnd());
		this.content = content;
	}
    
	public void add(String key, String... obj){
		waitForSave();
		this.content.add(key, obj);
	}
	
	public void remove(String key, String... obj){
		waitForSave();		
		if(obj.length==0){
			this.content.remove(key);
		}else{
			for(String s: obj){
				this.content.remove(key, s);
			}
		}
	}

	@Override
	public Tree<String> getContent() {
		return content;
	}

	public ArrayList<String> get(String key) {
		return this.content.get(key);
	}
	
	public void set(String key, String... value){
		waitForSave();		
		this.content.set(key, value);
	}
	
	public String[] getSubkey(String key){
		return this.content.getSubKey(key);
	}

	@Override
	protected void saveData(BufferedWriter bw, FileLoader loader) {
		this.saveTree(bw, this.content.getStartNodes(), 0, loader);
	}
	
	private void saveTree(BufferedWriter bw, ArrayList<Node<String>> nodes, int in, FileLoader loader) {
		try{
			for(Node<String> node: nodes){
				for(int i = 0; i<in; i++)loader.writeInLine(bw, "  ");
				loader.writeInLine(bw, node.key+":");
				bw.newLine();
				for(String obj: node.save){
					for(int i = 0; i<in+1; i++)loader.writeInLine(bw, "  ");
					loader.writeInLine(bw, '"'+obj+'"');
					bw.newLine();
				}
				saveTree(bw, node.follow, in+1, loader);
			}
		}catch (IOException e) {
			System.out.println("Error: File could not be saved");
		}
	}

	@Override
	public FileReader getReader() {
		return FileReader.KTV_READER;
	}
}
