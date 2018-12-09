package data.db;

import java.util.ArrayList;

import data.DefaultUserExceptionHandler;
import data.exceptions.UserDatabaseReadingException;
import data.user.User;

public class Userdb {
	
	private InternalFilemanager filemanager;
	
	private ArrayList<User> registeredUsers = new ArrayList<>();
	
	public Userdb(String generalPassword){
		this.filemanager = new InternalFilemanager(generalPassword);
		try {
			this.registeredUsers = filemanager.getAllUser();
		} catch (UserDatabaseReadingException e) {
			DefaultUserExceptionHandler.getDefaultUserExceptionHandler().getDefaultHandler_UserDatabaseReadingException().handleError(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<User> getUsers(){
		return (ArrayList<User>) this.registeredUsers.clone();
	}
	
	public User saveUser(User user) throws UserDatabaseReadingException{
		user = filemanager.saveUser(user);
		if(user!=null)this.registeredUsers.add(user);
		return user;
	}
	
	public void removeUser(String userID) throws UserDatabaseReadingException{
		filemanager.removeUser(userID);
	}

}
