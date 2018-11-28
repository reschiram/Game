package game.vehicle.data;

import game.vehicle.data.armor.AdvancedArmorData;
import game.vehicle.data.armor.BasicArmorData;
import game.vehicle.data.armor.EpicArmorData;
import game.vehicle.data.armor.SuperiorArmorData;

public class VehicleArmorData extends VehicleData{
	
	public static VehicleArmorData EPICARMOR;
	public static VehicleArmorData SUPERIORARMOR;
	public static VehicleArmorData ADVANCEDARMOR;
	public static VehicleArmorData BASICARMOR;
	
	private static VehicleArmorData[] data;
	
	public static VehicleArmorData[] getVehicleArmorDatas(){
		return data;
	}
	
	static void createData(){
		BASICARMOR 		= new BasicArmorData();
		ADVANCEDARMOR 	= new AdvancedArmorData();
		SUPERIORARMOR 	= new SuperiorArmorData();
		EPICARMOR		= new EpicArmorData();

		data = new VehicleArmorData[4];
		data[0] = BASICARMOR;
		data[1] = ADVANCEDARMOR;
		data[2] = SUPERIORARMOR;
		data[3] = EPICARMOR;
	}
	
	public VehicleArmorData(int HP, String Name){
		super(HP, Name);
	}

}
