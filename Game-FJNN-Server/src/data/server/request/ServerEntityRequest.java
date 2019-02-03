package data.server.request;

import data.entities.ServerEntity;

public class ServerEntityRequest extends ServerRequest {

	private ServerEntity serverEntity;

	public ServerEntityRequest(int clientRequestID, ServerEntity serverEntity) {
		super(clientRequestID);
		this.serverEntity = serverEntity;
	}

	public ServerEntity getServerEntity() {
		return serverEntity;
	}

}
