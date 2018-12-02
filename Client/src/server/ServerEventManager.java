package server;

import java.util.ArrayList;

import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;

public class ServerEventManager {

	@SuppressWarnings("unchecked")
	private ArrayList<ToServerMessageEventListener>[] serverMessageEventListenerdb = new ArrayList[10];
	
	@SuppressWarnings("unchecked")
	private ArrayList<NewClientConnectionEventListener>[] newClientConnectionEventListenerdb = new ArrayList[10];

	public void publishNewToServerMessageEvent(ToServerMessageEvent event) {
		for(int prorityLevel = 0; prorityLevel<this.serverMessageEventListenerdb.length && event.isActive(); prorityLevel++){
			ArrayList<ToServerMessageEventListener> eventListener = this.serverMessageEventListenerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					ToServerMessageEventListener listener = eventListener.get(i);
					listener.messageFromClient(event);
				}
			}
		}
	}

	public void publishNewClientConnectionEvent(NewClientConnectionEvent event) {
		for(int prorityLevel = 0; prorityLevel<this.newClientConnectionEventListenerdb.length && event.isActive(); prorityLevel++){
			ArrayList<NewClientConnectionEventListener> eventListener = this.newClientConnectionEventListenerdb[prorityLevel];
			if(eventListener!=null){
				for(int i = 0; i<eventListener.size() && event.isActive(); i++){
					NewClientConnectionEventListener listener = eventListener.get(i);
					listener.newServerClient(event);
				}
			}
		}
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

}
