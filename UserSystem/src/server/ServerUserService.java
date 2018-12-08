package server;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

import data.db.Userdb;
import data.exceptions.ClientValidationException;
import data.user.User;
import data.user.UserService;

public class ServerUserService extends UserService{
	
	private Userdb userdb;
	
	ServerUserService(String generalPassword){
		this.userdb = new Userdb(generalPassword);
	}
	
	User getUser(String encodedLoginInfo) throws ClientValidationException{
		ArrayList<User> registeredUsers = this.userdb.getUsers();
		
		BigInteger mod = getModForLoginInfo();		
		for(int i = 0; i<registeredUsers.size(); i++){
			User user = registeredUsers.get(i);
			
			String decodedLoginInfo = this.getDecodedLoginInfo(encodedLoginInfo, user, mod);
			String userLoginInfos = this.getUserLoginInfo(user, mod);
			
			if(decodedLoginInfo.equals(userLoginInfos))return user;
		}
		throw new ClientValidationException(encodedLoginInfo);
	}

	private String getDecodedLoginInfo(String encodedLoginInfo, User user, BigInteger mult) {		
		try {
			return this.deEnCode.decode(encodedLoginInfo, new String(new BigInteger(user.getPassword().getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat));
		} catch (UnsupportedEncodingException e) {
			System.out.println("A fatal error has occoured while trying to decode loginInfos for user: "+user.getUsername());
		}		
		
		return "";
	}

	public void registerNewUser(User user) {
		this.userdb.saveUser(user);
	}

	public ArrayList<User> getAllRegisteredUsers() {
		return this.userdb.getUsers();
	}

}
