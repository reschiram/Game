package game.entity.type.data;

public class LightEntityData extends EntityData{

	private int lightDistance;
	private double lightStrength;
	
	public LightEntityData(int lightDistance, double lightStrength) {
		super();
		this.lightDistance = lightDistance;
		this.lightStrength = lightStrength;
	}

	public int getLightDistance() {
		return lightDistance;
	}

	public double getLightStrength() {
		return lightStrength;
	}

}
