package game.vehicle.data;

public abstract class VehicleData {
	
	private static VehicleData[] data;
	
	public static VehicleData[] getVehicleDatas(){
		return data;
	}
	
	public static void Load(){
		VehicleArmorData		.createData();
		VehicleWeaponData		.createData();
		VehiclePropulsionData	.createData();
		
		VehicleData[] armor 		= VehicleArmorData		.getVehicleArmorDatas()		;
		VehicleData[] weapons		= VehicleWeaponData		.getVehicleWeaponDatas()	;
		VehicleData[] propulsions	= VehiclePropulsionData	.getVehiclePropulsionDatas();
		
		data = new VehicleData[armor.length+weapons.length+propulsions.length];
		
		for(int i = 0; i<armor		.length; i++)data[i									] = armor		[i];
		for(int i = 0; i<weapons	.length; i++)data[i + armor.length					] = weapons		[i];
		for(int i = 0; i<propulsions.length; i++)data[i + armor.length + weapons.length	] = propulsions [i]; 
	}
	
	protected int HP;
	protected String Name;
	
	public VehicleData(int HP, String Name){
		this.HP = HP;
		this.Name = Name;
	}
	
	public int getHP(){
		return HP;
	}
	
	public String getName(){
		return this.Name;
	}

}
