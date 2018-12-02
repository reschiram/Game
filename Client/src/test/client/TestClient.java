package test.client;

import client.ClientManager;
import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.readableData.ReadableData;
import test.client.main.ClientTestMain;

public class TestClient implements ToClientMessageEventListener{

	private ClientTestMain main;
	private ClientManager clientManager;
	
	public TestClient(ClientTestMain main, String ip, int port) {
		this.main = main;
		
		this.clientManager = new ClientManager(ip, port);
		this.clientManager.getEventManager().registerClientMessageEventListener(this, 5);
		this.clientManager.connectToServer();
	}
	
	public void messageFromServer(ToClientMessageEvent event){
		PackageType message = event.getMessage();	
		
		String text = message.getId()+": "+message.getName()+"-> ";
		System.out.println(message.getDataStructures().length);
		for(ReadableData<?> data: message.getDataStructures()){
			System.out.println(data.getData()+"|"+data.getData().toString());
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

}
