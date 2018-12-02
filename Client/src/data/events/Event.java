package data.events;

public class Event {
	
	protected boolean active;
	
	public Event(){
		this.active = true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
