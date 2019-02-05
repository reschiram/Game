package game.inventory;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import game.entity.Entity;
import game.entity.ItemEntity;
import game.entity.manager.EntityManager;
import game.entity.type.EntityType;
import game.inventory.items.Item;
import game.map.Map;

public class ItemCollector {
	
	private static int COLLECTRANGE = Map.DEFAULT_SQUARESIZE;
	
	private Dimension collectRange;
	private Point locationChange;
	
	private Entity entity;
	private Inventory inventoy;
	
	private long lastCollected = System.currentTimeMillis();
	private ArrayList<ItemEntity> collectable = new ArrayList<>();
	
	public ItemCollector(Entity entity, Inventory inventory, double range){
		this.entity = entity;
		this.inventoy = inventory;
		
		this.collectRange = new Dimension((int)(entity.getWidth()*range), (int) (entity.getHeight()*range));
		this.locationChange = new Point((int) Math.abs((collectRange.getWidth()-entity.getWidth())/2), (int) Math.abs((collectRange.getHeight()-entity.getHeight())/2));
	}
	
	public void tick(){
		collect();
	}

	private void collect() {

		for(ItemEntity entity:collectable){				
			int itemX   =      entity.getX()+     entity.getWidth()/2;
			int entityX = this.entity.getX()+this.entity.getWidth()/2;
			if(entityX-itemX<-1920)entityX+=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
			else if(entityX-itemX>1920)itemX+=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
			
			int itemY   = 	   entity.getY()+  	   entity.getHeight()/2;
			int entityY = this.entity.getY()+ this.entity.getHeight()/2;
			
			if((itemX-entityX)*(itemX-entityX) + (itemY-entityY)*(itemY-entityY)<=COLLECTRANGE*COLLECTRANGE){
				entity.destroy(true);
				inventoy.addItem(new Item(((ItemEntity)entity).getItemType()));
				collectable.remove(entity);
				return;
			}
			
			if(itemX>entityX){
				entity.move(Direction.LEFT);
			}else entity.move(Direction.RIGHT);
			if(itemY>entityY)entity.move(Direction.UP);
		}
		
		if(System.currentTimeMillis()-lastCollected<1000)return;
		lastCollected = System.currentTimeMillis();
		
		ArrayList<Entity> itemEntitys = EntityManager.getEntityManager().getEntityPixelLocation(EntityType.ItemEntity, 
				new Location(this.entity.getLocation().getX()-(int)locationChange.getX(), this.entity.getLocation().getY()-(int)locationChange.getY()), this.collectRange);
		for(Entity entity: itemEntitys){
			if(this.inventoy.canAdd(((ItemEntity)entity).getItemType())){
				collectable.add((ItemEntity)entity);
			}
		}
	}

	public Inventory getInventory() {
		return inventoy;
	}

}
