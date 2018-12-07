package client;

import java.util.ArrayList;

import data.Queue;
import data.events.client.ClientEvent;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;

public class ClientEventManager {

	@SuppressWarnings("unchecked")
	private ArrayList<ToClientMessageEventListener>[] clientMessageEventListenerdb = new ArrayList[10];

	@SuppressWarnings("unchecked")
	private ArrayList<ClientLostConnectionToServerEventListener>[] clientLostConnectionToServerEventListenerdb = new ArrayList[10];
	
	private Queue<ClientEvent> publishedEvents = new Queue<>();
	
	public void publishClientEvent(ClientEvent event){
		this.publishedEvents.add(event);
	}

	public void registerClientMessageEventListener(ToClientMessageEventListener listener, int priority) {
		if(priority>clientMessageEventListenerdb.length-1)priority = clientMessageEventListenerdb.length-1;
		if(this.clientMessageEventListenerdb[priority]==null)this.clientMessageEventListenerdb[priority] = new ArrayList<>();
		this.clientMessageEventListenerdb[priority].add(listener);
	}

	public void registerClientLostConnectionToServerEventListener(ClientLostConnectionToServerEventListener listener, int priority) {
		if(priority>clientLostConnectionToServerEventListenerdb.length-1)priority = clientLostConnectionToServerEventListenerdb.length-1;
		if(this.clientLostConnectionToServerEventListenerdb[priority]==null)this.clientLostConnectionToServerEventListenerdb[priority] = new ArrayList<>();
		this.clientLostConnectionToServerEventListenerdb[priority].add(listener);
	}
	
	public void tick(){
		while(!publishedEvents.isEmpty()){
			ClientEvent  event = publishedEvents.get();
			publishedEvents.remove();
			if(event instanceof ToClientMessageEvent){
				handleNewEvent(event, this.clientMessageEventListenerdb);
			}else if(event instanceof ClientLostConnectionToServerEvent){
				handleNewEvent(event, this.clientLostConnectionToServerEventListenerdb);
			}
		}
	}

	private void handleNewEvent(ClientEvent event, ArrayList<?>[] eventListnerdb) {
		for(int prorityLevel = 0; prorityLevel<eventListnerdb.length && event.isActive(); prorityLevel++){
			ArrayList<?> eventListener = eventListnerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					Object listener = eventListener.get(i);
					if(event instanceof ToClientMessageEvent){
						ToClientMessageEventListener tcm = (ToClientMessageEventListener)listener;
						tcm.messageFromServer((ToClientMessageEvent) event);
					}else if(event instanceof ClientLostConnectionToServerEvent){
						ClientLostConnectionToServerEventListener clcts= (ClientLostConnectionToServerEventListener)listener;
						clcts.connectionLost((ClientLostConnectionToServerEvent) event);
					}
				}
			}
		}
	}
}
