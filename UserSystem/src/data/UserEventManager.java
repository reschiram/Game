package data;

import java.util.ArrayList;

import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLoginEvent;
import data.events.ClientLoginEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.events.Event;


public class UserEventManager {
	
	@SuppressWarnings("unchecked")
	private ArrayList<ClientLoginEventListener>[] clientLoginEventListenerdb = new ArrayList[10];
	
	@SuppressWarnings("unchecked")
	private ArrayList<ClientConnectionValidationEventListener>[] clientConnectionValidationEventListenerdb = new ArrayList[10];
	
	@SuppressWarnings("unchecked")
	private ArrayList<ClientLogoutEventListener>[] clientLogoutEventListenerdb = new ArrayList[10];
	
	private Queue<Event> publishedEvents = new Queue<>();
	
	public void publishEvent(Event event){
		this.publishedEvents.add(event);
	}

	public void registerClientLoginEventListener(ClientLoginEventListener listener, int priority) {
		if(priority>clientLoginEventListenerdb.length-1)priority = clientLoginEventListenerdb.length-1;
		if(this.clientLoginEventListenerdb[priority]==null)this.clientLoginEventListenerdb[priority] = new ArrayList<>();
		this.clientLoginEventListenerdb[priority].add(listener);
	}

	public void registerClientConnectionValidationEventListener(ClientConnectionValidationEventListener listener, int priority) {
		if(priority>clientConnectionValidationEventListenerdb.length-1)priority = clientConnectionValidationEventListenerdb.length-1;
		if(this.clientConnectionValidationEventListenerdb[priority]==null)this.clientConnectionValidationEventListenerdb[priority] = new ArrayList<>();
		this.clientConnectionValidationEventListenerdb[priority].add(listener);
	}

	public void registerClientLogoutEventListener(ClientLogoutEventListener listener, int priority) {
		if(priority>clientLogoutEventListenerdb.length-1)priority = clientLogoutEventListenerdb.length-1;
		if(this.clientLogoutEventListenerdb[priority]==null)this.clientLogoutEventListenerdb[priority] = new ArrayList<>();
		this.clientLogoutEventListenerdb[priority].add(listener);
	}
	
	public void tick(){
		while(!publishedEvents.isEmpty()){
			Event  event = publishedEvents.get();
			publishedEvents.remove();
			if(event instanceof ClientLoginEvent){
				handleNewEvent(event, this.clientLoginEventListenerdb);
			}else if(event instanceof ClientConnectionValidationEvent){
				handleNewEvent(event, this.clientConnectionValidationEventListenerdb);
			}else if(event instanceof ClientLogoutEvent){
				handleNewEvent(event, this.clientLogoutEventListenerdb);
			}
		}
	}

	private void handleNewEvent(Event event, ArrayList<?>[] eventListnerdb) {
		for(int prorityLevel = 0; prorityLevel<eventListnerdb.length && event.isActive(); prorityLevel++){
			ArrayList<?> eventListener = eventListnerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					Object listener = eventListener.get(i);
					if(event instanceof ClientLoginEvent){
						ClientLoginEventListener cl = (ClientLoginEventListener)listener;
						cl.ClientLogin((ClientLoginEvent) event);
					}else if(event instanceof ClientConnectionValidationEvent){
						ClientConnectionValidationEventListener ccv = (ClientConnectionValidationEventListener)listener;
						ccv.handleClientLogginIn((ClientConnectionValidationEvent) event);
					}else if(event instanceof ClientLogoutEvent){
						ClientLogoutEventListener cl = (ClientLogoutEventListener)listener;
						cl.clientLogout((ClientLogoutEvent) event);
					}
				}
			}
		}
	}

}
