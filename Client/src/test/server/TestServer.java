package test.server;

import java.util.ArrayList;

import data.DataPackage;
import data.Queue;
import data.events.server.NewClientConnectionEvent;
import data.events.server.NewClientConnectionEventListener;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ServerLostConnectionToClientEventListener;
import data.events.server.ToServerMessageEvent;
import data.events.server.ToServerMessageEventListener;
import data.exceptions.handler.DefaultExceptionHandler;
import data.exceptions.server.InvalidServerClientIDException;
import data.exceptions.server.ServerPortException;
import data.readableData.ReadableData;
import server.KickTask;
import server.ServerManager;
import test.server.main.ServerTestMain;

public class TestServer implements NewClientConnectionEventListener, ToServerMessageEventListener, ServerLostConnectionToClientEventListener{
	
	private ServerManager serverManager;
	private ServerTestMain main;
	
	public TestServer(ServerTestMain main){
		this.main = main;		
		this.serverManager = new ServerManager();
		
		try {
			this.serverManager.openConnection(false);
		} catch (ServerPortException e) {
			System.out.println(e.getErrorMessage());
		
		}
		this.serverManager.getEventManager().registerNewClientConnectionEventListener(this, 5);
		this.serverManager.getEventManager().registerServerMessageEventListener(this, 5);
		this.serverManager.getEventManager().registerServerLostConnectionToClientEventListener(this, 5);
	}

	@Override
	public void messageFromClient(ToServerMessageEvent event) {
		String msg = "";
		msg += "===== New Message From CLient("+event.getClientID()+"): "+event.getMessage().getId()+" | "+event.getMessage().getName()+" =====" +"\n";
		for(ReadableData<?> data: event.getMessage().getDataStructures()){
			msg += "["+data.getName()+"|"+data.toString()+"] ";
		}
		msg += "\n";
		msg += "-------------------";
		try {
			serverManager.sendMessage(event.getClientID(), DataPackage.getPackage(event.getMessage()));
		} catch (InvalidServerClientIDException e) {
			DefaultExceptionHandler.getDefaultExceptionHandler().getDefaultHandler_InvalidServerClientIDException().handleError(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		main.getGUI().println(msg);
	}

	@Override
	public void newServerClient(NewClientConnectionEvent event) {
		main.getGUI().println("New Client: "+event.getClientID());
	}

	@Override
	public void connectionLost(ServerLostConnectionToClientEvent event) {
		main.getGUI().println("Lost Connection to Client: "+event.getClientID()+". Status: (active = "+event.isActive()+") , (closed = "+event.isClosed()+"), (ended = "+event.isEnded()+")");		
	}

	public void tick() {
		if(serverManager!=null)this.serverManager.tick();
	}

	public boolean isConnected() {
		return serverManager.isConnected();
	}

	public void sendPackage(long clientID, Queue<DataPackage> message) throws InvalidServerClientIDException {
		this.serverManager.sendMessage(clientID, message);
	}

	public void sendPackageToAllClients(Queue<DataPackage> message) {
		for(long clientID: this.serverManager.getConnectedClients()){
			try {
				this.serverManager.sendMessage(clientID, message.clone());
			} catch (InvalidServerClientIDException e) {
				System.out.println(e.getErrorMessage());
			}			
		}
	}

	public ArrayList<Long> getAllConnectedClients() {
		return this.serverManager.getConnectedClients();
	}

	public void kickClient(long clientID, String reason) throws Exception, InvalidServerClientIDException {
		this.serverManager.addServerTask(new KickTask(clientID, reason));
	}

	public ServerManager getServerManager() {
		return this.serverManager;
	}

}
