package server;

import java.util.HashMap;

import data.exceptions.LoginSecurityException;
import data.exceptions.LogoutSecurityException;
import data.permission.PermissionManager;

public class ServerSecurityManager {
	
	private ServerManager serverManager;
	private SecurityServerListener serverListener;
	private PermissionManager permissionManager;
	
	private HashMap<Long, ValidatedUser> validatedUsers = new HashMap<>();

	void login(ValidatedUser user) throws LoginSecurityException {
		if(validatedUsers.containsKey(user.getServerClientID())) throw new LoginSecurityException(null, user, validatedUsers.get(user.getServerClientID()));
		validatedUsers.put(user.getServerClientID(), user);
	}

	void logout(ValidatedUser user) throws LogoutSecurityException {
		if(!validatedUsers.containsKey(user.getServerClientID())) throw new LogoutSecurityException(null, user);
		validatedUsers.remove(user.getServerClientID());
	}
}
