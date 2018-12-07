package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.server.NewClientConnectionEvent;
import data.events.server.ServerLostConnectionToClientEvent;
import data.events.server.ToServerMessageEvent;
import data.exceptions.UnsupportedPackageException;

public class ServerClient {
	
	private final static int maxHeartbeatDuration = 10000;
	
	private Socket connection;
	private OutputStream out;
	private InputStream in;
	private Long id;
	
	private Server server;
	
	private long lastHeartbeat = -1;
	private boolean ended = false;
	
	ServerClient(Server server, Socket client, Long id) {
		this.connection = client;
		this.id = id;
		this.server = server;
		
		try {
			this.out = client.getOutputStream();
			this.in = client.getInputStream();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void tick(){
		checkIsAlive();
	}

	private void checkIsAlive(){
		if(!isConnected() || !(System.currentTimeMillis()-lastHeartbeat<=maxHeartbeatDuration || lastHeartbeat == -1)){
			connection = null;
			if(!ended){
				ended = true;
				server.getServerManager().getEventManager().publishServerEvent(new ServerLostConnectionToClientEvent(id, connection));
			}
		}			
	}

	void acceptConnection() {
		this.server.getServerManager().getEventManager().publishServerEvent(new NewClientConnectionEvent(this.getID()));
		
		try {
			Queue<DataPackage> dbs = DataPackage.getPackage(PackageType.readPackageData(DataPackage.PackageType_InitConnection, id));
			sendToClient(dbs);
		} catch (Exception e) {e.printStackTrace();}
	}

	void sendToClient(Queue<DataPackage> packages) {
		if(isConnected()){
			try {
				while(!packages.isEmpty()){
					this.out.write(packages.get().getByteData());
					packages.remove();
				}
			} catch (IOException e) {
				if(!isConnected()){
					if(!ended){
						ended = true;
						server.getServerManager().getEventManager().publishServerEvent(new ServerLostConnectionToClientEvent(id, connection));
					}
				}
			}
		}
	}

	boolean isConnected() {
		return connection!=null && connection.isConnected() && !connection.isClosed();
	}

	void scanForIncomingData() throws UnsupportedPackageException {
		Queue<DataPackage> dataStream = new Queue<>();
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(int length = this.in.read(income); length!=-1 && isConnected(); length = this.in.read(income)){
				income = Arrays.copyOf(income, DataPackage.MAXPACKAGELENGTH);				
								
				DataPackage dataPackage = null;
				try {
					dataPackage = new DataPackage(income, length);
				} catch (Exception ea) {ea.printStackTrace();}
				
				if(dataPackage!=null){
					if(dataPackage.getId() == DataPackage.PackageType_Heartbeat)this.lastHeartbeat = System.currentTimeMillis();
					else handNewDataPackage(dataPackage, dataStream);
				}
				
				income = new byte[DataPackage.PACKAGESIZE];
			}
		} catch (IOException e) {
			if(!isConnected()){
				if(!ended){
					ended = true;
					server.getServerManager().getEventManager().publishServerEvent(new ServerLostConnectionToClientEvent(id, connection));
				}
			}
		}
	}
	
	private void handNewDataPackage(DataPackage dataPackage, Queue<DataPackage> dataStream) throws UnsupportedEncodingException, UnsupportedPackageException {dataStream.add(dataPackage);
		if(dataPackage.isEnd()){
			ByteBuffer data = ByteBuffer.allocate(DataPackage.PACKAGESIZE);
			int actuallLength = 0;
			while(!dataStream.isEmpty()){
				actuallLength+=dataStream.get().getByteData().length;
				data.put(dataStream.get().getByteData(), DataPackage.ID_Length, dataStream.get().getByteData().length-DataPackage.ID_Length);
				dataStream.remove();
			}
			try {
				this.server.getServerManager().getEventManager().publishServerEvent(new ToServerMessageEvent(this.getID(), PackageType.readPackageData(dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength))));
			} catch (Exception e) {
				throw new UnsupportedPackageException(e, dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength));
			}
	
		}
	}

	long getID() {
		return this.id;
	}

	InetAddress getConnectionAdress() {
		return this.connection.getInetAddress();
	}
}
