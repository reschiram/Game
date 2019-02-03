package data.entities;

import Data.Location;
import data.Velocity;
import game.entity.requester.EntityRequesterService;
import game.entity.type.EntityType;
import game.inventory.equipment.EquipmentInventory;
import server.ValidatedUser;

public class ServerPlayerEntity extends ServerEntity{
	
	private ValidatedUser user;
	private EquipmentInventory inv;
	private Velocity velocity = new Velocity();
	
	public ServerPlayerEntity(int id, Location pixeLoc, EntityType entityType, ValidatedUser user, EquipmentInventory inv) {
		super(id, pixeLoc, entityType);
		this.user = user;
		this.inv = inv;
	}

	public ValidatedUser getUser() {
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
