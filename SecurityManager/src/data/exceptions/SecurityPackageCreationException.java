package data.exceptions;

public class SecurityPackageCreationException extends CustomException{

	private static final long serialVersionUID = 1L;

	private int packageID;
	private String noPermissionExceptionInfo;

	public SecurityPackageCreationException(Exception exception, int packageID, String noPermissionExceptionInfo) {
		super(exception, "Error while creating answer for package: "+packageID+" with  no permission exception information: "+noPermissionExceptionInfo);
		this.packageID = packageID;
		this.noPermissionExceptionInfo = noPermissionExceptionInfo;
	}
	
	public int getPackageID() {
		return packageID;
	}

	public String getNoPermissionExceptionInfo() {
		return noPermissionExceptionInfo;
	}

}
