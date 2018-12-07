package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
			throw new ServerNotFoundException(e, this.client, ip, port);
		}
	}

	private void acceptData() throws UnsupportedPackageException {
		Queue<DataPackage> dataStream = new Queue<>();
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(int length = socket.getInputStream().read(income); length!=-1 && isConnected(); length = socket.getInputStream().read(income)){
				income = Arrays.copyOf(income, DataPackage.MAXPACKAGELENGTH);				
								
				DataPackage dataPackage = null;
				try {
					dataPackage = new DataPackage(income, length);
				} catch (Exception ea) {ea.printStackTrace();}
				
				if(dataPackage!=null){
					handNewDataPackage(dataPackage, dataStream);
				}
				
				income = new byte[DataPackage.PACKAGESIZE];
			}
		} catch (IOException e) {
			if(this.client.run() && !ended){
				this.client.endClient();
				this.client.connectionLost(new ClientLostConnectionToServerEvent(this.socket));
			}
			ended = true;
		}
	}

	private void handNewDataPackage(DataPackage dataPackage, Queue<DataPackage> dataStream) throws UnsupportedEncodingException, UnsupportedPackageException {
		dataStream.add(dataPackage);
		if(dataPackage.isEnd()){
			ByteBuffer data = ByteBuffer.allocate(DataPackage.PACKAGESIZE);
			int actuallLength = 0;
			while(!dataStream.isEmpty()){
				actuallLength+=dataStream.get().getByteData().length;
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
				socket.getOutputStream().write(packages.get().getByteData());
				packages.remove();
			}
		} catch (IOException e) {
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
