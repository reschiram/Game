package data.readableData;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DoubleData extends ReadableData<Double> {

	public DoubleData(String name) {
		super(name, 8);
	}

	@Override
	public void readData(byte[] data) throws UnsupportedEncodingException {
		this.data = ByteBuffer.wrap(data).getDouble();
	}

	@Override
	public void readString(String data) throws Exception {
		this.data = Double.parseDouble(data);
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (Double) data;
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength]; 
		ByteBuffer.wrap(data).putDouble(this.data);
		return data;
	}

	@Override
	public ReadableData<Double> clone() {
		DoubleData newData = new DoubleData(this.getName());
		if(this.data!=null)newData.data = this.data.doubleValue();
		return newData;
	}
	
	@Override
	public String toString() {
		if(this.data == null)return "0.0";
		return this.data.doubleValue()+"";
	}
}
