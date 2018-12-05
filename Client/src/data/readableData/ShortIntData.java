package data.readableData;

import data.ShortInt;

public class ShortIntData extends ReadableData<ShortInt>{

	public ShortIntData(String name) {
		super(name, 1);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		this.data = new ShortInt(data[0]);		
	}

	@Override
	public void readString(String data) throws Exception {
		this.data = new ShortInt(Integer.parseInt(data));		
	}

	@Override
	public void readData(Object data) throws Exception {
		this.data = (ShortInt) data;
	}

	@Override
	public byte[] toData() throws Exception {
		return new byte[]{this.data.getByte()};
	}

	@Override
	public ReadableData<ShortInt> clone() {
		ShortIntData newData = new ShortIntData(this.getName());
		if(this.data!=null)newData.data = this.data.clone();
		return newData;
	}
	
	@Override
	public String toString() {
		if(this.data == null)return "0";
		return this.data.toString();
	}

}
