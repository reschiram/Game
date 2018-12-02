package test.server.main;

import data.DataPackage;
import data.PackageType;
import data.readableData.StringData;
import test.server.TestServer;

public class ServerTestMain {
	
	public static void main(String[] args){
		new ServerTestMain();
	}
	
	private TestServer testServer;
	
	public ServerTestMain(){
		this.testServer = new TestServer();
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
	}

}
