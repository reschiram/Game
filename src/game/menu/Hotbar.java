package game.menu;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Data.Hitbox;
import Data.Location;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import Engine.Engine;
import data.Resource;
import game.map.Map;
import sprites.Sprites;

public class Hotbar{
	
	private Resource[] inv;
	private Image[] ims;
	private Image[] backs;
	
	private int state = -1;
	private Hitbox hb;
	
	public Hotbar(int size, Location l, int layer){
		this.inv = new Resource[size];
		this.ims = new Image[size];
		this.backs = new Image[size];
		hb = new Hitbox(l.getX(), l.getY(), (Map.DEFAULT_SQUARESIZE+20)*9, Map.DEFAULT_SQUARESIZE);
		for(int i = 0; i< size; i++){
			this.backs[i] = new Image(new Location(l.getX()+(Map.DEFAULT_SQUARESIZE+20)*i, l.getY()), new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE), "", Sprites.Slot.getSpriteSheet(), null);
			this.backs[i].disabled = true;
			Engine.getEngine(this, this.getClass()).addImage(this.backs[i], layer);
			this.ims[i] = new Image(new Location(l.getX()+(Map.DEFAULT_SQUARESIZE+20)*i+5, l.getY()+5), new Dimension(Map.DEFAULT_SQUARESIZE-10, Map.DEFAULT_SQUARESIZE-10), "", new SpriteSheet(), null);
			this.ims[i].disabled = true;
			Engine.getEngine(this, this.getClass()).addImage(this.ims[i], layer);
		}
	}	
	
	public void addRecources(Resource... resources){
		for(int i = 0; i< inv.length; i++){
			if(inv[i]==null){
				for(int r=0; r<resources.length; r++){
					if(resources[r]!=null){
						inv[i] = resources[r];
						resources[r] = null;
						r=resources.length;
						ims[i].setSpriteSheet(inv[i].getSprites());
						ims[i].setSpriteState(inv[i].getSpriteIDs()[0]);
					}
				}
			}
		}
	}
	
	public void setRecource(Resource res, int id){
		inv[id] = res;
		ims[id].setSpriteSheet(inv[id].getSprites());
		ims[id].setSpriteState(inv[id].getSpriteIDs()[0]);
	}
	
	public void show(){
		if(ims[0].disabled){
			for(int i = 0; i< ims.length; i++){
				backs[i].disabled = false;
				ims[i].disabled = false;
			}
		}
	}
	
	public void hide(){
		if(!ims[0].disabled){
			for(int i = 0; i< ims.length; i++){
				backs[i].disabled = true;
				ims[i].disabled = true;
			}
		}
	}

	public void tick() {
		if(!backs[0].disabled){
			if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_1))state = 0;
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_2))state = 1; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_3))state = 2; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_4))state = 3; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_5))state = 4; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_6))state = 5; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_7))state = 6; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_8))state = 7; 
			else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_9))state = 8; 
			else if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1) && contains(Engine.getInputManager().MousePosition())){
				for(int i = 0; i<backs.length; i++){
					if(backs[i].getHitbox().contains(Engine.getInputManager().MousePosition())){
						state = i;
						return;
					}
				}
				state = -1;
			}
		}
	}

	public Resource getSlected() {
		if(state==-1)return null;
		return inv[state];
	}
	
	public int getState() {
		return state;
	}

	public boolean contains(Point p) {
		return hb.contains(p);
	}

	public void setSelected(int selected) {
		this.state = selected;
	}

	public void clear() {
		for(int i = 0; i<inv.length; i++){
			inv[i]=null;
			ims[i].setSpriteSheet(new SpriteSheet());
		}
	}
}
