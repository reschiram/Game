package game.vehicle.data;

import game.vehicle.data.weapon.LaserWeaponData;

public class VehicleWeaponData extends VehicleData{
	
	public static VehicleWeaponData LASER;
	
	private static VehicleWeaponData[] data;
	
	public static VehicleWeaponData[] getVehicleWeaponDatas(){
		return data;
	}
	
	static void createData(){
		LASER = new LaserWeaponData();

		data = new VehicleWeaponData[1];
		data[0] = LASER;
	}
	
	protected int damage;
	protected int speed;
	
	public VehicleWeaponData(int HP, String Name, int damage, int speed){
		super(HP, Name);
		this.damage = damage;
		this.speed = speed;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public int getSpeed(){
		return this.speed;
	}

}
