package data.entities;

import Data.Location;
import data.Velocity;
import data.map.ServerMap;
import data.server.Player;
import game.entity.requester.EntityRequesterService;
import game.entity.type.EntityType;
import game.inventory.equipment.EquipmentInventory;

public class ServerPlayerEntity extends ServerEntity{
	
	private Player user;
	private EquipmentInventory inv;
	private Velocity velocity = new Velocity();
	
	public ServerPlayerEntity(int id, Location pixeLoc, EntityType entityType, Player user, EquipmentInventory inv, ServerMap serverMap) {
		super(id, pixeLoc, entityType, serverMap);
		this.user = user;
		this.inv = inv;
	}

	public Player getPlayer() {
		return user;
	}

	public EquipmentInventory getInv() {
		return inv;
	}

	@Override
	public String getExtraInfos(long currentClientID) throws Exception {
		String extraInfos = "";
		extraInfos += EntityRequesterService.getEntityRequesterService().readIsOwnPlayer(currentClientID == user.getServerClientID());
		extraInfos += EntityRequesterService.getEntityRequesterService().readInventory(inv);
		return extraInfos;
	}

	public void setVelocity(int velocityX, int velocityY) {
		this.velocity.setXSpeed(velocityX);
		this.velocity.setYSpeed(velocityY);
	}

	public Velocity getVelocity() {
		return this.velocity;
	}

}
