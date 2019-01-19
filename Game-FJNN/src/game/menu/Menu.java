package game.menu;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import Data.Events.Action;
import Data.Image.Image;
import Engine.Engine;
import tick.TickManager;

public class Menu {
	
	private static int OPENSPEED = 15;
	
	private HashMap<Integer, ArrayList<Button>> buttons = new HashMap<>();
	private HashMap<Integer, Boolean> buttonActive = new HashMap<>();
	private Image background;
	private Dimension size;
	private boolean visible = false;
	private int layer;
	
	private int changeSize = -1;	
	private double grow = 0.0;
	
	public Menu(Image background, int layer){
		this.layer = layer;
		this.size = new Dimension(background.getWidth(), background.getX());
		background.disabled = true;
		this.background = background;
		Engine.getEngine(this, this.getClass()).addImage(background, layer);
	}
	
	public void addButton(int id, Image I, Action a){
		if(!buttons.containsKey(id))buttons.put(id, new ArrayList<Button>());
		if(!buttonActive.containsKey(id))buttonActive.put(id, true);
		Button button = new Button(a, layer, I);
		buttons.get(id).add(button);
		I.disabled = true;
		Engine.getEngine(this, this.getClass()).addImage(I, layer+1);
	}
	
	public void show(){
		if(!visible){
			changeSize = 0;
			visible = true;
			background.disabled = false;
			background.setDimension(1,background.getHeigth());
		}
	}
	
	public void hide(){
		if(visible){
			changeSize = 1;	
			for(int id: buttonActive.keySet()){
				for(Button button: buttons.get(id))button.hide();
			}
		}
	}
	
	public void tick(){
		if(changeSize == 0){
			if(background.getWidth()!=size.width){
				grow += Menu.OPENSPEED*TickManager.getDeltaTime();
				if(grow>=size.width){
//					System.out.println("==="+grow+"===");
					grow = size.width;
					changeSize = -1;
					for(int id: buttonActive.keySet()){
//						System.out.println("VIEW"+buttonActive.get(id)+":"+buttons.get(id));
						if(buttonActive.get(id))for(Button button: buttons.get(id)){
							button.show();
//							System.out.println(button);
						}
					}
//					System.out.println("===["+changeSize+" -> "+grow+"|"+size.width+"]===");
				}
				background.setDimension((int) grow, background.getHeigth());
				background.setLocation((int) (size.getHeight()-grow), background.getY());
			}
		}else if(changeSize == 1){
//			System.out.println("REMOVE");
			if(background.getWidth()!=0){
				grow -= Menu.OPENSPEED*TickManager.getDeltaTime();
				if((int)grow<=0){
					grow = 1;
					background.disabled = true;
					visible = false;
				}
				background.setDimension((int) grow, background.getHeigth());
				background.setLocation((int) (size.getHeight()-grow), background.getY());
			}
		}else if(changeSize == -1){
			if(visible){
				for(int id: buttonActive.keySet()){
					if(buttonActive.get(id))for(Button button: buttons.get(id)){
						if(button.getImage(0).getHitbox().contains(Engine.getInputManager().MousePosition())){
							if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1) && !button.isActive()){
								button.tick();
								button.setActive(true);
							}else if(!Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1) && button.isActive()){
								button.setActive(false);
							}
						}
					}
				}
			}
			
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isOpen() {
		return changeSize==-1;
	}
	
	public void disableButtons(int id){
		
		if(buttonActive.get(id)==false)return;
		buttonActive.put(id, false);
		for(Button button:buttons.get(id)){
//			System.out.println(button);
			button.hide();
		}
	}
	
	public void enableButtons(int id){
		if(buttonActive.get(id)==true)return;
		buttonActive.put(id, true);
		for(Button button:buttons.get(id)){
			button.show();
		}
	}

	public void hideDirect() {
		if(visible){
			for(int id: buttonActive.keySet()){
				for(Button button: buttons.get(id))button.hide();
			}
			background.disabled = true;
			visible = false;
			background.setDimension(1, background.getHeigth());
			background.setLocation((int)size.getHeight(), background.getY());
			visible = false;
		}
	}

}
