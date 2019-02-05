package launcher.lobby;

public class SPlayer {
	
	private String username;
	private boolean isHost;
	
	public SPlayer(String username, boolean isHost) {
		super();
		this.username = username;
		this.isHost = isHost;
	}

	public String getUsername() {
		return username;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

}
