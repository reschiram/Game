package client;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.client.ToClientMessageEvent;
import data.exceptions.client.ServerNotFoundException;
import data.readableData.LongData;

public class ClientManager {
	
	private Client client;
	private ClientEventManager eventManager;
	
	public ClientManager(String ip, int port){
		DataPackage.loadInternalPackageIds();
		
		this.client = new Client(this, ip, port);
		this.eventManager = new ClientEventManager();
	}

	public ClientEventManager getEventManager(){
		return this.eventManager;
	}
	
	public void connectToServer() throws ServerNotFoundException{
		this.client.connect();
	}

	public void publishNewMessage(PackageType message) {
		ToClientMessageEvent event = new ToClientMessageEvent(message);
		handleInternalMessages(event);
		if(event.isActive()){
			this.eventManager.publishClientMessageEvent(event);
		}
	}

	private void handleInternalMessages(ToClientMessageEvent event) {
		if(event.getMessage().getId() == DataPackage.PackageType_InitConnection){
			this.client.setId(((LongData)event.getMessage().getDataStructures()[0]).getData().longValue());
			event.setActive(false);
		}
	}

	public void endClient() {
		this.client.endClient();
	}

	public void sendToServer(Queue<DataPackage> packages) {
		this.client.sendToServer(packages);
	}

}
