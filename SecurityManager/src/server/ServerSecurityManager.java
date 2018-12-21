package server;

import java.util.ArrayList;
import java.util.HashMap;

import data.SecurityPackageManager;
import data.exceptions.LoginSecurityException;
import data.exceptions.LogoutSecurityException;
import data.exceptions.SecurityPackageCreationException;
import data.permission.Permission;
import data.permission.PermissionGroup;
import data.permission.Permissiondb;

public class ServerSecurityManager {
	private ServerManager serverManager;
	
	private HashMap<Long, ValidatedUser> validatedUsers = new HashMap<>();
	private Permissiondb permissiondb;	
	
	public ServerSecurityManager(ServerManager serverManager, ServerUserManager serverUserManager){
		this.serverManager = new ServerManager();
		new SecurityServerListener(serverUserManager, this);
		
		this.permissiondb = new Permissiondb();
	}

	void login(ValidatedUser user) throws LoginSecurityException {
		if(validatedUsers.containsKey(user.getServerClientID())) throw new LoginSecurityException(null, user, validatedUsers.get(user.getServerClientID()));
		validatedUsers.put(user.getServerClientID(), user);
	}

	void logout(ValidatedUser user) throws LogoutSecurityException {
		if(!validatedUsers.containsKey(user.getServerClientID())) throw new LogoutSecurityException(null, user);
		validatedUsers.remove(user.getServerClientID());
	}
	
	public Permissiondb getPermissiondb(){
		return this.permissiondb;
	}
	
	public boolean hasPermission(String userID, Permission permission){
		ArrayList<PermissionGroup> permissionGroups = this.permissiondb.getPermissionGroups(userID);
		for(PermissionGroup pg: permissionGroups){
			if(pg.getPermissions().contains(permission)) return true;
		}
		return false;
	}
	
	public void sendAccessDenied(long clientID, int securityPackageID, String noPermissionExceptionInfo) throws SecurityPackageCreationException{
		this.serverManager.sendMessage(clientID, SecurityPackageManager.getMessageFromDeniedSecurityPackageType(securityPackageID, noPermissionExceptionInfo));
	}
}
