package data.exceptions;

import data.exceptions.client.ClientException;

public class ClientValidationException extends ClientException {

	private static final long serialVersionUID = 1L;
	
	private String encodedLoginInfo;
	
	public ClientValidationException(String encodedLoginInfo) {
		super(null, "Could not find User with the loginInfo:"+encodedLoginInfo);
		this.encodedLoginInfo = encodedLoginInfo;
	}

	public String getEncodedLoginInfo() {
		return encodedLoginInfo;
	}

}
