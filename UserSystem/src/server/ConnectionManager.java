package server;

import java.util.ArrayList;
import java.util.HashMap;
import data.user.User;

public class ConnectionManager {
	
	private ArrayList<Long> unknownConnections = new ArrayList<>();
	private ArrayList<String> validatedOnlineUsers = new ArrayList<>();
	private HashMap<Long, ValidatedUser> validatedUsers = new HashMap<>();
	
	private boolean inUse = false;
	
	ConnectionManager(){	
		
	}

	boolean validate(long serverClientID, User user) {
		waitForInUse();

		if(validatedUsers.containsKey(serverClientID)){
			endInUse();
			return false;
		}else if(validatedOnlineUsers.contains(user.getID())){
			endInUse();
			return false;			
		}else if(unknownConnections.contains(serverClientID)){
			unknownConnections.remove(serverClientID);
			validatedUsers.put(serverClientID, new ValidatedUser(serverClientID, user));
			validatedOnlineUsers.add(user.getID());
			endInUse();
			return true;
		}
		
		endInUse();
		return false;
	}

	void registerNewUnknownConnection(long serverClientID) {
		waitForInUse();
		
		this.unknownConnections.add(serverClientID);
		
		endInUse();
	}

	void logout(long clientID) {
		waitForInUse();
		
		ValidatedUser user = this.getValidatetUserFunktion(clientID);
		if(user!=null){
			this.validatedUsers.remove(clientID);
			this.validatedOnlineUsers.remove(user.getID());
			this.unknownConnections.add(clientID);
		}
		
		endInUse();
	}
	
	void disconnect(long clientID){
		waitForInUse();
		
		this.unknownConnections.remove(clientID);
		ValidatedUser user = this.getValidatetUserFunktion(clientID);
		if(user!=null){
			this.validatedUsers.remove(clientID);
			this.validatedOnlineUsers.remove(user.getID());
		}
		
		endInUse();	
	}
	
	private ValidatedUser getValidatetUserFunktion(long clientID){		
		if(this.validatedUsers.containsKey(clientID)){
			ValidatedUser vu = this.validatedUsers.get(clientID);
			return vu;
		}
		return null;
	}
	
	ValidatedUser getValidatetUser(long clientID){
		waitForInUse();
		
		ValidatedUser vu = getValidatetUserFunktion(clientID);
		
		endInUse();
		return vu;
	}

	private void waitForInUse() {
		if(inUse){
			synchronized (unknownConnections) {
				try {
					unknownConnections.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}		
		
		inUse = true;
	}
	
	private void endInUse(){
		inUse = false;		
		
		synchronized (unknownConnections) {
			unknownConnections.notify();
		}		
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getAllValidatedOnlineUsers() {
		waitForInUse();
		
		ArrayList<String> validatedOnlineUsers = (ArrayList<String>) this.validatedOnlineUsers.clone();
		
		endInUse();		
		
		return validatedOnlineUsers;
	}
}
