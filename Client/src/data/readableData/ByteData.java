package data.readableData;

import java.util.Arrays;

public class ByteData extends ReadableData<byte[]>{

	public ByteData(String name, int byteLength) {
		super(name, byteLength);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		this.data = data;
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (byte[]) data;
	}

	@Override
	public void readString(String data) throws Exception {
		data = data.substring(1, data.length()-1);
		String[] bytesString = data.split(" , ");
		this.data = new byte[bytesString.length];
		for(int i = 0; i < bytesString.length; i++) {
			this.data[i] = Byte.parseByte(bytesString[i]);
		}
	}

	@Override
	public byte[] toData() throws Exception {
		return this.data;
	}

	@Override
	public String toString() {
		String msg = "{";
		for(int i = 0; i < this.data.length; i++) {
			msg += this.data[i]+" , ";
		}
		msg = msg.substring(0, msg.length()-3) + "}";
		return msg;
	}

	@Override
	public ReadableData<byte[]> clone() {
		ByteData newData = new ByteData(this.getName(), this.byteLength);
		if(this.data != null) newData.data = Arrays.copyOf(this.data, this.data.length);
		return newData;
	}

}
