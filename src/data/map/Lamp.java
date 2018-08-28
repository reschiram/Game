package data.map;

public class Lamp extends BlockData{
	
	public static int DEFAULT_LIGHT_STATES = 40;
	public static int DEFAULT_SURFACE_LEVELS = 3;
	
	private int lightDistance;
	private double lightStrength;
	
	public Lamp(String name, int lightDistance, double lightStrength) {
		super(name);
		this.lightDistance = lightDistance;
		this.lightStrength = lightStrength;
	}
	
	public int getLightDistance(){
		return lightDistance;
	}
	
	public double getLightStrength(){
		return lightStrength;
	}
}
