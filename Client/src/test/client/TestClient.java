package test.client;

import client.ClientManager;
import data.DataPackage;
import data.PackageManager;
import data.PackageType;
import data.Queue;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.exceptions.client.ServerNotFoundException;
import data.readableData.ReadableData;
import test.client.main.ClientTestMain;

public class TestClient implements ToClientMessageEventListener, ClientLostConnectionToServerEventListener{

	private ClientTestMain main;
	private ClientManager clientManager;
	
	public TestClient(ClientTestMain main, String ip, int port) {
		this.main = main;
		
		this.clientManager = new ClientManager(ip, port);
		this.clientManager.getEventManager().registerClientMessageEventListener(this, 5);
		this.clientManager.getEventManager().registerClientLostConnectionToServerEventListener(this, 5);
		
		try {
			this.clientManager.connectToServer();
		} catch (ServerNotFoundException e) {
			System.out.println(e.getErrorMessage());
		}
	}
	
	public void messageFromServer(ToClientMessageEvent event){
		String msg = "";
		if(event.getMessage().getId() == DataPackage.PackageType_Kick) {
			msg += "Client has been kicked. Reason: " + PackageManager.readKickMessage(event);
		} else {
			PackageType message = event.getMessage();	
			
			msg += message.getId()+": "+message.getName()+"-> ";
			for(ReadableData<?> data: message.getDataStructures()){
				msg+=data.getData().toString()+ "|";
			}
			if(message.getDataStructures().length>0)msg = msg.substring(0, msg.length()-1);
		}
		this.main.getGUI().println(msg);
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
	}

}
