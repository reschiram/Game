package data;

import data.readableData.ReadableData;
import data.readableData.StringData;

public class SecurityPackageType extends PackageType{

	private static ReadableData<?>[] getDataStrucktures(ReadableData<?>[] dataStructures) {
		 StringData npei = new StringData("No Permission Exception Info", SecurityPackageManager.NoPermissionExceptionInfo_MaxLength);
		 
		ReadableData<?>[] data = new ReadableData<?>[dataStructures.length+1];
		for(int i = 0; i<dataStructures.length; i++){
			data[i] = dataStructures[i];
		}
		data[data.length-1] = npei;
		
		return data;
	}

	public SecurityPackageType(int id, String name, ReadableData<?>... dataStructures){
		super(id, name, getDataStrucktures(dataStructures));
	}
}
