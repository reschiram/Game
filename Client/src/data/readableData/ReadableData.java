package data.readableData;

public abstract class ReadableData<ContentType>{

	private String name;
	
	protected ContentType data;
	protected int byteLength;
	
	public ReadableData(String name, int byteLength){
		this.name = name;
		this.byteLength = byteLength;
	}
	
	public ContentType getData(){
		return data;
	}

	public int getByteLength() {
		return byteLength;
	}
	
	public abstract void readData(byte[] data) throws Exception;
	
	public abstract void readString(String data) throws Exception;
	
	public abstract byte[] toData() throws Exception;
	
	public abstract ReadableData<ContentType> clone();

	public String getName() {
		return name;
	}
}
