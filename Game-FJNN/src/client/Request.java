package client;

public abstract class Request {
	
	private static int lastRequestID = 1;
	
	private int requestID;	

	public Request() {
		super();
		lastRequestID++;
		this.requestID = lastRequestID;
	}

	public int getRequestID() {
		return requestID;
	}
}
