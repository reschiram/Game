package server;

import data.Queue;

public class ServerMessageManager {
	
	private Server server;
	
	private Queue<ServerMessage> serverMessages = new Queue<>();
	private boolean inUse;
	
	public ServerMessageManager(Server server) {
		this.server = server;
	}

	public void tick() {
		waitForInUse();
		
		while(!serverMessages.isEmpty()){
			ServerMessage message = serverMessages.get();
			serverMessages.remove();
			ServerClient sc = this.server.getConnectionHandler().getServerClient(message.getId());
			sc.sendToClient(message.getMessage());
//			sc.flush();
		}
		
		endInUse();
	}

	public void addMessage(ServerMessage serverMessage) {
		waitForInUse();
		
		this.serverMessages.add(serverMessage);
		
		endInUse();
	}

	private void waitForInUse() {
		if(inUse){
			synchronized (server) {
				try {
					server.wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}		
		
		inUse = true;
	}
	
	private void endInUse(){
		inUse = false;		
		
		synchronized (server) {
			server.notify();
		}		
	}
}
