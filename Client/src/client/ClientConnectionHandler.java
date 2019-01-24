package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.client.ClientLostConnectionToServerEvent;
import data.exceptions.UnsupportedPackageException;
import data.exceptions.client.ServerNotFoundException;
import data.exceptions.handler.DefaultExceptionHandler;

public class ClientConnectionHandler {
	
	private String ip;
	private int port;
	
	private Socket socket;
	private Client client;
	
	private DataInputStream in;
	private BufferedOutputStream out;
	
	private boolean ended = false;

	ClientConnectionHandler(Client client, String ip, int port) {
		try {
			InetAddress Host = InetAddress.getByName(ip);
			ip =Host.getHostAddress();
		} catch (UnknownHostException ea){}
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	void connect() throws ServerNotFoundException{
		try {
			
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(false);
			socket.setReceiveBufferSize(DataPackage.MAXPACKAGELENGTH);
			socket.setSendBufferSize(DataPackage.MAXPACKAGELENGTH);

			this.out = new BufferedOutputStream(socket.getOutputStream(), DataPackage.MAXPACKAGELENGTH);
			this.in = new DataInputStream(socket.getInputStream());
			
			new Thread(new Runnable() {				
				@Override
				public void run() {							
					while(isConnected()){
						try {
							acceptData();
						} catch (UnsupportedPackageException e) {
							DefaultExceptionHandler.getDefaultExceptionHandler().getDefaultHandler_UnsupportedPackageException().handleError(e);
						}
					}					
				}
			}).start();
			
		} catch (IOException e) {
			throw new ServerNotFoundException(e, ip, port);
		}
	}
	
	private HashMap<Integer, Queue<DataPackage>> dataStreams = new HashMap<>();

	private void acceptData() throws UnsupportedPackageException {
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(in.readFully(income); isConnected(); in.readFully(income)){
				income = Arrays.copyOf(income, DataPackage.MAXPACKAGELENGTH);	
				
				DataPackage dataPackage = null;
				try {
					dataPackage = new DataPackage(income, DataPackage.MAXPACKAGELENGTH);
				} catch (Exception ea) {ea.printStackTrace();}
				
				if(dataPackage!=null){
					
					if(!this.dataStreams.containsKey(dataPackage.getId())) this.dataStreams.put(dataPackage.getId(), new Queue<DataPackage>());
					Queue<DataPackage> dataStream = dataStreams.get(dataPackage.getId());
					if(dataPackage.isEnd()) {
						this.dataStreams.remove(dataPackage.getId());	
						handNewDataPackage(dataPackage, dataStream);
					}					
				}
				
				income = new byte[DataPackage.MAXPACKAGELENGTH];
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(this.client.run() && !ended){
				this.client.endClient();
				this.client.connectionLost(new ClientLostConnectionToServerEvent(this.socket));
			}
			ended = true;
		}
	}

	private void printData(byte[] income) {
		String msg = "In: "+income.length+" [";
		for(int i = 0; i < 20; i++)msg += income[i] + ",";
		System.out.println(msg+"]");
	}

	private void handNewDataPackage(DataPackage dataPackage, Queue<DataPackage> dataStream) throws UnsupportedEncodingException, UnsupportedPackageException {
		dataStream.add(dataPackage);
		if(dataPackage.isEnd()){
			ByteBuffer data = ByteBuffer.allocate(DataPackage.PACKAGESIZE);
			int actuallLength = 0;
			while(!dataStream.isEmpty()){				
				actuallLength+=dataStream.get().getByteData().length;
				if(actuallLength > DataPackage.PACKAGESIZE) throw new UnsupportedPackageException(null, dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength));
				data.put(dataStream.get().getByteData(), DataPackage.ID_Length, dataStream.get().getByteData().length-DataPackage.ID_Length);
				dataStream.remove();
			}
			try {
				this.client.fromServer(PackageType.readPackageData(dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength)));
			} catch (Exception e) {
				throw new UnsupportedPackageException(e, dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength));
			}
		}
	}

	void send(Queue<DataPackage> packages) {
		try {
			while(!packages.isEmpty()){
				out.write(packages.get().getByteData());
				packages.remove();
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			if(this.client.run() && !ended){
				this.client.endClient();
				this.client.connectionLost(new ClientLostConnectionToServerEvent(this.socket));
			}
			ended = true;
		}
	}

	boolean isConnected() {
		return socket!=null && socket.isConnected() && !socket.isClosed() && client.run();
	}

	void endConnection() {
		this.socket = null;
	}

	Socket getConnection() {
		return socket;
	}

	boolean isEnded() {
		return ended;
	}

}
