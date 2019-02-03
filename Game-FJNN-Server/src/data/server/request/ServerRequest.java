package data.server.request;

import client.Request;

public class ServerRequest extends Request{
	
	private int clientRequestID;

	public ServerRequest(int clientRequestID) {
		this.clientRequestID = clientRequestID;
	}
	
	public int getClientRequestID() {
		return clientRequestID;
	}
}
