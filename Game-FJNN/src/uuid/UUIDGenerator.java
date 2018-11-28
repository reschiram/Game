package uuid;

import java.util.ArrayList;
import java.util.UUID;

import files.FileManager;

public class UUIDGenerator {
	
	private static UUIDGenerator generator = null;
	public static UUIDGenerator getUUIDGenerator(){
		if(generator == null)generator = new UUIDGenerator();
		return UUIDGenerator.generator;
	}
	
	private int id = Integer.parseInt(FileManager.UUID.get("Anzahl").get(0));
	
	public UUID newUUID(int... pID){
		if(pID.length == 1&&id == pID[0])id++;
		int mID = id;
		if(pID.length == 1)mID = pID[0];
		String[] ids = FileManager.UUID.getSubkey("Used");
		boolean found = true;
		while(found){
			found = false;
			for(String s: ids) if(s.equals(mID+"")){
				if(pID[0] == mID)return null;
				id++;
				mID++;
				found = true;
			}
		}
		FileManager.UUID.set("Anzahl", id+"");
		UUID uuid = new UUID(0,mID);
		FileManager.UUID.add("Used."+mID, uuid.toString());
		return uuid;
	}
	
	public UUID getUUID(int id){
		ArrayList<String> uuid = FileManager.UUID.get("Used."+id);
		if(uuid!=null&&uuid.size()>0)return UUID.fromString(uuid.get(0));
		else return newUUID(id);
	}

}
