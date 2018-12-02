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

public class ServerClient {
	
	private Socket connection;
	private OutputStream out;
	private InputStream in;
	private Long id;
	
	private Server server;

	public ServerClient(Server server, Socket client, Long id) {
		this.connection = client;
		this.id = id;
		this.server = server;
		
		try {
			this.out = client.getOutputStream();
			this.in = client.getInputStream();
		} catch (IOException e) {e.printStackTrace();}
	}

	public void acceptConnection() {
		this.server.getServerManager().publishNewClientConnectionEvent(this);
		
		try {
			Queue<DataPackage> dbs = DataPackage.getPackage(PackageType.readPackageData(DataPackage.PackageType_InitConnection, id));
			sendToClient(dbs);
		} catch (Exception e) {e.printStackTrace();}
	}

	private void sendToClient(Queue<DataPackage> packages) {
		if(isConnected()){
			try {
				while(!packages.isEmpty()){
					System.out.println(new String(packages.get().getByteData(), "UTF-8"));
					this.out.write(packages.get().getByteData());
					packages.remove();
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println("Not Supported Char detected");
			} catch (IOException e) {
				System.out.println("Error while contacting the server");
			}
		}
	}

	public boolean isConnected() {
		return connection!=null && connection.isConnected() && !connection.isClosed();
	}

	public void scanForIncomingData() {
		Queue<DataPackage> dataStream = new Queue<>();
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(int length = this.in.read(income); length!=-1 && isConnected(); length = this.in.read(income)){
				System.out.println("\""+new String(income, "UTF-8")+"\"");	
				
								
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
			System.out.println("Error while reading income");
			this.connection = null;
		}
	}
	
	private void handNewDataPackage(DataPackage dataPackage, Queue<DataPackage> dataStream) throws UnsupportedEncodingException {dataStream.add(dataPackage);
		if(dataPackage.isEnd()){
			ByteBuffer data = ByteBuffer.allocate(DataPackage.PACKAGESIZE);
			int actuallLength = 0;
			while(!dataStream.isEmpty()){
				System.out.println("\""+new String(dataStream.get().getByteData(), "UTF-8")+"\""+DataPackage.ID_Length+"->"+(dataStream.get().getByteData().length-DataPackage.ID_Length));
				actuallLength+=dataStream.get().getByteData().length;
				data.put(dataStream.get().getByteData(), DataPackage.ID_Length, dataStream.get().getByteData().length-DataPackage.ID_Length);
				dataStream.remove();
			}
			try {
				this.server.getServerManager().publishNewMessageEvent(PackageType.readPackageData(dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength)));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unsupported Package ID: "+dataPackage.getId()+" with data: "+data);
			}
	
		}
	}

	public long getId() {
		return this.id;
	}

	public InetAddress getConnectionAdress() {
		return this.connection.getInetAddress();
	}
}
