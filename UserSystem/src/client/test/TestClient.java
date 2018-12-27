package client.test;

import client.ClientManager;
import client.ClientUserManager;
import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.ClientLoginEvent;
import data.events.ClientLoginEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.exceptions.LoginInformationCreationException;
import data.exceptions.UnsupportedPackageCreationException;
import data.exceptions.client.ServerNotFoundException;
import data.readableData.ReadableData;
import client.test.main.ClientLoginMain;

public class TestClient implements ToClientMessageEventListener, ClientLoginEventListener{

	private ClientLoginMain main;
	private ClientManager clientManager;
	
	private ClientUserManager clientUserManager;
	
	public TestClient(ClientLoginMain clientLoginMain, String ip, int port) {
		this.main = clientLoginMain;
		
		this.clientManager = new ClientManager(ip, port);
		this.clientManager.getEventManager().registerClientMessageEventListener(this, 5);
		
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
		this.main.println(text);
	}
	
	public void endClient() {
		this.clientManager.endClient();
		System.exit(0);
	}

	public void sendToServer(Queue<DataPackage> packages) {
		this.clientManager.sendToServer(packages);
	}

	public void tick() {
		if(clientManager!=null)this.clientManager.getEventManager().tick();
		if(clientUserManager!=null)this.clientUserManager.getUserEventManager().tick();
	}

	public void login(String username, String password) {
		try {
			this.clientUserManager.login(username, password);
		} catch (UnsupportedPackageCreationException | LoginInformationCreationException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	public void logout() {
		try {
			this.clientUserManager.logout();
		} catch (UnsupportedPackageCreationException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	@Override
	public void ClientLogin(ClientLoginEvent event) {
		if(event.isLoggedIn())main.loginSuccesfull();
		else main.loginFailed(event);
	}

	public ClientManager getClientManager() {
		return this.clientManager;
	}

}
