package server;

import java.util.ArrayList;

import data.DataPackage;
import data.PackageType;
import data.UserEventManager;
import data.UserPackageManager;
import data.events.ClientConnectionValidationEvent;
import data.exceptions.ClientValidationException;
import data.user.User;

public class ServerUserManager{
	
	private ConnectionManager connectionManager;
	private ServerUserService serverUserService;
	private ServerManager serverManager;
	private UserEventManager userEventManager;
	
	public ServerUserManager(ServerManager serverManager, String generalPassword){
		UserPackageManager.loadPackageTypes();
		
		this.userEventManager = new UserEventManager();	
		this.serverManager = serverManager;
		this.serverUserService = new ServerUserService(generalPassword);
		this.connectionManager = new ConnectionManager();
		new ServerListener(this, serverManager);
	}

	void login(long clientID, String loginInfo) {
		User user = null;
		try {
			user = this.serverUserService.getUser(loginInfo);
		} catch (ClientValidationException e) {}
		if(user == null){
			try {
				this.serverManager.sendMessage(clientID, DataPackage.getPackage(PackageType.readPackageData(UserPackageManager.DataPackage_UserLoginConfirm, 1, "")));
				this.userEventManager.publishEvent(new ClientConnectionValidationEvent(clientID, false, user));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}else{
			boolean valdiation = this.connectionManager.validate(clientID, user);
			try {
				if(!valdiation)this.serverManager.sendMessage(clientID, DataPackage.getPackage(PackageType.readPackageData(UserPackageManager.DataPackage_UserLoginConfirm, "1", "")));
				else this.serverManager.sendMessage(clientID, DataPackage.getPackage(PackageType.readPackageData(UserPackageManager.DataPackage_UserLoginConfirm, "0", user.getID())));
				this.userEventManager.publishEvent(new ClientConnectionValidationEvent(clientID, valdiation, user));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public UserEventManager getUserEventManager(){
		return userEventManager;
	}
	
	public void tick(){
		this.userEventManager.tick();
	}
	
	public ValidatedUser getValidatedUser(long clientID){
		return this.connectionManager.getValidatetUser(clientID);
	}

	public void registerNewUser(User user) {
		this.serverUserService.registerNewUser(user);
	}

	public ArrayList<User> getAllRegisteredUsers() {
		return this.serverUserService.getAllRegisteredUsers();
	}
}
