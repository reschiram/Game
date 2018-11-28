package game.map;

import java.util.Random;

import Data.Location;

public class CaveGenerator {
	
	private Location center;
	
	private int ex;
	private int ey;
	
	private double maxDistance;
	
	public CaveGenerator(Location center, int ex, int ey){
		this.center = center;
		this.ex = ex;
		this.ey = ey;
		
		this.maxDistance = 2.0*(Math.sqrt((ex*ex)+(ey*ey))+Math.sqrt(1*1+1*1));
	}

	public boolean contains(int px, int py, Random rnd) {
		int x = px-this.center.getX();
		int y = py-this.center.getY();
	
		double pf1 = Math.pow((x+ex), 2)+Math.pow(y+ey, 2);
		double pf2 = Math.pow((x-ex), 2)+Math.pow(y-ey, 2);
		
//		if(this.center.distance_Math(new Location(px, py))<=maxDistance/2)System.out.println(x+"|"+y+"->"+ex+"|"+ey+"->"+Math.sqrt(pf1)+" + "+Math.sqrt(pf2)+" = "+(Math.sqrt(pf1)+Math.sqrt(pf2))+" <= "+maxDistance);
		
		return Math.sqrt(pf1)+Math.sqrt(pf2)<=maxDistance;
	}

}
