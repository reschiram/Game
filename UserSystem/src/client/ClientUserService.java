package client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import data.user.User;
import data.user.UserService;

public class ClientUserService extends UserService{
	
	public ClientUserService(){
		
	}
	
	String getEncodedLoginInfo(User user){
		BigInteger mult = getModForLoginInfo();
		
		try {
			return this.deEnCode.encode(this.getUserLoginInfo(user, mult), new String(new BigInteger(user.getPassword().getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat));
		} catch (UnsupportedEncodingException e) {
			System.out.println("A fatal error has occoured while trying to create loginInfos for user: "+user.getUsername());
		}
		
		return "";
	}

}
