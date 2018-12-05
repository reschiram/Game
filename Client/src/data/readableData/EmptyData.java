package data.readableData;

public class EmptyData extends ReadableData<String>{

	public EmptyData(String name) {
		super(name, 1);
	}

	@Override
	public void readData(byte[] data) throws Exception {}

	@Override
	public void readData(Object data) throws Exception {}

	@Override
	public void readString(String data) throws Exception {}

	@Override
	public byte[] toData() throws Exception {
		return new byte[1];
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public ReadableData<String> clone() {
		return new EmptyData(this.getName());
	}

}
