package test.server;

import data.DataPackage;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.server.ServerPortException;
import data.readableData.ReadableData;
import server.ServerManager;

public class TestServer implements NewClientConnectionEventListener, ToServerMessageEventListener, ServerLostConnectionToClientEventListener{
	
	private ServerManager serverManager;
	
	public TestServer(){		
		this.serverManager = new ServerManager();
		try {
			this.serverManager.openConnection();
		} catch (ServerPortException e) {
			System.out.println(e.getErrorMessage());
		}
		this.serverManager.getEventManager().registerNewClientConnectionEventListener(this, 5);
		this.serverManager.getEventManager().registerServerMessageEventListener(this, 5);
		this.serverManager.getEventManager().registerServerLostConnectionToClientEventListener(this, 5);
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		System.out.println("=====New Message From CLient:"+event.getMessage().getId()+"|"+event.getMessage().getName()+"=====");
		for(ReadableData<?> data: event.getMessage().getDataStructures()){
			System.out.print("["+data.getName()+"|"+data.toString()+"] ");
		}
		System.out.println();
		System.out.println("-------------------");
		serverManager.sendMessage(event.getClientID(), DataPackage.getPackage(event.getMessage()));
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		System.out.println("New Client: "+event.getClientID());
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		System.out.println("Lost Connection to Client:"+event.getClientID()+". Connection is active:"+event.isActive()+", is closed:"+event.isClosed()+" has been ended:"+event.isEnded());		
	}

	public void tick() {
		if(serverManager!=null)this.serverManager.tick();
	}

	public boolean isConnected() {
		return serverManager.isConnected();
	}

}
