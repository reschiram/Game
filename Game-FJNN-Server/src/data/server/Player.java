package data.server;

import java.util.ArrayList;

import data.entities.ServerEntity;
import server.ValidatedUser;

public class Player {

	private ValidatedUser validatedUser;
	private boolean hasCatchedUp;
	
	private ArrayList<ServerEntity> knownEntity = new ArrayList<>();

	public Player(ValidatedUser validatedUser, boolean hasCatchedUp) {
		super();
		this.validatedUser = validatedUser;
		this.hasCatchedUp = hasCatchedUp;
	}
	
	public ValidatedUser getValidatedUser() {
		return validatedUser;
	}
	
	public boolean hasCatchedUp() {
		return hasCatchedUp;
	}
	
	public void setHasCatchedUp(boolean catchUpStatus) {
		this.hasCatchedUp = catchUpStatus;
	}
	
	public void addKnownEntity(ServerEntity entity) {
		this.knownEntity.add(entity);
	}
	
	public boolean knowsEntity(ServerEntity entity) {
		System.out.println("Entity Knwon: " + entity);
		return this.knownEntity.contains(entity);
	}
}
