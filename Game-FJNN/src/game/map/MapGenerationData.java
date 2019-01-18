package game.map;

public class MapGenerationData {

	private int[][][] groundData;
	private int[][][] buildData;
	private int seed;

	public MapGenerationData(int[][][] groundData, int[][][] buildData, int seed) {
		this.groundData = groundData;
		this.buildData = buildData;
		this.seed = seed;
	}
	
	public int[][][] getGroundData() {
		return groundData;
	}
	
	public int[][][] getBuildData() {
		return buildData;
	}
	
	public int getSeed() {
		return seed;
	}

	public int getWidth() {
		return groundData.length;
	}

	public int getHeight() {
		return groundData[0].length;
	}

}
