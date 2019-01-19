package game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Data.Image.Image;
import Engine.Engine;
import data.map.Factory;
import game.map.Map;
import game.vehicle.BluePrint;
import sprites.Sprites;
import tick.TickManager;

public class FactoryMenu {
	
	private Image image;
	private Image menu;
	private Image frame;
	private Image scrollBar;
	private Image scrollButton;
	private ArrayList<ScrollItem<BluePrint>> scrollItems = new ArrayList<>();
	private StringRenderOperation name;
	private StringRenderOperation edit;
	private StringRenderOperation create;
	
	private ScrollItem<BluePrint> selected;
	private int moved = 0;
	private int movedState = 0;
	
	private Location location;
	private int layer;	
	private boolean created = false;
	
	private Factory factory;
	
	public FactoryMenu(Factory factory, int layer){
		this.layer = layer;
		location = new Location((int)(1920-Sprites.FactoryMenu.getDimension().getWidth())/2, (int)(1080-Sprites.FactoryMenu.getDimension().getHeight())/2);
		this.factory = factory;
	}
	
	public FactoryMenu create(){
		if(!created){
			this.frame	= new Image(location								, Sprites.FactoryFrame	.getDimension(), "", Sprites.FactoryFrame.getSpriteSheet(), null);
			this.menu	= new Image(new Location(location.x+3, location.y+3	), Sprites.FactoryMenu	.getDimension(), "", Sprites.FactoryMenu .getSpriteSheet(), null);
			
//			this.image = new Image(new Location(location.x+10, location.y+50), new Dimension(90, 90), "", Functions.FuseToOneSprite(MapResource.Factory), null);
			
			this.scrollBar 	 = new Image(new Location(location.x+frame.getWidth()-15, location.y+160), Sprites.FactoryScrollBar		.getDimension(), "", Sprites.FactoryScrollBar	.getSpriteSheet(), null);
			this.scrollButton= new Image(new Location(location.x+frame.getWidth()-19, location.y+165), Sprites.FactoryScrollButton	.getDimension(), "", Sprites.FactoryScrollButton.getSpriteSheet(), null);
			
			this.name 	= new StringRenderOperation(new Location(  2+location.x,  35+location.y), new Dimension(frame.getWidth()-20	, 60), factory.getName(), new Font("TimesRoman", Font.BOLD, 35), Color.WHITE);
			this.edit	= new StringRenderOperation(new Location(110+location.x, 130+location.y), new Dimension(90					, 60), "EDIT"			, new Font("TimesRoman", Font.BOLD, 24), Color.WHITE);
			this.create	= new StringRenderOperation(new Location(190+location.x, 130+location.y), new Dimension(90					, 60), "CREATE"			, new Font("TimesRoman", Font.BOLD, 24), Color.WHITE);			
			
			Engine.getEngine(this, this.getClass()).addImage(menu				, layer  );
			Engine.getEngine(this, this.getClass()).addImage(image				, layer  );
			Engine.getEngine(this, this.getClass()).addImage(scrollBar			, layer  );
			Engine.getEngine(this, this.getClass()).addImage(scrollButton		, layer  );			
			
			Engine.getEngine(this, this.getClass()).addGraphicOperation(name	, layer  );
			Engine.getEngine(this, this.getClass()).addGraphicOperation(edit	, layer  );
			Engine.getEngine(this, this.getClass()).addGraphicOperation(create	, layer  );
			
			Engine.getEngine(this, this.getClass()).addImage(image				, layer+1);
			Engine.getEngine(this, this.getClass()).addImage(frame				, layer+1);
			

			for(BluePrint blueprint: BluePrint.BLUEPRINTS){
				createScrollItem(blueprint);
			}
			created = true;
		}
		return this;
	}
	
	private void createScrollItem(BluePrint blueprint) {
		ScrollItem<BluePrint> item = new ScrollItem<BluePrint>(blueprint, new Location(10+location.x,160+35*scrollItems.size()+location.y+moved), blueprint.getName(), layer, new Image(new Location(location.x+10,location.y+160+35*scrollItems.size()), Sprites.FactoryScrollItem.getSpriteDimension(), "", Sprites.FactoryScrollItem.getSpriteSheet(), null));
		if(item.getHitbox().getHeigth()+item.getHitbox().getY()<=frame.getHeigth()+frame.getY()){
			if(menu.visible)item.show();
		}
		scrollItems.add(item);
	}

	public void show(){
		if(!created){
			create();
		}else{
			menu.			disabled = false;
			frame.			disabled = false;
			image.			disabled = false;
			scrollBar.		disabled = false;
			scrollButton.	disabled = false;	
			name.			disabled = false;
			edit.			disabled = false;
			create.			disabled = false;
		}
		for(ScrollItem<BluePrint> item: scrollItems)if(item.getHitbox().getHeigth()+item.getHitbox().getY()<=frame.getHeigth()+frame.getY())item.show();
	}
	
	public void hide(){
		if(created){
//			System.out.println("Hide");
			menu.			disabled = true;
			frame.			disabled = true;
			image.			disabled = true;
			scrollBar.		disabled = true;
			scrollButton.	disabled = true;	
			name.			disabled = true;
			edit.			disabled = true;
			create.			disabled = true;
			
			for(int i = 0; i<scrollItems.size(); i++){
				ScrollItem<BluePrint> scrollItem = scrollItems.get(i);
				scrollItem.hide();
				scrollItem.setLocation(scrollItem.getHitbox().getX(), 160+(35*i)+location.y);
			}
			
			moved = 0;
		}
	}
	
	long lastTime = System.currentTimeMillis();
	public BluePrint tick(){
		if(!menu.disabled && System.currentTimeMillis()-lastTime>100){
			lastTime = System.currentTimeMillis();
			if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1)){
				if(menu.getHitbox().contains(Engine.getInputManager().MousePosition())){
					for(ScrollItem<BluePrint> item: scrollItems){
						if(item.isVisible() && item.containsPoint(Engine.getInputManager().MousePosition())){
							if(this.selected!=null)this.selected.deselect();
							this.selected = item;
							this.selected.select();
//							System.out.println("select");
							return null;
						}
					}
					Point p = new Point(Engine.getInputManager().MousePosition().x+8,Engine.getInputManager().MousePosition().y+31);
					if(this.create.Hitbox.contains(p)){
//						System.out.println("create");
						if(scrollItems.size()>=1000)return null;
						String name = "BluePrint#"+(scrollItems.size()+1);
						while(name.length()<"BluePrint#".length()+3)name="BluePrint#0"+name.substring("BluePrint#".length());
						BluePrint blueprint = new BluePrint(20, 20, name);
						BluePrint.BLUEPRINTS.add(blueprint);
						createScrollItem(blueprint);
					}else if(this.edit.Hitbox.contains(p) && selected !=null){
//						System.out.println("edit");
						Map.getMap().hide();
						this.hide();
						Engine.getEngine(this, this.getClass()).clearVision();
						return selected.getObjekt();
					}
				}
			}
		}
		if(Engine.getInputManager().getMouseWheelsMove((long)(2*TickManager.getTickDuration()*TickManager.getDeltaTime()))!=0){
			int rotation = Engine.getInputManager().getMouseWheelsMove((long)(2*TickManager.getTickDuration()*TickManager.getDeltaTime()));
			moveItems(rotation*5);
		}
		return null;
	}

	private void moveItems(int amount) {
		
		if(movedState==2)return;
		else if(amount<0 && movedState==-1)return;
		else if(amount>0 && movedState== 1)return;		
		movedState = 0;
		
		for(int i = 0; i<scrollItems.size(); i++){
			ScrollItem<BluePrint> item = scrollItems.get(i);
			if(item.getHitbox().getHeigth()+item.getHitbox().getY()<=frame.getHeigth()+frame.getY() && item.getHitbox().getY() > frame.getY()+150){
				if(i==scrollItems.size()-1){
					if(movedState == 1)movedState=2;
					else movedState = -1;
				}else if(i==0){
					if(movedState == -1)movedState=2;
					else movedState = 1;
				}
				if(item.equals(this.selected))item.select();
				item.show();
			}else{
				item.hide();
			}
			item.move(amount);
		}
		
		this.moved+=amount;
		
		double procent = (double)moved/(double)(scrollItems.get(scrollItems.size()-7).getHitbox().getY()-scrollItems.get(0).getHitbox().getY());
		if(procent<0) procent*=-1;
		if(procent>1) procent = 1;
		
//		System.out.println(procent);
		scrollButton.setLocation(scrollButton.getX(), scrollBar.getY()+5+(int)((this.scrollBar.getHeigth()-30)*procent));
	}

	public boolean isVisible() {
		return menu!=null && menu.disabled==false && created;
	}

	public BluePrint getCurrentBluePrint() {
		if(selected==null)return null;
		return selected.getObjekt();
	}

	public void setSelected(BluePrint p) {
		if(p==null)this.selected=null;
		else {
			for(ScrollItem<BluePrint> item : scrollItems){
				if(item.getObjekt().equals(p)){
					this.selected=item;
					return;
				}
			}
		}
	}

}
