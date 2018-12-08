package server;

import java.util.ArrayList;
import java.util.HashMap;
import data.user.User;

public class ConnectionManager {
	
	private ArrayList<Long> unknownConnections = new ArrayList<>();
	private HashMap<Long, ValidatedUser> validatedUsers = new HashMap<>();
	
	ConnectionManager(){	
		
	}

	boolean validate(long serverClientID, User user) {
		if(validatedUsers.containsKey(serverClientID)){
			return false;
		}
		if(unknownConnections.contains(serverClientID)){
			unknownConnections.remove(serverClientID);
			validatedUsers.put(serverClientID, new ValidatedUser(serverClientID, user));
			return true;
		}
		return false;
	}

	void registerNewUnknownConnection(long serverClientID) {
		this.unknownConnections.add(serverClientID);
	}

	void logout(long clientID) {
		this.unknownConnections.remove(clientID);
		this.validatedUsers.remove(clientID);
	}
	
	ValidatedUser getValidatetUser(long clientID){
		if(this.validatedUsers.containsKey(clientID)){
			return this.validatedUsers.get(clientID);
		}
		return null;
	}
	
}
