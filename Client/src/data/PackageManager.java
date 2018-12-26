package data;

import data.events.client.ToClientMessageEvent;
import data.readableData.StringData;

public class PackageManager {

	public static String readKickMessage(ToClientMessageEvent event){
		String message = ((StringData)event.getMessage().getDataStructures()[0]).getData();
		return message;
	}
}
