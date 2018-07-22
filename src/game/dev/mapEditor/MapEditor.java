package game.dev.mapEditor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import Data.Location;
import Data.Events.Action;
import Data.Image.Image;
import Engine.Engine;
import data.Mouse;
import data.MapResource;
import game.map.Map;
import game.map.MapLoader;
import game.menu.Grid;
import game.menu.Hotbar;
import game.menu.Menu;
import sprites.Sprites;

public class MapEditor {

	private MapLoader mapLoader;
	private Map map;
	private Menu menu;
	private Hotbar hotbar;
	private Grid grid;
	
	private MapResource selectedRes;
	
	private boolean build = false;
	private int LayerUp = 0;
	private long lastSaved = System.currentTimeMillis();
	private long lastGridToggle = System.currentTimeMillis();
	
	public MapEditor(MapLoader mapLoader){
		this.mapLoader = mapLoader;
		this.mapLoader.loadMap();
		this.map = this.mapLoader.getMap();
		Engine.getEngine(this, this.getClass()).addLayer(false, false, false, 4);
		Engine.getEngine(this, this.getClass()).addLayer(false, true , false, 5,6);
		this.grid = new Grid(4, 1920/Map.DEFAULT_SQUARESIZE, 1080/Map.DEFAULT_SQUARESIZE, new Location(0,0), true);
		createMenu();
		createHotbar();
	}

	private void createHotbar() {
		hotbar = new Hotbar(9, new Location((1920-60*9)/2,1000), 5);
		hotbar.show();
	}

	private void createMenu() {
		//---Ground-Build---
		menu = new Menu(new Image(new Location(1920,0), new Dimension(300, 1049), "", Sprites.Sidebar.getSpriteSheet(), null), 5);
		Image ground = new Image(new Location(1705,20), new Dimension(123, 50), "", Sprites.Buttons.getSpriteSheet(), null);
		menu.addButton(0, ground, new Action() {
			int index = 1;
			double lastTime = System.currentTimeMillis();
			@Override
			public void act(Object caller, Object... data) {
				if(System.currentTimeMillis()-lastTime<500)return;
				lastTime = System.currentTimeMillis();
				if(Sprites.Buttons.getSpriteSheet().getSprite(index)==null)index = 0;
				ground.setSpriteState(index);
				if(index == 1){
					build = true;
					LayerUp = 0;
					menu.disableButtons(2);
					menu.disableButtons(3);
					//menu.disableButtons(5);
					menu.enableButtons(4);
				}else{
					build = false;
					LayerUp = 0;
					menu.enableButtons(2);
					menu.disableButtons(3);
					//menu.disableButtons(5);
					menu.disableButtons(4);					
				}
				index++;
			}
		});
		
		//---Hintergrund---
		for(int x = 0; x< 5; x++){
			for(int y = 0; y<18; y++){
				menu.addButton(1, new Image(new Location(1920-x*50-70,y*50+90), new Dimension(40, 40), "", Sprites.Slot.getSpriteSheet(), null), new Action() {
					@Override
					public void act(Object caller, Object... data) {	
					}
				});	
			}
		}
		
		//---AddImages---
		HashMap<Integer, Point> Locs = new HashMap<>();
		for(MapResource resource: MapResource.getMapResource()){
			int key = 2;
			if(resource.getLayerUp()==1)key++;
			if(!resource.isGround())key+=2;
			if(!Locs.containsKey(key))Locs.put(key, new Point(0,0));
			Point p = Locs.get(key);
			Image I = new Image(new Location(1920-p.x*50-65,p.y*50+95), new Dimension(30, 30), "", resource.getSprites(), null);
			I.setSpriteState(resource.getSpriteIDs()[0]);
			menu.addButton(key, I, new Action() {
				MapResource res = resource;
				@Override
				public void act(Object caller, Object... data) {		
					if(res==null) Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(), 0);
					else Mouse.getMouse().setImage(res.getSprites(), res.getSpriteIDs()[0]);
					selectedRes = res;		
				}
			});	
			p.setLocation(p.x+1, p.y);
			if(p.x>=5)p.setLocation(0, p.y+1);
		}
		
		//---Up-Down
		Image b = new Image(new Location(1830, 990), new Dimension(30,30), "", Sprites.Arrows.getSpriteSheet(), null);
		b.setSpriteState(1);
		menu.addButton(0,b, new Action() {
			double time = System.currentTimeMillis();
			@Override
			public void act(Object caller, Object... data) {
				if(System.currentTimeMillis()-time<500)return;
				time = System.currentTimeMillis();
				if(Sprites.Arrows.getSpriteSheet().getSprite(LayerUp)==null) LayerUp = 0;
				b.setSpriteState(LayerUp);
				if(LayerUp == 0){
					menu.disableButtons(2);
					menu.disableButtons(4);
					if(build)menu.enableButtons(5);
					else menu.enableButtons(3);
					hotbar.clear();
					selectedRes = null;
				}else{
					menu.disableButtons(3);
					//menu.disableButtons(5);
					if(build)menu.enableButtons(4);
					else menu.enableButtons(2);
					hotbar.clear();
					selectedRes = null;
				}
				LayerUp++;
			}
		});
		menu.disableButtons(3);
		menu.disableButtons(4);
	}

	private MapResource res = null;
	public void tick() {
		map.move();
		grid.tick();
		if(menu.isVisible()){
			menu.tick();
			if(menu.isOpen() && Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_Q)){
				menu.hide();
				selectedRes = null;
				Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(),0);
			}else{
				hotbar.tick();
				if(hotbar.getState()!=-1 && selectedRes !=null){
					hotbar.setRecource(selectedRes, hotbar.getState());
					Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(),0);
					selectedRes = null;
				}
			}
		}else{
			menu.tick();
			if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_Q)){
//				System.out.println("Show");
				menu.show();
				Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(),0);
				hotbar.setSelected(-1);
			}
			if(hotbar!=null){
				hotbar.tick();
				if(hotbar.getSlected()==null && res!=null){
					Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(),0);
				}
				if(hotbar.getSlected()!=null && !hotbar.getSlected().equals(res)){
					Mouse.getMouse().setImage(hotbar.getSlected().getSprites(), hotbar.getSlected().getSpriteIDs()[0]);
				}
				res = (MapResource) hotbar.getSlected();
				if(!hotbar.contains(Engine.getInputManager().MousePosition())){
					int x = Mouse.getMouse().getPosition().x/Map.DEFAULT_SQUARESIZE;
					int y = Mouse.getMouse().getPosition().y/Map.DEFAULT_SQUARESIZE; 
					if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1)){
						if(res!=null && x>0 && y>0 && !map.hasResource(res, x, y)){
							if(res.isGround())map.addToGround(res.getID(), x, y);
							else map.addToBuild(res.getID(), x, y);
						}
					}else if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON3)){
						if(!build)map.addToGround(0, x, y);
						else map.addToBuild(0, x, y);
					}
				}
			}
			if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_S) && System.currentTimeMillis()-lastSaved>10000){
				//System.out.println("save");
				mapLoader.saveMap();
				lastSaved = System.currentTimeMillis();
			} else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_G) && System.currentTimeMillis()-lastGridToggle>500){
				grid.toggleVisible();
				lastGridToggle = System.currentTimeMillis();
			}
		}
	}

	public Map getMap() {
		return map;
	}
}
