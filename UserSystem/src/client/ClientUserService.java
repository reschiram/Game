package client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import data.exceptions.LoginInformationCreationException;
import data.user.User;
import data.user.UserService;

public class ClientUserService extends UserService{
	
	public ClientUserService(){
		
	}
	
	String getEncodedLoginInfo(User user) throws LoginInformationCreationException{
		BigInteger mult = getModForLoginInfo();
		
		try {
			return this.deEnCode.encode(this.getUserLoginInfo(user, mult), new String(new BigInteger(user.getPassword().getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat));
		} catch (UnsupportedEncodingException e) {
			throw new LoginInformationCreationException(e, user.getUsername(), user.getPassword());
		}
	}

}
