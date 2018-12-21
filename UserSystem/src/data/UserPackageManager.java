package data;

import data.events.ClientLoginEvent;
import data.readableData.EmptyData;
import data.readableData.ShortIntData;
import data.readableData.StringData;
import data.user.User;
import data.user.UserService;

public class UserPackageManager {
	
	public static final int DataPackage_UserLogin = 4;
	public static final int DataPackage_UserLoginConfirm = 6;
	public static final int DataPackage_UserLogout = 8;

	public static void loadPackageTypes(){		
		DataPackage.setType(new PackageType(DataPackage_UserLogin, "UserLogin", new StringData("LoginInfo", 121, UserService.StringFormat)));	
		DataPackage.setType(new PackageType(DataPackage_UserLoginConfirm, "UserLoginConfirm", new ShortIntData("UserLoginConfirmStatus(0:ok, 1:rejected)"), new StringData("UserID", User.ID_Length, UserService.StringFormat)));
		DataPackage.setType(new PackageType(DataPackage_UserLogout, "UserLogout", new EmptyData("empty")));		
	}
	
	public static ClientLoginEvent getLoginEventFromData(PackageType data, String username){
		int status = ((ShortIntData)data.getDataStructures()[0]).getData().getInt();
		if(status == 0){
			String userID = ((StringData)data.getDataStructures()[1]).getData();
			return new ClientLoginEvent(true, new User(userID, username, "**********"));
		}else{
			return new ClientLoginEvent(false, new User("", username, "**********"));
		}
	}

	public static String getUserInfoFromData(PackageType data) {
		String loginInfo = ((StringData)data.getDataStructures()[0]).getData();
		return loginInfo;
	}

}
