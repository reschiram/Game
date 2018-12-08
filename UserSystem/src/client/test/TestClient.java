package client.test;

import client.ClientManager;
import client.ClientUserManager;
import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.ClientLoginEvent;
import data.events.ClientLoginEventListener;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.exceptions.client.ServerNotFoundException;
import data.readableData.ReadableData;
import client.test.main.ClientTestMain;

public class TestClient implements ToClientMessageEventListener, ClientLostConnectionToServerEventListener, ClientLoginEventListener{

	private ClientTestMain main;
	private ClientManager clientManager;
	
	private ClientUserManager clientUserManager;
	
	public TestClient(ClientTestMain main, String ip, int port) {
		this.main = main;
		
		this.clientManager = new ClientManager(ip, port);
		this.clientManager.getEventManager().registerClientMessageEventListener(this, 5);
		this.clientManager.getEventManager().registerClientLostConnectionToServerEventListener(this, 5);
		
		this.clientUserManager = new ClientUserManager(clientManager);
		this.clientUserManager.getUserEventManager().registerClientLoginEventListener(this, 5);
		
		try {
			this.clientManager.connectToServer();
		} catch (ServerNotFoundException e) {
			System.out.println(e.getErrorMessage());
		}
	}
	
	public void messageFromServer(ToClientMessageEvent event){
		PackageType message = event.getMessage();	
		
		String text = message.getId()+": "+message.getName()+"-> ";
		for(ReadableData<?> data: message.getDataStructures()){
			text+=data.getData().toString()+ "|";
		}
		if(message.getDataStructures().length>0)text = text.substring(0, text.length()-1);
		this.main.getGUI().println(text);
	}
	
	public void endClient() {
		this.clientManager.endClient();
		System.exit(0);
	}

	public void sendToServer(Queue<DataPackage> packages) {
		this.clientManager.sendToServer(packages);
	}

	@Override
	public void connectionLost(ClientLostConnectionToServerEvent event) {
		System.out.println("Lost Connection to Server. Connection is active:"+event.isActive()+", is closed:"+event.isClosed()+" has been ended:"+event.isEnded());
	}

	public void tick() {
		if(clientManager!=null)this.clientManager.getEventManager().tick();
		if(clientUserManager!=null)this.clientUserManager.getUserEventManager().tick();
	}

	public void login(String username, String password) {
		this.clientUserManager.login(username, password);
	}

	@Override
	public void ClientLogin(ClientLoginEvent event) {
		if(event.isLoggedIn())System.out.println("Login request answered. Login was successful. UserID: "+event.getUser().getID());
		else System.out.println("Login request answered. Login has failed.");
	}

}
