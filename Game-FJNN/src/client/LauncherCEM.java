package client;

import data.events.AccessDeniedEvent;
import data.events.AccessDeniedEventListner;
import data.events.ClientLoginEvent;
import data.events.ClientLoginEventListener;
import data.events.client.ClientLostConnectionToServerEvent;
import data.events.client.ClientLostConnectionToServerEventListener;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.readableData.IntegerData;
import launcher.Launcher;

public class LauncherCEM implements ClientLostConnectionToServerEventListener, ToClientMessageEventListener, ClientLoginEventListener, AccessDeniedEventListner{
	
	private Launcher launcher;
	
	public LauncherCEM(Launcher launcher) {
		this.launcher = launcher;
	}

	@Override
	public void connectionLost(ClientLostConnectionToServerEvent event) {
	}

	@Override
	public void handleNoPermissionException(AccessDeniedEvent event) {
	}

	@Override
	public void ClientLogin(ClientLoginEvent event) {
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if (event.getMessage().getId() == GameCPM.DataPackage_MapDownloadInfos) {
			this.launcher.getMapDownloader().load(
				((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue(),
				((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue(),
				((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue(),
				((IntegerData) event.getMessage().getDataStructures()[3]).getData().intValue());
			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_MapDownloadData) {
			int[] data = new int[GameCPM.MapDownloadData_DataCount * 4];
			for (int i = 0; i < data.length; i++) {
				data[i] = ((IntegerData) event.getMessage().getDataStructures()[i + 1]).getData().intValue();
			}
			this.launcher.getMapDownloader().addData(data,
				((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue());
		}
	}

}
