package test;

import client.Client;
import data.PackageType;
import data.readableData.ReadableData;
import test.main.Main;

public class TestClient extends Client{

	private Main main;
	
	public TestClient(Main main, String ip, int port) {
		super(ip, port);
		this.main = main;
	}
	
	@Override
	protected void FromServer(PackageType type){
		String text = type.getId()+": "+type.getName()+"-> ";
		for(ReadableData<?> data: type.getDataStructures())text+=data.getData().toString()+ "|";
		if(type.getDataStructures().length>0)text = text.substring(0, text.length()-3);
		this.main.getGUI().println(text);
	}
	
	@Override
	protected void endClient() {
		System.exit(0);
	}

}
