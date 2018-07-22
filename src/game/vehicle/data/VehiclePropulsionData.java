package game.vehicle.data;

import game.vehicle.data.propulsion.RocketPropulsionData;

public class VehiclePropulsionData extends VehicleData{
	
public static VehiclePropulsionData ROCKET;
	
	private static VehiclePropulsionData[] data;
	
	public static VehiclePropulsionData[] getVehiclePropulsionDatas(){
		return data;
	}
	
	static void createData(){
		ROCKET = new RocketPropulsionData();
		
		data = new VehiclePropulsionData[1];
		data[0] = ROCKET;
	}
	
	protected int speed;
	
	public VehiclePropulsionData(int HP, String Name, int speed){
		super(HP, Name);
		this.speed = speed;
	}
	
	public int getSpeed(){
		return this.speed;
	}

}
