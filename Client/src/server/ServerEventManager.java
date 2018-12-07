package server;

import java.util.ArrayList;

import data.Queue;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerEvent;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;

public class ServerEventManager {

	@SuppressWarnings("unchecked")
	private ArrayList<ToServerMessageEventListener>[] serverMessageEventListenerdb = new ArrayList[10];
	
	@SuppressWarnings("unchecked")
	private ArrayList<NewClientConnectionEventListener>[] newClientConnectionEventListenerdb = new ArrayList[10];
	
	@SuppressWarnings("unchecked")
	private ArrayList<ServerLostConnectionToClientEventListener>[] serverLostConnectionToClientEventListenerdb = new ArrayList[10];
	
	private Queue<ServerEvent> publishedEvents = new Queue<>();
	
	public void publishServerEvent(ServerEvent event){
		this.publishedEvents.add(event);
	}

	public void registerServerMessageEventListener(ToServerMessageEventListener listener, int priority) {
		if(priority>serverMessageEventListenerdb.length-1)priority = serverMessageEventListenerdb.length-1;
		if(this.serverMessageEventListenerdb[priority]==null)this.serverMessageEventListenerdb[priority] = new ArrayList<>();
		this.serverMessageEventListenerdb[priority].add(listener);
	}

	public void registerNewClientConnectionEventListener(NewClientConnectionEventListener listener, int priority) {
		if(priority>newClientConnectionEventListenerdb.length-1)priority = newClientConnectionEventListenerdb.length-1;
		if(this.newClientConnectionEventListenerdb[priority]==null)this.newClientConnectionEventListenerdb[priority] = new ArrayList<>();
		this.newClientConnectionEventListenerdb[priority].add(listener);
	}

	public void registerServerLostConnectionToClientEventListener(ServerLostConnectionToClientEventListener listener, int priority) {
		if(priority>serverLostConnectionToClientEventListenerdb.length-1)priority = serverLostConnectionToClientEventListenerdb.length-1;
		if(this.serverLostConnectionToClientEventListenerdb[priority]==null)this.serverLostConnectionToClientEventListenerdb[priority] = new ArrayList<>();
		this.serverLostConnectionToClientEventListenerdb[priority].add(listener);
	}
	
	void tick(){
		while(!publishedEvents.isEmpty()){
			ServerEvent  event = publishedEvents.get();
			publishedEvents.remove();
			if(event instanceof ToServerMessageEvent){
				handleNewEvent(event, this.serverMessageEventListenerdb);
			}else if(event instanceof NewClientConnectionEvent){
				handleNewEvent(event, this.newClientConnectionEventListenerdb);
			}else if(event instanceof ServerLostConnectionToClientEvent){
				handleNewEvent(event, this.serverLostConnectionToClientEventListenerdb);
			}
		}
	}

	private void handleNewEvent(ServerEvent event, ArrayList<?>[] eventListnerdb) {
		for(int prorityLevel = 0; prorityLevel<eventListnerdb.length && event.isActive(); prorityLevel++){
			ArrayList<?> eventListener = eventListnerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					Object listener = eventListener.get(i);
					if(event instanceof ToServerMessageEvent){
						ToServerMessageEventListener tsm = (ToServerMessageEventListener)listener;
						tsm.messageFromClient((ToServerMessageEvent) event);
					}else if(event instanceof NewClientConnectionEvent){
						NewClientConnectionEventListener ncc= (NewClientConnectionEventListener)listener;
						ncc.newServerClient((NewClientConnectionEvent) event);
					}else if(event instanceof ServerLostConnectionToClientEvent){
						ServerLostConnectionToClientEventListener slctc= (ServerLostConnectionToClientEventListener)listener;
						slctc.connectionLost((ServerLostConnectionToClientEvent) event);
					}
				}
			}
		}
	}

}
