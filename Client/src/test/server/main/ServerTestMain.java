package test.server.main;

import data.DataPackage;
import data.PackageType;
import data.readableData.StringData;
import test.server.TestServer;

public class ServerTestMain {
	
	public static void main(String[] args){
		new ServerTestMain();
	}
	
	
	public ServerTestMain(){
		
		new TestServer();
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType(30, "test2", new StringData("test30", 30), new StringData("test50", 50), new StringData("test20", 20)));
	}

}
