package data;

import galaxy.*;

public class WorldLocation {
	
	public Planet planet;
	public SolarSystem solarSystem;
	
	public WorldLocation(Planet planet, SolarSystem solarSystem){
		this.planet = planet;
		this.solarSystem = solarSystem;
	}

}
