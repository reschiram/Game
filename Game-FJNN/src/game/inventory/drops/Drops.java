package game.inventory.drops;

import Data.Location;
import game.entity.requester.EntityRequester;

public class Drops {
	
	private Drop[] drops;
	
	public Drops(Drop... drops){
		this.drops = drops;
	}
	
	public void drop(Location blockLoc){
		for(int i = 0; i<this.drops.length; i++){
			Drop drop = this.drops[i];
			int amount = (int) Math.round(Math.random()*((double)(drop.getMaxAmount()-drop.getMinAmount()))) + drop.getMinAmount();
			for(int a = 0; a<amount; a++){
				EntityRequester.getEntityRequester().requestItemEntity(drop.getType(), blockLoc);
			}
		}
	}

}
