package data.exceptions;

public class UnsupportedPackageException extends CustomException {

	private static final long serialVersionUID = 1L;
	
	private int packageID;
	private byte[] data;

	public UnsupportedPackageException(Exception exception, int packageID, byte[] data) {
		super(exception, "Unsupported Package-ID: "+packageID+" or data Error.");
		this.packageID = packageID;
		this.data = data;
	}

	public int getPackageID() {
		return packageID;
	}

	public byte[] getData() {
		return data;
	}

}
