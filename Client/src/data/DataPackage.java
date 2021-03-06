package data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import data.readableData.EmptyData;
import data.readableData.LongData;
import data.readableData.ShortIntData;
import data.readableData.StringData;

public class DataPackage {

	public static final int PACKAGESIZE = 8192;
	public static final int MAXPACKAGELENGTH = PACKAGESIZE/8;
	public static final int ID_Length = 1;
	
	
	public static final int PackageType_InitConnection = 0;
	public static final int PackageType_Heartbeat = 2;
	public static final int PackageType_Kick = 4;
	
	public static void loadInternalPackageIds(){
		setType(new PackageType(PackageType_InitConnection, "InitConnection", new LongData("Connection-ID")));
		setType(new PackageType(PackageType_Heartbeat, "Heartbeat", new EmptyData("Empty")));
		setType(new PackageType(PackageType_Kick, "Kick", new StringData("Reason", MAXPACKAGELENGTH-1)));
	}
	
	private static final ArrayList<PackageType> PackageTypes = new ArrayList<>();
	
	public static void setType(PackageType type){
		for(int i = 0; i<PackageTypes.size(); i++){
			PackageType foundType = PackageTypes.get(i);
			if(foundType.getId() == type.getId()){
				PackageTypes.set(i, type);
				return;
			}
		}
		PackageTypes.add(type);
	}
	
	public static PackageType getPackageType(int id){
		for(int i = 0; i<PackageTypes.size(); i++){
			PackageType foundType = PackageTypes.get(i);
			if(foundType.getId() == id){
				return foundType;
			}
		}
		return null;
	}
	
	public static ArrayList<PackageType> getPackageTypes() {
		return PackageTypes;
	}
	
	public static Queue<DataPackage> getPackage(PackageType packageType) throws Exception{
		Queue<DataPackage> packages = new Queue<>();
		double maxDataLength = MAXPACKAGELENGTH-1.0;
		int id = packageType.getId();
		
		byte[] data = packageType.getData();
		
		for(int i = 0; Math.ceil((double)i/maxDataLength)*maxDataLength<data.length; i+=maxDataLength){
			int length = (int) maxDataLength+i;
			if(length>=data.length){
				length = data.length;
				id++;
			}
			DataPackage dataPackage = new DataPackage(new ShortInt(id), Arrays.copyOfRange(data, i, length));
			packages.add(dataPackage);
		}
		return packages;
	}
	
	private ShortInt id;
	private byte[] data;
	
	public DataPackage(ShortInt id, byte[] data) {
		this.id = id;
		this.data = ByteBuffer.allocate(DataPackage.MAXPACKAGELENGTH).put(id.getByte()).put(data).array();
	}

	public DataPackage(byte[] data, int length) throws Exception {
		ShortIntData idData = new ShortIntData("ID");
		idData.readData(Arrays.copyOf(data, idData.getByteLength()));
		this.id = idData.getData();
		this.data = data;
	}

	public byte[] getByteData() {
		return data;
	}

	public int getId() {
		if(isEnd()) return id.getInt()-1;
		return id.getInt();
	}

	public boolean isEnd() {
		return ((int)(this.id.getInt()/2))*2 != this.id.getInt();
	}

	public ShortInt getShortId() {
		return id;
	}

}
