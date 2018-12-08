package data.db;

import java.util.ArrayList;

import data.user.User;

public class Userdb {
	
	private InternalFilemanager filemanager;
	
	private ArrayList<User> registeredUsers = new ArrayList<>();
	
	public Userdb(String generalPassword){
		this.filemanager = new InternalFilemanager(generalPassword);
		this.registeredUsers = filemanager.getAllUser();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<User> getUsers(){
		return (ArrayList<User>) this.registeredUsers.clone();
	}
	
	public User saveUser(User user){
		user = filemanager.saveUser(user);
		if(user!=null)this.registeredUsers.add(user);
		return user;
	}

}
