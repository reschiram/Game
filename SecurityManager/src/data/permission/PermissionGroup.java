package data.permission;

import java.util.ArrayList;

public class PermissionGroup {
	
	private String groupName;
	private ArrayList<Permission> permissions;

	public PermissionGroup(String groupName, ArrayList<Permission> permissions) {
		this.groupName = groupName;
		this.permissions = permissions;
	}

	public String getName() {
		return groupName;
	}

	public ArrayList<Permission> getPermissions() {
		return permissions;
	}

}
