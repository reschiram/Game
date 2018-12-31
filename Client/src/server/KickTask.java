package server;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.events.server.ServerLostConnectionToClientEvent;

public class KickTask implements ServerTask{
	
	private long clientID;
	
	private Queue<DataPackage> message;
	
	public KickTask(long clientID, String reason) throws Exception {
		this.clientID = clientID;
		
		this.message = DataPackage.getPackage(PackageType.readPackageData(DataPackage.PackageType_Kick, reason));
	}

	@Override
	public void act(ServerClient sc, ServerManager serverManager) {
		sc.sendToClient(message);
		sc.closeConnection();
		
		serverManager.getEventManager().publishServerEvent(new ServerLostConnectionToClientEvent(clientID, null));
	}

	@Override
	public long getClientID() {
		return clientID;
	}

}
