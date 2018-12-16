package data.permission;

import java.util.ArrayList;

import file.ktv.KTV_File;
import filemanager.FileManager;

public class InternalFilemanager {
	
	private FileManager fileManager;
	
	private KTV_File data;
	
	private Permissiondb permissiondb;
	
	public InternalFilemanager(Permissiondb permissiondb){
		this.fileManager = new FileManager(false);	
		this.permissiondb = permissiondb;
		loadData();
	}

	private void loadData() {					
		this.data = this.fileManager.createNewKTVFile("permissiondb/data", false);
		this.fileManager.saveFile(this.data);
	}
	
	public ArrayList<PermissionGroup> readAllGroups(){
		ArrayList<PermissionGroup> groups = new ArrayList<>();
		ArrayList<String> savedGroups = this.data.get("Groups");
		if(savedGroups!=null)for(String savedGroup : savedGroups){
			ArrayList<Permission> permissions = new ArrayList<>();
			ArrayList<String> savedPermissions = this.data.get("Groups."+savedGroup+".Permissions");
			if(savedPermissions!=null)for(String savedPermission : savedPermissions){
				permissions.add(new Permission(savedPermission));
			}
			groups.add(new PermissionGroup(savedGroup, permissions));
		}
		return groups;
	}
	
	public ArrayList<PermissionGroup> readUserPermission(String userID){
		ArrayList<PermissionGroup> groups = new ArrayList<>();
		ArrayList<String> userPermissions = this.data.get("Users."+userID+".groups");
		if(userPermissions!=null)for(String userPermission : userPermissions){
			PermissionGroup group = this.permissiondb.getPermissionGroup(userPermission);
			if(group!=null)groups.add(group);
			
		}		
		return groups;
	}
	
	public ArrayList<Permission> readAllPermissions(){
		ArrayList<Permission> permissions = new ArrayList<>();
		ArrayList<String> savedPermissions = this.data.get("Permissions");
		if(savedPermissions!=null)for(String permission : savedPermissions){
			permissions.add(new Permission(permission));
			
		}		
		return permissions;
	}
	
	public void savePermissionGroup(PermissionGroup group){
		this.data.set("Groups."+group.getName());
		for(Permission permission: group.getPermissions()){
			this.data.add("Groups."+group.getName()+".Permissions", permission.getName());	
		}
		this.fileManager.saveFile(this.data);
	}
	
	public void removePermissionGroup(PermissionGroup group){
		this.data.remove("Groups."+group.getName());
		this.fileManager.saveFile(this.data);
	}
	
	public void saveUserPermissions(String userID, ArrayList<PermissionGroup> permissionGroups){
		this.data.set("Users."+userID);		
		for(PermissionGroup group: permissionGroups){
			this.data.add("Users."+userID+".Permissions", group.getName());
		}
		this.fileManager.saveFile(this.data);
	}
	
	public void savePermission(Permission permission){
		this.data.add("Permissions", permission.getName());		
		this.fileManager.saveFile(this.data);
	}
	
	public void removePermission(Permission permission){
		this.data.remove("Permissions", permission.getName());		
		this.fileManager.saveFile(this.data);
	}

}
