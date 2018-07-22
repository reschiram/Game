package game.inventory.menu;

import java.awt.Color;
import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import Engine.Engine;
import Data.GraphicOperation.StringRenderOperation;
import game.inventory.items.Item;
import sprites.Sprites;

public class ItemImage {
	
	public static int DEFAULT_SLOTSIZE = 60;
	private static SpriteSheet EMPTYSLOT = new SpriteSheet(Image.createEmptyImage(DEFAULT_SLOTSIZE, DEFAULT_SLOTSIZE));
	
	private Image slotVisual;
	private Image itemVisual;
	private StringRenderOperation amountVisual;
	private int layer;
	
	private Item item;

	public ItemImage(Item item, int layer, Location location){
		this.item = item;
		this.layer = layer;
		
		this.slotVisual = new Image(location, new Dimension(DEFAULT_SLOTSIZE, DEFAULT_SLOTSIZE), "", Sprites.Slot.getSpriteSheet(), null);
		
		this.itemVisual = new Image(new Location(location.x+2, location.y+2), new Dimension(DEFAULT_SLOTSIZE-4, DEFAULT_SLOTSIZE-4), "", EMPTYSLOT, null);
		if(item!=null){
			this.itemVisual.setSpriteSheet(item.getItemType().getSpriteSheet());
			this.itemVisual.setSpriteState(item.getItemType().getSpriteIds()[0]);
		}
		
		this.amountVisual = new StringRenderOperation(new Location(location.x+20, location.y+20), new Dimension(20, 20), "", null, Color.GRAY);
		update();
	}
	
	public ItemImage createVisual(){
		Engine engine = Engine.getEngine(this, this.getClass());
		
		engine.addImage(slotVisual, layer);
		engine.addImage(itemVisual, layer);
		engine.addGraphicOperation(amountVisual, layer);
		
		return this;
	}
	
	public void show(){
		this.slotVisual  .disabled = false;
		this.itemVisual  .disabled = false;
		this.amountVisual.disabled = false;
	}
	
	public void hide(){
		this.slotVisual  .disabled = true;
		this.itemVisual  .disabled = true;
		this.amountVisual.disabled = true;
	}
	
	public void destroyVisual(){
		Engine engine = Engine.getEngine(this, this.getClass());
		
		engine.removeImage(layer ,slotVisual);
		engine.removeImage(layer ,itemVisual);
		engine.removeGraphicOperation(amountVisual, layer);
		
	}
	
	public Item getItem(){
		return item;
	}
	
	public boolean isClicked(){
		return this.slotVisual.getHitbox().contains(Engine.getInputManager().MousePosition());
	}
	
	public void update(){
		if(item!=null){
			this.amountVisual.setText(this.item.getAmount()+"");
		}else{
			this.amountVisual.setText("");
		}
	}
	
	public void setItem(Item item){
		
		if(this.item != null && item == null){
			this.itemVisual.setSpriteState(0);
			this.itemVisual.setSpriteSheet(EMPTYSLOT);
		}
		
		this.item = item;
		
		if(item!=null){
			this.itemVisual.setSpriteSheet(item.getItemType().getSpriteSheet());
			this.itemVisual.setSpriteState(item.getItemType().getSpriteIds()[0]);
		}
		
		update();
	}
	
}
