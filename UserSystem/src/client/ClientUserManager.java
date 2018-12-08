package client;

import data.DataPackage;
import data.PackageType;
import data.UserEventManager;
import data.UserPackageManager;
import data.events.ClientLoginEvent;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.user.User;

public class ClientUserManager implements ToClientMessageEventListener{
	
	private ClientManager clientManager;
	private ClientUserService userService;
	private UserEventManager userEventManager;
	
	private User user;
	private int logginState = 0;
	
	public ClientUserManager(ClientManager clientManager){
		this.clientManager = clientManager;
		this.userService = new ClientUserService();
		this.userEventManager = new UserEventManager();
		
		UserPackageManager.loadPackageTypes();
		this.clientManager.getEventManager().registerClientMessageEventListener(this, 1);
	}

	public void login(String username, String password) {
		this.user = new User("", username, password);
		
		String encodedLoginInfo = this.userService.getEncodedLoginInfo(user);
		try {
			this.clientManager.sendToServer(DataPackage.getPackage(PackageType.readPackageData(UserPackageManager.DataPackage_UserLogin, encodedLoginInfo)));
		} catch (Exception e) {
			System.out.println("An excption occured, while packaging LoginInfo data: "+encodedLoginInfo);
		}
		
		this.logginState = 1;
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if(event.getMessage().getId() == UserPackageManager.DataPackage_UserLoginConfirm){
			event.setActive(false);
			ClientLoginEvent loginEvent = UserPackageManager.getLoginEventFromData(event.getMessage(), user.getUsername());
			this.user = loginEvent.getUser();
			this.logginState = loginEvent.isLoggedIn() ? 2 : 3;
			this.userEventManager.publishEvent(loginEvent);
		}
	}
	
	public User getUser(){
		return this.user;
	}
	
	public int getLogginState(){
		return this.logginState;
	}
	
	public UserEventManager getUserEventManager(){
		return this.userEventManager;
	}

}
