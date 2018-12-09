package data.exceptions;

public class UnsupportedPackageCreationException extends CustomException{
	
	private static final long serialVersionUID = 1L;

	private static String getMessage(int packageID, Object[] data) {
		String message = "Unsupported Package-ID: "+packageID+" or unsupported content: ";
		for(int i = 0; i<data.length; i++)message+="["+data.getClass().getName()+" : "+data.toString()+"] ";
		return message;
	}

	private int packageID;
	private Object[] data;

	public UnsupportedPackageCreationException(Exception exception, int packageID, String... data) {
		super(exception, getMessage(packageID, data));
		this.packageID = packageID;
		this.data = data;		
	}

	public int getPackageID() {
		return packageID;
	}

	public Object[] getData() {
		return data;
	}

}
