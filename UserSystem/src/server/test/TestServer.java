package server.test;

import data.DataPackage;
import data.Funktions;
import data.PackageType;
import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.UserDatabaseReadingException;
import data.exceptions.server.ServerPortException;
import data.readableData.ReadableData;
import data.readableData.StringData;
import data.user.User;
import server.ServerManager;
import server.ServerUserManager;

public class TestServer implements ClientConnectionValidationEventListener, ClientLogoutEventListener, ToServerMessageEventListener, NewClientConnectionEventListener, ServerLostConnectionToClientEventListener{
	
	public static void main(String args[]){
		new TestServer();
	}
	
	private ServerManager serverManager;
	
	public TestServer(){
		serverManager = new ServerManager();
		ServerUserManager serverUserManager = new ServerUserManager(serverManager, "fdsa4321");
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType(30, "test2", new StringData("test30", 30), new StringData("test50", 50), new StringData("test20", 20)));
		
		serverUserManager.getUserEventManager().registerClientConnectionValidationEventListener(this, 5);
		serverUserManager.getUserEventManager().registerClientLogoutEventListener(this, 5);
		
		this.serverManager.getEventManager().registerNewClientConnectionEventListener(this, 5);
		this.serverManager.getEventManager().registerServerLostConnectionToClientEventListener(this, 5);
		this.serverManager.getEventManager().registerServerMessageEventListener(this, 5);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					serverManager.tick();
					serverUserManager.tick();
					Funktions.wait(1);
				}
			}
		}).start();
		
		if(serverUserManager.getAllRegisteredUsers().size()==1){
			try {
				serverUserManager.registerNewUser(new User("", "Test1", "1234"));
				serverUserManager.registerNewUser(new User("", "Test2", "2341"));
				serverUserManager.registerNewUser(new User("", "Test3", "3421"));
				serverUserManager.registerNewUser(new User("", "Test4", "4321"));
			} catch (UserDatabaseReadingException e) {
				System.out.println(e.getErrorMessage());
			}
		}
		
		try {
			serverManager.openConnection();
		} catch (ServerPortException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	@Override
	public void clientLogout(ClientLogoutEvent event) {
		System.out.println("Client: "+event.getClientID()+" has logged out as user: "+event.getValidatedUser().getUsername()+" has logged out");
	}

	@Override
	public void handleClientLogginIn(ClientConnectionValidationEvent event) {
		if(event.isLoggedIn())System.out.println("Client: "+event.getClientID()+" has registered as User: "+event.getUser().toString());
		else System.out.println("Client: "+event.getClientID()+" has failed to Login");
		
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		System.out.println("=====New Message From CLient:"+event.getMessage().getId()+"|"+event.getMessage().getName()+"=====");
		for(ReadableData<?> data: event.getMessage().getDataStructures()){
			System.out.print("["+data.getName()+"|"+data.toString()+"] ");
		}
		System.out.println();
		System.out.println("-------------------");
		serverManager.sendMessage(event.getClientID(), DataPackage.getPackage(event.getMessage()));
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		System.out.println("New Client: "+event.getClientID());
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		System.out.println("Lost Connection to Client:"+event.getClientID()+". Connection is active:"+event.isActive()+", is closed:"+event.isClosed()+" has been ended:"+event.isEnded());		
	}

}
