package game.inventory.requester;

import java.util.HashMap;

import client.GameCM;
import client.GameCPM;
import data.DataPackage;
import data.PackageType;
import data.events.client.ToClientMessageEvent;
import data.events.client.ToClientMessageEventListener;
import data.readableData.IntegerData;

public class InventoryRequester implements ToClientMessageEventListener {
	
	private static InventoryRequester inventoryRequester;
	public static InventoryRequester getInventoryRequester() {
		return inventoryRequester;
	}
	
	private GameCM gameCM;		
	private HashMap<Integer, InventoryRequest> requests = new HashMap<>();
	private int lastRequest = 0;
	
	public InventoryRequester(GameCM gameCM) {
		inventoryRequester = this;
		this.gameCM = gameCM;
		
		gameCM.getClientManager().getEventManager().registerClientMessageEventListener(this, 4);
	}

	@Override
	public void messageFromServer(ToClientMessageEvent event) {
		if(event.getMessage().getId() == GameCPM.DataPackage_InventoryResponse) {
			int requestID = ((IntegerData) event.getMessage().getDataStructures()[0]).getData().intValue();
			int invID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			
			if(!this.requests.containsKey(requestID)) {
				System.out.println("Error accepting inventory. Request unknwon");
				return;
			}
			
			InventoryRequest request = this.requests.get(requestID);
			this.requests.remove(requestID);
			
			request.acceptInventory(invID);
			event.setActive(false);
		}else if(event.getMessage().getId() == GameCPM.DataPackage_InventoryRequest) {
			int oldRequestID = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			int invSize = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();
			int equipmentSlots = ((IntegerData) event.getMessage().getDataStructures()[1]).getData().intValue();

			InventoryRequest request = new InventoryRequest(invSize, equipmentSlots, invAcceptor);
			int requestID = lastRequest+1;
			lastRequest++;
			requests.put(requestID, request);
			
			sendRequest(invSize, equipmentSlots, requestID, oldRequestID);
		}
	}

	public void requestInventory(int invSize, int equipmentSlots, InventoryAcceptor invAcceptor) {
		InventoryRequest request = new InventoryRequest(invSize, equipmentSlots, invAcceptor);
		int requestID = lastRequest+1;
		lastRequest++;
		requests.put(requestID, request);
		
		sendRequest(invSize, equipmentSlots, requestID, 0);
	}

	private void sendRequest(int invSize, int equipmentSlots, int requestID, int oldRequestID) {
		try{
			PackageType message = PackageType.readPackageData(GameCPM.DataPackage_InventoryRequest, oldRequestID,
					requestID, invSize, equipmentSlots);
			gameCM.getClientManager().sendToServer(DataPackage.getPackage(message));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
