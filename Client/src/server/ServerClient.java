package server;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
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
	private BufferedOutputStream out;
	private DataInputStream in;
	private Long id;
	
	private Server server;
	
	private long lastHeartbeat = -1;
	private boolean ended = false;
	
	ServerClient(Server server, Socket client, Long id) {
		this.connection = client;
		this.id = id;
		this.server = server;
		
		try {
			connection.setTcpNoDelay(false);
			connection.setReceiveBufferSize(DataPackage.MAXPACKAGELENGTH);
			connection.setSendBufferSize(DataPackage.MAXPACKAGELENGTH);
			
			this.out = new BufferedOutputStream(connection.getOutputStream(), DataPackage.MAXPACKAGELENGTH);
			this.in = new DataInputStream(connection.getInputStream());
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

	private void printData(byte[] income) {
		String msg = "In: "+income.length+" [";
		for(int i = 0; i < 20 && income.length > i; i++)msg += income[i] + ",";
		System.out.println(msg+"]");
	}
	
	void flush() {
		try {
			this.out.flush();
		} catch (IOException e) {
			if(!isConnected()){
				if(!ended){
					ended = true;
					server.getServerManager().getEventManager().publishServerEvent(new ServerLostConnectionToClientEvent(id, connection));
				}
			}
		}
	}

	boolean isConnected() {
		return connection!=null && connection.isConnected() && !connection.isClosed() && !ended;
	}

	void scanForIncomingData() throws UnsupportedPackageException {
		Queue<DataPackage> dataStream = new Queue<>();
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(in.readFully(income); isConnected(); in.readFully(income)){
				income = Arrays.copyOf(income, DataPackage.MAXPACKAGELENGTH);				
								
				DataPackage dataPackage = null;
				try {
					dataPackage = new DataPackage(income, DataPackage.MAXPACKAGELENGTH);
				} catch (Exception ea) {ea.printStackTrace();}
				
				if(dataPackage!=null){
					if(dataPackage.getId() == DataPackage.PackageType_Heartbeat)this.lastHeartbeat = System.currentTimeMillis();
					else handNewDataPackage(dataPackage, dataStream);
				}
				
				income = new byte[DataPackage.MAXPACKAGELENGTH];
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

	void closeConnection() {
		this.ended = true;
	}
}
