package test.server.main;

import data.DataPackage;
import data.Funktions;
import data.PackageType;
import data.readableData.StringData;
import test.server.TestServer;

public class ServerTestMain {
	
	private TestServer testServer;
	
	public static void main(String[] args){
		new ServerTestMain();
	}
	
	
	public ServerTestMain(){
		
		testServer = new TestServer();
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType(30, "test2", new StringData("test30", 30), new StringData("test50", 50), new StringData("test20", 20)));
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(testServer.isConnected()){
					testServer.tick();
					Funktions.wait(1);
				}
			}
		}).start();;
	}

}
