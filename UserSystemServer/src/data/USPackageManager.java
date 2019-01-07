package data;

import data.readableData.LongData;
import data.readableData.StringData;
import data.user.User;
import data.user.UserService;
import data.user.ValidatedUser;

public class USPackageManager {
	
	public static final int DataPackage_RequestUserInfos = 6; 
	public static final int DataPackage_UserResponse = 8; 

	public static void loadPackageTypes() {
		DataPackage.setType(new PackageType(DataPackage_RequestUserInfos, "RequestUserInfos",
				new StringData("LoginInfo", 121, UserService.StringFormat),
				new LongData("Time"),
				new LongData("Client ID")));	
		DataPackage.setType(new PackageType(DataPackage_UserResponse, "UserResponse",
				new StringData("UserName", User.maxUsernameLength, UserService.StringFormat),
				new StringData("userID", User.ID_Length, UserService.StringFormat),
				new LongData("Client ID")));	
	}

	public static Queue<DataPackage> createUserRequestPackage(String encodedUserInfo, long clientID) {
		try {
			return DataPackage.getPackage(PackageType.readPackageData(DataPackage_RequestUserInfos, encodedUserInfo, System.currentTimeMillis(), clientID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ValidatedUser readUserResponse(PackageType message) {
		String userName = ((StringData)message.getDataStructures()[0]).getData();
		String userID = ((StringData)message.getDataStructures()[1]).getData();
		long clientID = ((LongData)message.getDataStructures()[2]).getData();
		
		return new ValidatedUser(clientID, userID, userName);
	}
}
