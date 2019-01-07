package client;

import java.util.ArrayList;

import data.Queue;
import data.events.Event;
import data.events.client.UserValidationEvent;
import data.events.client.UserValidationEventListener;

public class USCEventManager {
	
	@SuppressWarnings("unchecked")
	private ArrayList<UserValidationEventListener>[] userValidationEventListenerdb = new ArrayList[10];
	
	private Queue<Event> publishedEvents = new Queue<>();
	
	public void publishEvent(Event event){
		this.publishedEvents.add(event);
	}

	public void registerUserValidationEventListener(UserValidationEventListener listener, int priority) {
		if(priority>userValidationEventListenerdb.length-1)priority = userValidationEventListenerdb.length-1;
		if(this.userValidationEventListenerdb[priority]==null)this.userValidationEventListenerdb[priority] = new ArrayList<>();
		this.userValidationEventListenerdb[priority].add(listener);
	}
	
	public void tick(){
		while(!publishedEvents.isEmpty()){
			Event  event = publishedEvents.get();
			publishedEvents.remove();
			if(event instanceof UserValidationEvent){
				handleNewEvent(event, this.userValidationEventListenerdb);
			}
		}
	}

	private void handleNewEvent(Event event, ArrayList<?>[] eventListnerdb) {
		for(int prorityLevel = 0; prorityLevel<eventListnerdb.length && event.isActive(); prorityLevel++){
			ArrayList<?> eventListener = eventListnerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					Object listener = eventListener.get(i);
					if(event instanceof UserValidationEvent){
						UserValidationEventListener cl = (UserValidationEventListener)listener;
						cl.validateUser((UserValidationEvent) event);
					}
				}
			}
		}
	}


}
