package data.db;

import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import data.DeEnCode;
import data.user.User;
import data.user.UserService;
import file.csv.CSV_File;
import filemanager.FileManager;

public class InternalFilemanager {
	
	private static final String LineSeparator1 = "{LineSeparatot:10->                              }";
	private static final byte LS1 = 10;
	private static final String LineSeparator2 = "{LineSeparatot:13->                              }";
	private static final byte LS2 = 13;
	private static final String EntrySeparator = "{EntrySeparatot:59->                             }";
	private static final byte ES = 59;
	
	private FileManager fileManager;
	private DeEnCode deEncode;
	
	private String generalPassword;
	private CSV_File data;
	
	public InternalFilemanager(String generalPassword){
		this.generalPassword = generalPassword;
		this.deEncode = new DeEnCode();
		this.fileManager = new FileManager(false);	
		loadData();
	}

	private void loadData() {					
		this.data = this.fileManager.createNewCSVFile("userdb/data", false);

		if(this.data.getMaxPosition().getX() == -1 && this.data.getMaxPosition().getY() == -1){
			this.data.set(new Point(0, 0), "ID");
			this.data.set(new Point(1, 0), "Username");
			this.data.set(new Point(2, 0), "Password");
			this.fileManager.saveFile(data);
		}
	}

	public User saveUser(User user) {
		if(user.getID() == null || user.getID().equals(""))generateID(user);
		int position = getUserPosition(user.getID());
		
		this.data.set(new Point(0, position), user.getID());
		this.data.set(new Point(1, position), getEncodedData(user.getUsername()));
		this.data.set(new Point(2, position), getEncodedData(user.getPassword()));
		
		this.fileManager.saveFile(data);
		
		return user;
	}
	
	public User getUser(String id){
		int position = getUserPosition(id);
		if(position!=this.data.getMaxPosition().getY()+1) return null;
		else{
			String username = getDecodedData(this.data.get(new Point(1, position)));
			String password = getDecodedData(this.data.get(new Point(2, position)));
			if(!username.equals("") && !password.equals("")){
				return new User(id, username, password);
			}
		}
		return null;
	}
	
	public ArrayList<User> getAllUser(){
		ArrayList<User> allUser = new ArrayList<>();
		
		for(int y = 1; y<=this.data.getMaxPosition().getY(); y++){
			try{
				String id = this.data.get(new Point(0, y));
				String username = getDecodedData(this.data.get(new Point(1, y)));
				String password = getDecodedData(this.data.get(new Point(2, y)));
				
				if(!id.equals("") && !username.equals("") && !password.equals("")) allUser.add(new User(id, username, password));
			}catch (Exception e) {
				System.out.println("An error occured while trying to access user:"+y);
			}
		}
		
		return allUser;
	}

	private String getDecodedData(String data) {
		try {
			String encoded = ""+data;
			String encodedData = "";
			char[] chars = encoded.toCharArray();
			for(int i = 0; i<chars.length; i++){
				char c = chars[i];
				if(c=='{'){
					if(i+LineSeparator1.length()<encoded.length()){
						String s = encoded.substring(i, i+LineSeparator1.length());
						if(s.equals(LineSeparator1)){
							encoded = encoded.substring(0, i)+new String(new byte[]{LS1}, UserService.StringFormat)+encoded.substring(i+LineSeparator1.length());
							chars = encoded.toCharArray();
							encodedData+=new String(new byte[]{LS1}, UserService.StringFormat);
						}else if(s.equals(LineSeparator2)){
							encoded = encoded.substring(0, i)+new String(new byte[]{LS2}, UserService.StringFormat)+encoded.substring(i+LineSeparator1.length());
							chars = encoded.toCharArray();
							encodedData+=new String(new byte[]{LS2}, UserService.StringFormat);
						}else if(s.equals(EntrySeparator)){
							encoded = encoded.substring(0, i)+new String(new byte[]{ES}, UserService.StringFormat)+encoded.substring(i+LineSeparator1.length());
							chars = encoded.toCharArray();
							encodedData+=new String(new byte[]{ES}, UserService.StringFormat);
						}else encodedData+=c;
					}else encodedData+=c;
				}else encodedData+=c;
			}
			return this.deEncode.decode(encodedData, generalPassword);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error while decoding data: "+data);
		}
		return "";
	}

	private String getEncodedData(String data) {
		try {
			String encoded = this.deEncode.encode(data, generalPassword);
			String encodedData = "";
			byte[] dataBytes = encoded.getBytes(UserService.StringFormat);
			for(int i = 0; i<dataBytes.length; i++){
				if(dataBytes[i] == LS1){
					encodedData+=LineSeparator1;
				}else if(dataBytes[i] == LS2){
					encodedData+=LineSeparator2;
				}else if(dataBytes[i] == ES){
					encodedData+=EntrySeparator;
				}else encodedData+= new String(new byte[]{dataBytes[i]}, UserService.StringFormat);
			}
			return encodedData;
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error while encoding Data: "+data);
		}
		return "";
	}

	private int getUserPosition(String userID) {
		String id = userID;		
		for(int y = 1; y<=this.data.getMaxPosition().getY(); y++){
			String existendUserID = this.data.get(new Point(0, y));
			if(id.equals(existendUserID))return y;
		}
		return (int) this.data.getMaxPosition().getY()+1;
	}

	private void generateID(User user) {
		String id = "";
		boolean found = false;
		while(!found){
			id = "";
			for(int x = 0; x<User.ID_Length; x++){
				id+=((int)(Math.random()*10));
			}
			if(getUserPosition(id)==this.data.getMaxPosition().getY()+1)found = true;
		}
		user.setID(id);
	}

}
