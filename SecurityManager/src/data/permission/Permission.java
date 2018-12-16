package data.permission;

public class Permission {
	
	private String name;

	public Permission(String name) {
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public String toString(){
		return "Permission: "+this.name;
	}

}
