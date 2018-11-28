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
import data.readableData.ReadableData;

public class Client {

	private static final int Port = 12345;
	
	private Socket fromSocket;
	private Socket toSocket;
	
	public Client(String ip){
		try {
			InetAddress Host = InetAddress.getByName(ip);
			ip =Host.getHostAddress();
		} catch (UnknownHostException ea){}
		System.out.println("server IP: "+ip+" found");
		loadClient(ip, Port);
	}
	
	private void loadClient(String ip, int port) {
		try {
			fromSocket = new Socket(ip, port);
			System.out.println("from: "+fromSocket.getInetAddress());
			toSocket = new Socket(ip, port+1);
			System.out.println("to:   "+toSocket.getInetAddress());
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {					

					Queue<DataPackage> dataStream = new Queue<>();
					byte[] income = new byte[DataPackage.MAXPACKAGELENGTH];
					try {
						for(int length = fromSocket.getInputStream().read(income); length!=-1; length = fromSocket.getInputStream().read(income)){
							System.out.println("\""+new String(income, "UTF-8")+"\"");	
							
							DataPackage dataPackage = null;
							try {
								dataPackage = new DataPackage(income, length);
							} catch (Exception ea) {ea.printStackTrace();}
							
							if(dataPackage!=null){
								dataStream.add(dataPackage);
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
										FromServer(PackageType.readPackageData(dataPackage.getId(), Arrays.copyOfRange(data.array(), 0, actuallLength)));
									} catch (Exception e) {
										e.printStackTrace();
										System.out.println("Unsupported Package ID: "+dataPackage.getId()+" with data: "+data);
									}
								}
							}
							income = new byte[DataPackage.PACKAGESIZE];
						}
					} catch (IOException e) {
						System.out.println("Error while reading income");
						fromSocket = null;
					}
						
				}
			}).start();
			
		} catch (IOException e) {
			System.out.println("No Server Found");
			endClient();
		}
	}

	protected void endClient() {}

	public Client(String ip, int port) {
		try {
			InetAddress Host = InetAddress.getByName(ip);
			ip =Host.getHostAddress();
		} catch (UnknownHostException ea){}
		System.out.println("server IP: "+ip+" found");
		if(port!=0)loadClient(ip, port);
		else loadClient(ip, Port);
	}

	protected void FromServer(PackageType type) {
		System.out.print(type.getId()+": "+type.getName()+"->");
		for(ReadableData<?> data: type.getDataStructures())System.out.print(data.getData().toString());
		System.out.println();
	}
	
	public void sendToServer(Queue<DataPackage> packages){
		if(toSocket!=null && toSocket.isConnected() && !toSocket.isClosed()){
			try {
				while(!packages.isEmpty()){
					System.out.println(new String(packages.get().getByteData(), "UTF-8"));
					toSocket.getOutputStream().write(packages.get().getByteData());
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
		return fromSocket!=null && toSocket!=null;
	}

}
