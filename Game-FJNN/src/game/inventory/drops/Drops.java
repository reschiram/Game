package game.inventory.drops;

public class Drops {
	
	private Drop[] drops;
	
	public Drops(Drop... drops){
		this.drops = drops;
	}

	public Drop[] getDrops() {
		return drops;
	}

}
