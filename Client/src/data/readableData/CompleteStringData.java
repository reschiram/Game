package data.readableData;

import java.io.UnsupportedEncodingException;

public class CompleteStringData extends StringData{
	
	public CompleteStringData(String name, int charAmount) {
		super(name, charAmount);
	}

	public CompleteStringData(String name, int byteLength, String format) {
		super(name, byteLength, format);
	}

	@Override
	public void readData(byte[] data) throws UnsupportedEncodingException {
		this.data = new String(data, this.format);
		while(this.data.length() > 0 && this.data.charAt(this.data.length()-1) == (char)0) {
			this.data = this.data.substring(0, this.data.length()-1);
		}
	}

	@Override
	public ReadableData<String> clone() {
		CompleteStringData newData = new CompleteStringData(this.getName()+"", this.byteLength, this.format);
		if(this.data!=null)newData.data = this.data+"";
		return newData;
	}

}
