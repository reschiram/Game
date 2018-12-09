package data.exceptions;

public class ClientLoginException extends CustomException{
	
	private static final long serialVersionUID = 1L;
	
	public static final String Reason_ClientInformException = "Packages to inform client could not be created.";
	
	private String reason;

	public ClientLoginException(Exception exception, long clientID, String reason) {
		super(exception, "Login progress for client: "+clientID+" has failed. reason: "+reason);
		this.reason = reason;		
	}

	public String getReason() {
		return reason;
	}

}
