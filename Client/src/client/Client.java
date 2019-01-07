package client;

import data.DataPackage;
import data.Funktions;
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
	
	private Queue<Queue<DataPackage>> toServer = new Queue<>();
	
	Client(ClientManager clientManager, String ip){
		this.clientManager = clientManager;
		this.cch = new ClientConnectionHandler(this, ip, Port);
	}

	private void startInternalThread() {
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
					long currentTime = System.currentTimeMillis();
					while(System.currentTimeMillis()-currentTime < 1000){
						while(!toServer.isEmpty()){
							cch.send(toServer.get());
							toServer.remove();
						}
						Funktions.wait(1);
					}
				}
				
				if(!cch.isEnded()){
					endClient();
					clientManager.getEventManager().publishClientEvent(new ClientLostConnectionToServerEvent(cch.getConnection()));
				}
			}
		}).start();
	}

	Client(ClientManager clientManager, String ip, int port) {
		this.clientManager = clientManager;
		cch = new ClientConnectionHandler(this, ip, port);
	}
	
	void connect(boolean ssl) throws ServerNotFoundException{
		this.cch.connect(ssl);
		
		startInternalThread();
	}

	void endClient() {
		this.cch.endConnection();
		this.run = false;
	}
	
	void sendToServer(Queue<DataPackage> packages){
		if(this.cch.isConnected()){
			this.toServer.add(packages);
		}
	}

	public boolean run() {
		return run;
	}
	
	boolean isConnected(){
		return cch.isConnected();
	}

	void fromServer(PackageType message) {
		this.clientManager.publishNewMessage(message);
	}

	void setId(long id) {
		this.id = id;
	}	

	public long getId() {
		return this.id;
	}

	void connectionLost(ClientLostConnectionToServerEvent event) {
		this.clientManager.getEventManager().publishClientEvent(event);
	}	

}
