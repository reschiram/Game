package server;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

import data.DefaultUserExceptionHandler;
import data.db.Userdb;
import data.exceptions.ClientValidationException;
import data.exceptions.LoginInformationCreationException;
import data.exceptions.LoginInformationReadingException;
import data.exceptions.UnknownUserException;
import data.exceptions.UserAlreadyKnwonException;
import data.exceptions.UserDatabaseReadingException;
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
			
			try {
				String userLoginInfos = this.getUserLoginInfo(user, mod);
				String decodedLoginInfo = this.getDecodedLoginInfo(encodedLoginInfo, user, mod);
				if(decodedLoginInfo.equals(userLoginInfos))return user;
			} catch (LoginInformationReadingException e) {
				DefaultUserExceptionHandler.getDefaultUserExceptionHandler().getDefaultHandler_LoginInformationReadingException().handleError(e);
			} catch (LoginInformationCreationException e) {
				DefaultUserExceptionHandler.getDefaultUserExceptionHandler().getDefaultHandler_LoginInformationCreationException().handleError(e);
			}
			
		}
		throw new ClientValidationException(encodedLoginInfo);
	}

	private String getDecodedLoginInfo(String encodedLoginInfo, User user, BigInteger mult) throws LoginInformationReadingException {		
		try {
			return this.deEnCode.decode(encodedLoginInfo, new String(new BigInteger(user.getPassword().getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat));
		} catch (UnsupportedEncodingException e) {
			throw new LoginInformationReadingException(e, user, encodedLoginInfo, mult);
		}
	}

	void registerNewUser(User user) throws UserDatabaseReadingException, UserAlreadyKnwonException {
		if(this.userdb.hasUserName(user.getUsername())) throw new UserAlreadyKnwonException(user);
		this.userdb.saveUser(user);
	}

	ArrayList<User> getAllRegisteredUsers() {
		return this.userdb.getUsers();
	}
	
	void deleteUser(String userID) throws UserDatabaseReadingException, UnknownUserException{
		if(!this.userdb.hasUserID(userID)) throw new UnknownUserException(userID);
		this.userdb.removeUser(userID);
	}

	User getUserFromID(String userID) throws UnknownUserException {
		if(!this.userdb.hasUserID(userID)) throw new UnknownUserException(userID);
		return this.userdb.findByID(userID);
	}

}
