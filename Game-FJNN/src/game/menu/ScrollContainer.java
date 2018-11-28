package game.menu;

import java.awt.Dimension;
import java.util.ArrayList;

import Data.Hitbox;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.RotationSprites;

public class ScrollContainer {
	
	public class ScrollContent {
		
		private Location loc;
		private Button master;
		
		private ScrollContent(Location loc, Button master) {
			this.loc = loc;
			this.master = master;
		}
	
		public Button getMaster() {
			return master;
		}

		public Location getLocation() {
			return loc;
		}
	
	}
	
	private ScrollBar bar;
	private Image background;
	private ArrayList<ScrollContent> content = new ArrayList<>();
	
	private int layer;
	private boolean visible = false;
	private boolean visulasCreated = false;
	private Location location;
	private Dimension size;
	
	private int maxContentWidth;
	private int maxContentHeigth = 1;
	private int maxContentVisibleHeigth;
	private int contentSize;
	
	private Hitbox hb;
	
	public ScrollContainer(Location loc, Dimension size, int layer, int maxContentWidth){
		this.layer = layer;
		createGraphics(loc, size, maxContentWidth);
	}

	public ScrollContainer(Location loc, Dimension size, int layer, int maxContentWidth, Image background){
		this.layer = layer;		
		this.background = background;
		createGraphics(loc, size, maxContentWidth);
	}
	
	private void createGraphics(Location loc, Dimension size, int maxContentWidth) {
		this.size = size;
		this.maxContentWidth = maxContentWidth;
		this.maxContentVisibleHeigth = (int) Math.ceil(size.getHeight()/(size.getWidth()/(double)maxContentWidth));
		this.contentSize = (int) ((size.getWidth()-40)/(double)maxContentWidth);
		this.location = loc;		
		this.hb = new Hitbox(loc, size);
		
		this.bar = new ScrollBar(new Location((int) (loc.getX() + size.getWidth()-20), loc.getY()+10), (int) (size.getHeight()-20), RotationSprites.Orientation_UP, 10, this.layer);
	}
	
	public void createVisulas(){		
		Engine engine = Engine.getEngine(this, this.getClass());
		if(this.background!=null){
			this.background.disabled = false;
			engine.addImage(background, layer);
		}
		
		this.bar.createVisuals();	
		for(int i = 0; i<content.size(); i++){
			ScrollContent b = content.get(i);
			b.getMaster().createVisuals();
			if(!contentIsVisible(b))b.getMaster().hide();
		}
		this.visible = true;
		this.visulasCreated = true;
	}
	
	public void distroyVisulas(){		
		Engine engine = Engine.getEngine(this, this.getClass());
		if(this.background!=null){
			this.background.disabled = true;
			engine.removeImage(layer, background);
		}
		
		this.bar.destroyVisuals();		
		for(int i = 0; i<content.size(); i++){
			ScrollContent b = content.get(i);
			b.getMaster().destroyVisuals();
		}
		this.visible = false;
		this.visulasCreated = false;
	}
	
	public void show(){
		if(this.background!=null)this.background.disabled = false;
		this.bar.show();
		for(int i = 0; i<content.size(); i++){
			ScrollContent b = content.get(i);
			if(contentIsVisible(b))b.getMaster().show();
		}
		this.visible = true;
	}
	
	public void hide(){
		if(this.background!=null)this.background.disabled = true;
		this.bar.hide();
		for(int i = 0; i<content.size(); i++){
			ScrollContent b = content.get(i);
			b.getMaster().hide();
		}
		this.visible = false;
	}

	public boolean isVisible(){
		return this.visible;
	}
	
	private boolean contentIsVisible(ScrollContent content) {
		int dy = (int) (this.bar.getScrolled()*maxContentHeigth)-content.getLocation().getY(); 
		return Math.abs(dy)<maxContentVisibleHeigth 
				&& this.location.getY()<=content.getMaster().getLocation().getY() 
				&& this.location.getY()+this.size.getWidth()>=content.getMaster().getLocation().getY()+content.getMaster().getImage(0).getHeigth();
	}
	
	public void addContent(Button button){
		int y = this.content.size()/this.maxContentWidth;
		if(y>maxContentHeigth)maxContentHeigth = y;
		int x = this.content.size()-y;
		for(int i = 0; i<button.getImageAmount(); i++){
			button.getImage(i).setLocation(button.getLocation().getX()+x*this.contentSize+this.location.getX(), button.getLocation().getY()+y*this.contentSize+this.location.getY()+(int)(this.bar.getScrolled()*contentSize));
		}
		ScrollContent content = new ScrollContent(new Location(x, y), button);
		this.content.add(content);
		if(isVisible()){
			if(visulasCreated)button.createVisuals();
			else button.show();
		}
	}
	
	public void tick(){
		if(!this.isVisible())return;
		if(this.hb.contains(Engine.getInputManager().MousePosition()) && this.bar.tick()){
			for(ScrollContent content: this.content){
				Button button = content.getMaster();
				int x = content.getLocation().getX();
				int y = content.getLocation().getY();
				Location buttonLocation = button.getLocation();
				for(int i = 0; i<button.getImageAmount(); i++){
					button.getImage(i).setLocation((button.getImage(i).getX()-buttonLocation.getX())+x*this.contentSize+this.location.getX(), (button.getImage(i).getY()-buttonLocation.getY())+y*this.contentSize+this.location.getY()-(int)(this.bar.getScrolled()*contentSize*Math.max(maxContentHeigth-maxContentVisibleHeigth, 0)));
				}
				if(contentIsVisible(content))button.show();
				else button.hide();
			}
		}else{
			for(ScrollContent content: this.content)content.getMaster().tick();
		}
	}

}
