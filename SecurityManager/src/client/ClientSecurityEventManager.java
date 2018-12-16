package client;

import java.util.ArrayList;

import data.Queue;
import data.events.AccessDeniedEvent;
import data.events.AccessDeniedEventListner;
import data.events.Event;

public class ClientSecurityEventManager {
	
	@SuppressWarnings("unchecked")
	private ArrayList<AccessDeniedEventListner>[] accessDeniedEventListenerdb = new ArrayList[10];
	
	private Queue<Event> publishedEvents = new Queue<>();
	
	public void publishEvent(Event event){
		this.publishedEvents.add(event);
	}

	public void registerAccessDeniedEventListener(AccessDeniedEventListner listener, int priority) {
		if(priority>accessDeniedEventListenerdb.length-1)priority = accessDeniedEventListenerdb.length-1;
		if(this.accessDeniedEventListenerdb[priority]==null)this.accessDeniedEventListenerdb[priority] = new ArrayList<>();
		this.accessDeniedEventListenerdb[priority].add(listener);
	}
	
	public void tick(){
		while(!publishedEvents.isEmpty()){
			Event  event = publishedEvents.get();
			publishedEvents.remove();
			if(event instanceof AccessDeniedEvent){
				handleNewEvent(event, this.accessDeniedEventListenerdb);
			}
		}
	}

	private void handleNewEvent(Event event, ArrayList<?>[] eventListnerdb) {
		for(int prorityLevel = 0; prorityLevel<eventListnerdb.length && event.isActive(); prorityLevel++){
			ArrayList<?> eventListener = eventListnerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					Object listener = eventListener.get(i);
					if(event instanceof AccessDeniedEvent){
						AccessDeniedEventListner ad = (AccessDeniedEventListner)listener;
						ad.handleNoPermissionException((AccessDeniedEvent) event);
					}
				}
			}
		}
	}

}
