package client;

import java.util.ArrayList;

import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;

public class ClientEventManager {

	@SuppressWarnings("unchecked")
	private ArrayList<ToClientMessageEventListener>[] clientMessageEventListenerdb = new ArrayList[10];
	
	public void publishClientMessageEvent(ToClientMessageEvent event){
		for(int prorityLevel = 0; prorityLevel<this.clientMessageEventListenerdb.length && event.isActive(); prorityLevel++){
			ArrayList<ToClientMessageEventListener> eventListener = this.clientMessageEventListenerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					ToClientMessageEventListener listener = eventListener.get(i);
					listener.messageFromServer(event);
				}
			}
		}
	}

	public void registerClientMessageEventListener(ToClientMessageEventListener listener, int priority) {
		if(priority>clientMessageEventListenerdb.length-1)priority = clientMessageEventListenerdb.length-1;
		if(this.clientMessageEventListenerdb[priority]==null)this.clientMessageEventListenerdb[priority] = new ArrayList<>();
		this.clientMessageEventListenerdb[priority].add(listener);
	}
}
