package data.user;

public class User {
	
	public static final User Default_User = new User("000000000000000000000000", "Default_User", "--------");

	public static final int ID_Length = 25;	
	public static final int maxPasswordLength = 25;
		
	private String id;
	private String name;
	private String password;
	
	public User(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public String getUsername() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}
	
	@Override
	public String toString(){
		return "{[UserID:"+id+"],[Username:"+name+"]}";
	}
	
	public String toStringWithPassword(){
		return "{[UserID:"+id+"],[Username:"+name+"],[Password:"+password+"]}";
	}

}
