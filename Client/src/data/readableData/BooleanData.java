package data.readableData;

public class BooleanData extends ReadableData<Boolean>{

	public BooleanData(String name) {
		super(name, 1);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		this.data = (data[0] == 1);
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (boolean) data;		
	}

	@Override
	public void readString(String data) throws Exception {
		if(data.equalsIgnoreCase("true")) this.data = true;
		else if(data.equalsIgnoreCase("false")) this.data = true;
		else throw new Exception("Invalid String: " + data);
	}

	@Override
	public byte[] toData() throws Exception {
		if(this.data) return new byte[]{1};
		else return new byte[]{0};
	}

	@Override
	public String toString() {
		if(this.data) return "true";
		else return "false";
	}

	@Override
	public ReadableData<Boolean> clone() {
		BooleanData newData = new BooleanData(this.getName());
		if(this.data != null) newData.data = (this.data == true);
		return newData;
	}

}
