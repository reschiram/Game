package data.entities;

import Data.Location;
import data.SEPathManager;
import data.map.ServerMap;
import game.entity.requester.EntityRequesterService;
import game.entity.type.EntityType;
import game.inventory.Inventory;

public class ServerDroneEntity extends ServerEntity{
	
	private int droneType;
	private Inventory inv;
	private ServerPlayerEntity droneHost;
	
	private SEPathManager sePathManager;

	public ServerDroneEntity(int id, Location pixeLoc, EntityType entityType, int droneType, Inventory inv, ServerPlayerEntity droneHost, ServerMap serverMap) {
		super(id, pixeLoc, entityType, serverMap);
		this.droneType = droneType;
		this.inv = inv;
		this.droneHost = droneHost;
		
		this.sePathManager = new SEPathManager(this);
	}
	
	public int getDroneType() {
		return droneType;
	}

	public Inventory getInv() {
		return inv;
	}

	public ServerPlayerEntity getDroneHost() {
		return droneHost;
	}

	@Override
	public String getExtraInfos(long currentClientID) throws Exception {
		String extraInfos = "";
		extraInfos += EntityRequesterService.getEntityRequesterService().readInventory(inv);
		return extraInfos;
	}

	public SEPathManager getSEPathManager() {
		return sePathManager;
	}

}
