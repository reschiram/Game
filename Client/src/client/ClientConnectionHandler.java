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

public class ClientConnectionHandler {
	
	private String ip;
	private int port;
	
	private Socket socket;
	private Client client;

	public ClientConnectionHandler(Client client, String ip, int port) {
		try {
			InetAddress Host = InetAddress.getByName(ip);
			ip =Host.getHostAddress();
		} catch (UnknownHostException ea){}
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	public void connect(){
		try {
			socket = new Socket(ip, port);
			System.out.println("from: "+socket.getInetAddress());
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {		
					
					while(isConnected()){
						acceptData();
					}
					
				}
			}).start();
			
		} catch (IOException e) {
			System.out.println("No Server Found");
			this.client.endClient();
		}
	}

	private void acceptData() {
		Queue<DataPackage> dataStream = new Queue<>();
		byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
		try {
			for(int length = socket.getInputStream().read(income); length!=-1 && isConnected(); length = socket.getInputStream().read(income)){
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
			socket = null;
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
				this.client.fromServer(PackageType.readPackageData(dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength)));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unsupported Package ID: "+dataPackage.getId()+" with data: "+data);
			}
		}
	}

	public void send(Queue<DataPackage> packages) {
		try {
			while(!packages.isEmpty()){
				System.out.println(new String(packages.get().getByteData(), "UTF-8"));
				socket.getOutputStream().write(packages.get().getByteData());
				packages.remove();
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("Not Supported Char detected");
		} catch (IOException e) {
			System.out.println("Error while contacting the server");
		}
	}

	public boolean isConnected() {
		return socket!=null && socket.isConnected() && !socket.isClosed() && client.run();
	}

}
