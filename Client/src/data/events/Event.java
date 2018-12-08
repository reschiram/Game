package data.events;

public class Event {
	
	protected boolean active;
	protected long created;
	
	public Event(){
		this.active = true;
		this.created = System.currentTimeMillis();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getCreatedTime() {
		return created;
	}

}
