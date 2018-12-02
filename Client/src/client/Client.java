package client;

import data.DataPackage;
import data.PackageType;
import data.Queue;

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

	public Client(ClientManager clientManager, String ip, int port) {
		this.clientManager = clientManager;
		cch = new ClientConnectionHandler(this, ip, port);
	}
	
	public void connect(){
		this.cch.connect();
	}

	public void endClient() {
		this.run = false;
	}

//	protected void fromServer(PackageType type) {
//		System.out.print(type.getId()+": "+type.getName()+"->");
//		for(ReadableData<?> data: type.getDataStructures())System.out.print(data.getData().toString());
//		System.out.println();
//	}
	
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

}
