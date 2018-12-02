package data.readableData;

import java.nio.ByteBuffer;

public class IntegerData extends ReadableData<Integer>{

	public IntegerData(String name) {
		super(name, 4);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		this.data = ByteBuffer.wrap(data).getInt();
	}

	@Override
	public void readString(String data) throws Exception{
		this.data = Integer.parseInt(data);
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (Integer) data;
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength]; 
		ByteBuffer.wrap(data).putInt(this.data);
		return data;
	}

	@Override
	public ReadableData<Integer> clone() {
		IntegerData newData = new IntegerData(this.getName());
		if(this.data!=null)newData.data = this.data.intValue();
		return newData;
	}

}
