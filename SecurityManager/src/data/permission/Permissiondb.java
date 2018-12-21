package data.permission;

import java.util.ArrayList;
import java.util.HashMap;

public class Permissiondb {
	
	private ArrayList<PermissionGroup> permissionGroups;
	private ArrayList<Permission> permissions;
	private HashMap<String, ArrayList<PermissionGroup>> userPermissionGroups = new HashMap<>();
	
	private InternalFilemanager fileManager;
	
	public Permissiondb(){
		this.fileManager = new InternalFilemanager(this);
	}
	
	public Permissiondb loadData(){
		this.permissionGroups = fileManager.readAllGroups();
		this.permissions = fileManager.readAllPermissions();
		return this;
	}

	PermissionGroup getPermissionGroup(String groupName) {
		for(int i = 0; i<permissionGroups.size(); i++){
			PermissionGroup group = this.permissionGroups.get(i);
			if(group.getName().equals(groupName)){
				return group;
			}
		}
		return null;
	}
	
	public void setPermissionGroup(PermissionGroup pg){
		for(int i = 0; i<permissionGroups.size(); i++){
			PermissionGroup group = this.permissionGroups.get(i);
			if(group.getName().equals(pg.getName())){
				this.permissionGroups.set(i, pg);
				break;
			}			
		}		
		this.fileManager.savePermissionGroup(pg);
	}
	
	public void removePermissionGroup(PermissionGroup pg){
		for(int i = 0; i<permissionGroups.size(); i++){
			PermissionGroup group = this.permissionGroups.get(i);
			if(group.getName().equals(pg.getName())){
				this.permissionGroups.remove(i);
				this.fileManager.removePermissionGroup(pg);		
				return;
			}			
		}		
	}
	
	public void addPermission(Permission p){
		for(int i = 0; i<permissions.size(); i++){
			Permission permission = this.permissions.get(i);
			if(permission.getName().equals(p.getName())){
				return;
			}			
		}
		this.fileManager.savePermission(p);
	}
	
	public void removePermission(Permission p){
		for(int i = 0; i<permissions.size(); i++){
			Permission permission = this.permissions.get(i);
			if(permission.getName().equals(p.getName())){
				this.permissions.remove(i);
				this.fileManager.savePermission(p);				
				return;
			}			
		}
	}
	
	public void addUserPermissionGroup(String userID, PermissionGroup pg){
		ArrayList<PermissionGroup> existendPermissiongroups = this.getPermissionGroups(userID);
		if(existendPermissiongroups.contains(pg))return;
		existendPermissiongroups.add(pg);
		this.fileManager.saveUserPermissions(userID, existendPermissiongroups);
	}
	
	public void removeUserPermissionGroup(String userID, PermissionGroup pg){
		ArrayList<PermissionGroup> existendPermissiongroups = this.getPermissionGroups(userID);
		if(!existendPermissiongroups.contains(pg))return;
		existendPermissiongroups.remove(pg);
		this.fileManager.saveUserPermissions(userID, existendPermissiongroups);
	}
	
	public ArrayList<PermissionGroup> getPermissionGroups(String userID){
		if(this.userPermissionGroups.containsKey(userID)){
			return this.userPermissionGroups.get(userID);
		}
		ArrayList<PermissionGroup> userPermissionGroups = this.fileManager.readUserPermission(userID);
		this.userPermissionGroups.put(userID, userPermissionGroups);
		return userPermissionGroups;
	}
}
