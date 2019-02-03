package data.readableData;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class StringData extends ReadableData<String>{

	public static final String Default_Format = "ISO_8859_1";
	
	protected String format;
	
	public StringData(String name, int charAmount) {
		super(name, charAmount);
		this.format = Default_Format;
	}
	
	public StringData(String name, int byteLength, String format) {
		super(name, byteLength);
		this.format = format;
	}
	
	@Override
	public void readData(byte[] data) throws UnsupportedEncodingException {
		this.data = new String(data, this.format);
	}

	@Override
	public void readString(String data) {
		this.data = data+"";
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (String) data;
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength]; 
		ByteBuffer.wrap(data).put(this.data.getBytes(this.format));
		return data;
	}

	@Override
	public ReadableData<String> clone() {
		StringData newData = new StringData(this.getName()+"", this.byteLength, this.format);
		if(this.data!=null)newData.data = this.data+"";
		return newData;
	}

	@Override
	public String toString() {
		if(this.data == null)return "";
		else return this.data;
	}

}
