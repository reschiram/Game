package client;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.client.ClientLostConnectionToServerEvent;
import data.exceptions.client.ServerNotFoundException;

public class Client {

	private static final int Port = 12345;
	
	private ClientConnectionHandler cch;
	private ClientManager clientManager;
	private long id;
	private boolean run = true;
	
	public Client(ClientManager clientManager, String ip){
		this.clientManager = clientManager;
		this.cch = new ClientConnectionHandler(this, ip, Port);
	}

	private void startHeartbeatSender() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Queue<DataPackage> heartbeat = null;
				try {
					heartbeat = DataPackage.getPackage(PackageType.readPackageData(DataPackage.PackageType_Heartbeat, new byte[1]));
				} catch (Exception e1) {
					e1.printStackTrace();
					endClient();
				}
				
				while(cch.isConnected() && !cch.isEnded()){
					cch.send(heartbeat.clone());
					try {
						synchronized (Thread.currentThread()) {
							Thread.currentThread().wait(1000);							
						}
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				if(!cch.isEnded()){
					endClient();
					clientManager.getEventManager().publishClientLostConnectionToServerEvent(new ClientLostConnectionToServerEvent(cch.getConnection()));
				}
			}
		}).start();
	}

	public Client(ClientManager clientManager, String ip, int port) {
		this.clientManager = clientManager;
		cch = new ClientConnectionHandler(this, ip, port);
	}
	
	public void connect() throws ServerNotFoundException{
		this.cch.connect();
		
		startHeartbeatSender();
	}

	public void endClient() {
		this.cch.endConnection();
		this.run = false;
	}
	
	public void sendToServer(Queue<DataPackage> packages){
		if(this.cch.isConnected()){
			this.cch.send(packages);
		}
	}

	public boolean run() {
		return run;
	}

	public void fromServer(PackageType message) {
		this.clientManager.publishNewMessage(message);
	}

	public void setId(long id) {
		this.id = id;
	}	

	public long getId() {
		return this.id;
	}

	public void connectionLost(ClientLostConnectionToServerEvent event) {
		this.clientManager.getEventManager().publishClientLostConnectionToServerEvent(event);
	}	

}
