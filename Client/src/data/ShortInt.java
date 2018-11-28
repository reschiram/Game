package data;

import java.math.BigInteger;

public class ShortInt {
	
	private byte data;

	public ShortInt(int data){
		setInt(data);
	}
	
	public ShortInt(byte data){
		setData(data);
	}
	public int getInt(){
		return (int)data;
	}
	
	public byte getByte(){
		return data;
	}
	
	public void setInt(int i){
		this.data = new BigInteger(""+i).toByteArray()[0];
	}
	
	private void setData(byte data) {
		this.data = data;
	}
	
	@Override
	public ShortInt clone(){
		return new ShortInt(data);
	}
	
	@Override
	public String toString(){
		return this.data+"";
	}

}
