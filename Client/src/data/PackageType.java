package data;

import java.nio.ByteBuffer;
import java.util.Arrays;

import data.readableData.ReadableData;

public class PackageType {
	
	public static PackageType readPackageData(int type, String... data) throws Exception{
		PackageType packageType = DataPackage.getPackageType(type).newData();
		return packageType.read(data);
	}

	public static PackageType readPackageData(int id, byte[] data) throws Exception{
		PackageType packageType = DataPackage.getPackageType(id).newData();
		return packageType.read(data);
	}

	public static PackageType readPackageData(int id, Object... data) throws Exception {
		PackageType packageType = DataPackage.getPackageType(id).newData();
		return packageType.read(data);
	}

	private int id;
	private String name;
	private ReadableData<?>[] dataStructures;	
	
	private int maxByteLength = 0;
	
	public PackageType(int id, String name, ReadableData<?>... dataStructures) {
		this.id = id;
		this.name = name;
		this.dataStructures = dataStructures;
		
		for(ReadableData<?> readData: this.dataStructures)this.maxByteLength+=readData.getByteLength();
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public ReadableData<?>[] getDataStructures() {
		return dataStructures;
	}

	public PackageType newData() {
		ReadableData<?>[] dataStructures = new ReadableData<?>[this.dataStructures.length];
		for(int i = 0; i<dataStructures.length; i++){
			dataStructures[i] = this.dataStructures[i].clone();
		}
		return new PackageType(this.id, this.name+"", dataStructures);
	}

	private PackageType read(Object[] data) throws Exception {
		int i = 0;
		for(ReadableData<?> readData: this.dataStructures){
			readData.readData(data[i]);
			i++;
		}
		return this;
	}

	public PackageType read(byte[] data) throws Exception {
		int last = 0;
		for(ReadableData<?> readData: this.dataStructures){
			readData.readData(Arrays.copyOfRange(data, last, last+readData.getByteLength()));
			last+=readData.getByteLength();
		}
		return this;
	}	
	
	private PackageType read(String[] data) throws Exception {
		int i = 0;
		for(ReadableData<?> readData: this.dataStructures){
			readData.readString(data[i]);
			i++;
		}
		return this;
	}
	
	public byte[] getData() throws Exception{
		ByteBuffer buffer = ByteBuffer.allocate(this.maxByteLength);
		for(ReadableData<?> data: dataStructures){
			buffer.put(data.toData());
		}
		return buffer.array();
	}
	
	public int getMaxByteLength(){
		return this.maxByteLength;
	}

}
