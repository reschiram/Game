package game.inventory.drops;

import Data.Location;
import game.entity.ItemEntity;
import game.map.Map;

public class Drops {
	
	private Drop[] drops;
	
	public Drops(Drop... drops){
		this.drops = drops;
	}
	
	public void drop(Location loc){
		for(int i = 0; i<this.drops.length; i++){
			Drop drop = this.drops[i];
			int amount = (int) Math.round(Math.random()*((double)(drop.getMaxAmount()-drop.getMinAmount()))) + drop.getMinAmount();
			for(int a = 0; a<amount; a++){
				new ItemEntity(drop.getType(), new Location(loc.getX()*Map.DEFAULT_SQUARESIZE, loc.getY()*Map.DEFAULT_SQUARESIZE)).show();
			}
		}
	}

}
