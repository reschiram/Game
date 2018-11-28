package data.readableData;

import java.nio.ByteBuffer;

public class LongData extends ReadableData<Long>{

	public LongData(String name) {
		super(name, 8);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		this.data = ByteBuffer.wrap(data).getLong();
	}

	@Override
	public void readString(String data) throws Exception{
		this.data = Long.parseLong(data);
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength]; 
		ByteBuffer.wrap(data).putLong(this.data);
		return data;
	}

	@Override
	public ReadableData<Long> clone() {
		LongData newData = new LongData(this.getName());
		if(this.data!=null)newData.data = this.data.longValue();
		return newData;
	}

}
