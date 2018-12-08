package server.test;

import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.exceptions.server.ServerPortException;
import data.user.User;
import server.ServerManager;
import server.ServerUserManager;

public class TestServer implements ClientConnectionValidationEventListener, ClientLogoutEventListener{
	
	public static void main(String args[]){
		new TestServer();
	}
	
	public TestServer(){
		ServerManager serverManager = new ServerManager();
		ServerUserManager serverUserManager = new ServerUserManager(serverManager, "fdsa4321");
		
		serverUserManager.getUserEventManager().registerClientConnectionValidationEventListener(this, 5);
		serverUserManager.getUserEventManager().registerClientLogoutEventListener(this, 5);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					serverManager.tick();
					serverUserManager.tick();
				}
			}
		}).start();
		
		if(serverUserManager.getAllRegisteredUsers().size()==0){
			serverUserManager.registerNewUser(new User("", "Test1", "1234"));
			serverUserManager.registerNewUser(new User("", "Test2", "2341"));
			serverUserManager.registerNewUser(new User("", "Test3", "3421"));
			serverUserManager.registerNewUser(new User("", "Test4", "4321"));
		}
		
		try {
			serverManager.openConnection();
		} catch (ServerPortException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	@Override
	public void clientLogout(ClientLogoutEvent event) {
		System.out.println("User: "+event.getUser().getUsername()+" has logged out");
	}

	@Override
	public void handleClientLogginIn(ClientConnectionValidationEvent event) {
		if(event.isLoggedIn())System.out.println("Client: "+event.getClientID()+" has registered as User: "+event.getUser().toString());
		else System.out.println("Client:"+event.getClientID()+" has failed to Login");
		
	}

}
