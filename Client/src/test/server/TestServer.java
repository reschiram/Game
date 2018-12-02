package test.server;

import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.readableData.ReadableData;
import server.ServerManager;

public class TestServer implements NewClientConnectionEventListener, ToServerMessageEventListener{
	
	private ServerManager serverManager;
	
	public TestServer(){		
		this.serverManager = new ServerManager();
		this.serverManager.getEventManager().registerNewClientConnectionEventListener(this, 5);
		this.serverManager.getEventManager().registerServerMessageEventListener(this, 5);
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		System.out.println("=====New Message From CLient:"+event.getMessage().getId()+"|"+event.getMessage().getName()+"=====");
		for(ReadableData<?> data: event.getMessage().getDataStructures()){
			System.out.print("["+data.getName()+"|"+data.toString()+"] ");
		}
		System.out.println();
		System.out.println("-------------------");
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		System.out.println("New Client: "+event.getServerClient().getId()+"->"+event.getServerClient().getConnectionAdress());
	}

}
