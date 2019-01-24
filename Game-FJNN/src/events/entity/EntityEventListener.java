package events.entity;

public interface EntityEventListener {
	
	public void entityPathFinder(EntityPathEvent event);
	public void entityStatusChange(EntityStatusEvent event);
	public void playerMove(PlayerMoveEvent event);

}
