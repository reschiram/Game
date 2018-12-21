package fileloader.reader;

import java.io.BufferedReader;
import java.io.IOException;

import file.File;
import file.ktv.KTV_File;
import file.ktv.Tree;

public class KTV_Reader extends FileReader{

	public KTV_Reader(String end) {
		super(end);
	}

	@Override
	public File read(String path, BufferedReader br, boolean absolute, String format) throws IOException {
		
		Tree<String> content = new Tree<>();
		
		String key = br.readLine();
		if(key==null){
			br.close();
			return new KTV_File(path, content, absolute);
		}
		key = key.substring(0, key.length()-1);
		content.add(key);
		int leer = 0;
		String line = "";
		for(int cn = br.read(); cn!=-1 && !System.getProperty("line.separator").contains(((char)cn)+""); cn = br.read()){
			line += (char)cn;
		}
		br.readLine();
		for(int c = 0; c!=-1;){
			c = br.read();
			
			if(line.length()==0) break;
			
			int nLeer = 0;
			while(line.length()>nLeer && line.charAt(nLeer) == ' ')nLeer++;
			line = line.substring(nLeer, line.length());
			while(line.charAt(line.length()-1) == ' ')line = line.substring(0, line.length()-1);
			
			if(leer<nLeer){
				if(line.charAt(line.length()-1) == ':'){
					key+="."+line.substring(0,line.length()-1);
					content.add(key);
				}else{
					content.add(key,line.substring(1,line.length()-1));
				}
			}else if( leer == nLeer){
				if(line.endsWith(":")){
					key+="."+line.substring(0,line.length()-1);
					content.add(key);
				}else content.add(key,line.substring(1,line.length()-1));
			}else if( leer > nLeer){
				for(int i = 0; i<leer-nLeer; i+=2){
					int m = key.length()-1;
					for(; m>0; m--){
						if(key.charAt(m) == '.'){
							key = key.substring(0, m);
							m = -1;
						}
					}
					if(m!=-2)key = "";
				}
				if(key.length()>0)key+=".";
				key+=line.substring(0,line.length()-1);
				content.add(key);
			}
			
			leer = nLeer;
			line = ((char)c)+"";
			for(int cn = br.read(); cn!=-1 && !System.getProperty("line.separator").contains(((char)cn)+""); cn = br.read()){
				line += (char)cn;
			}
			br.readLine();
		}
		
		return new KTV_File(path, content, absolute);
		
	}

	@Override
	public File createEmptyFile(String path, boolean absolute, String format) {
		return new KTV_File(path, new Tree<String>(), absolute);
	}

}
