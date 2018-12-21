package data;

import data.events.AccessDeniedEvent;
import data.exceptions.SecurityPackageCreationException;
import data.readableData.IntegerData;
import data.readableData.StringData;

public class SecurityPackageManager {
	
	public static final int NoPermissionExceptionInfo_MaxLength = 62;
	public final static int DataPackage_NoPermission = 10;
	
	public static void loadSecurityPackages(){
		DataPackage.setType(new PackageType(DataPackage_NoPermission, "No Permission", new IntegerData("Denied Package ID"), new StringData("No Permission Exception Info", 62)));	
	}
	
	public static Queue<DataPackage> getMessageFromDeniedSecurityPackageType(int securityPackageID, String noPermissionExceptionInfo) throws SecurityPackageCreationException{
		try {
			return DataPackage.getPackage(PackageType.readPackageData(NoPermissionExceptionInfo_MaxLength, securityPackageID, noPermissionExceptionInfo));
		} catch (Exception e) {
			throw new SecurityPackageCreationException(e, securityPackageID, noPermissionExceptionInfo);
		}
	}

	public static AccessDeniedEvent getPackageFromAccessDeniedMessage(PackageType message) {
		String noPermissionExceptionInfo = ((StringData)message.getDataStructures()[1]).getData();
		
		try{
			for(int i = ((StringData)message.getDataStructures()[1]).toData().length-1; i>=0; i--){
				if(((StringData)message.getDataStructures()[1]).toData()[i]!=0)break;
				noPermissionExceptionInfo = noPermissionExceptionInfo.substring(0, i);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return new AccessDeniedEvent(((IntegerData)message.getDataStructures()[0]).getData().intValue(), noPermissionExceptionInfo);
	}

}
