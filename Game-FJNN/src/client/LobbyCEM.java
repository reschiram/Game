package client;

import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.readableData.BooleanData;
import data.readableData.IntegerData;
import data.readableData.LongData;
import data.readableData.StringData;
import launcher.lobby.Lobby;

public class LobbyCEM implements ToClientMessageEventListener{

	private Lobby lobby;
	
	public LobbyCEM(Lobby lobby) {
		this.lobby = lobby;
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if (event.getMessage().getId() == GameCPM.DataPackage_MapDownloadInfos) {
			this.lobby.getMapDownloader().load(
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
			this.lobby.getMapDownloader().addData(data,
				((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue());
			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_LobbyPlayerStatus) {
			String username = ((StringData) event.getMessage().getDataStructures()[0]).getData();
			boolean isHost = ((BooleanData) event.getMessage().getDataStructures()[1]).getData().booleanValue();
			int status = ((IntegerData) event.getMessage().getDataStructures()[2]).getData().intValue();
			this.lobby.updatPlayerStatus(username, isHost, status);
			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_PlayerMessage) {
			String username = ((StringData) event.getMessage().getDataStructures()[0]).getData();
			String message = ((StringData) event.getMessage().getDataStructures()[1]).getData();
			lobby.getGUI().println("[" + username + "] : " + message);
			event.setActive(false);
		} else if (event.getMessage().getId() == GameCPM.DataPackage_StartGame) {
			long time = ((LongData) event.getMessage().getDataStructures()[0]).getData().longValue();
			lobby.startGame(time);
			event.setActive(false);
		}
	}
}
