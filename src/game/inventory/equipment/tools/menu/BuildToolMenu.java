package game.inventory.equipment.tools.menu;

import java.awt.Color;
import java.awt.Dimension;

import Data.Location;
import Data.Events.Action;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import data.MapResource;
import data.map.EdgeBlockData;
import game.inventory.crafting.Recipe;
import game.menu.Button;
import game.menu.ScrollContainer;
import sprites.Sprites;

public class BuildToolMenu extends ToolMenu{
	
	private static int SelectableResourceAmount = 10;
	private static Dimension Size = new Dimension(1200,675);	
	private static Location Location = new Location((int) ((1920-Size.getWidth())/2), (int) ((1080-Size.getHeight())/2));
	
	private int[] selectedResources = new int[SelectableResourceAmount];
	private Button[] selectedResourcesVisuals = new Button[SelectableResourceAmount];

	private ScrollContainer selectMenu;
	private int selected = 0;
	
	public BuildToolMenu() {
		super(Location, Size);
		int size = (int) ((Size.getWidth()-100)/SelectableResourceAmount);
		
		for(int i = 0; i<SelectableResourceAmount; i++){
			int a = i;
			selectedResourcesVisuals[i] = new Button(new Action() {
				int id = a;
				@Override
				public void act(Object arg0, Object... arg1) {
					selectedResourcesVisuals[selected].getImage(0).setSpriteState(0);
					selected = this.id;
					selectedResourcesVisuals[selected].getImage(0).setSpriteState(1);
				}
			}, Defaut_Layer, 
				new Image(new Location(Location.getX()+50				 +i*(size),Location.getY()+50				 ), new Dimension((int) (size*0.9), (int) (size*0.9)), "", Sprites.Slot  .getSpriteSheet(), null),
				new Image(new Location(Location.getX()+50+(int)(size*0.2)+i*(size),Location.getY()+50+(int)(size*0.2)), new Dimension((int) (size*0.5), (int) (size*0.5)), "", Sprites.Blocks.getSpriteSheet(), null)
				);
			selectedResourcesVisuals[i].getImage(1).setSpriteState(9);
			if(i==selected)selectedResourcesVisuals[i].getImage(0).setSpriteState(1);
		}
		
		this.selectMenu = new ScrollContainer(new Location(Location.getX()+40, selectedResourcesVisuals[0].getLocation().getY()+(int)(size*1.2)), new Dimension((int) (Size.getWidth()-80), 300), Defaut_Layer, SelectableResourceAmount,
									new Image(new Location(Location.getX()+40, selectedResourcesVisuals[0].getLocation().getY()+(int)(size*1.2)), new Dimension((int) (Size.getWidth()-80), 300), "", new SpriteSheet(Image.createColorImage(1, 1, new Color(40, 40, 40))), null));
		
		for(MapResource res: MapResource.getMapResource()){
			Recipe rec = Recipe.getRecipe(res.getID());
			if(rec!=null){
				Button button = new Button(new Action() {
					@Override
					public void act(Object arg0, Object... arg1) {
						selectedResources[selected] = res.getID();
						selectedResourcesVisuals[selected].getImage(1).setSpriteState(0);
						selectedResourcesVisuals[selected].getImage(1).setSpriteSheet(res.getSprites());
						if(res.hasData() && res.getBlockData() instanceof EdgeBlockData){
							selectedResourcesVisuals[selected].getImage(1).setSpriteState(11);
						}else{
							selectedResourcesVisuals[selected].getImage(1).setSpriteState(res.getSpriteIDs()[0]);
						}
					}
				}, Defaut_Layer, 
					new Image(new Location(0				, 0				 ), new Dimension((int) (size*0.9), (int) (size*0.9)), "", Sprites.Slot  .getSpriteSheet(), null),
					new Image(new Location((int) (size*0.2)	,(int) (size*0.2)), new Dimension((int) (size*0.5), (int) (size*0.5)), "", Sprites.Blocks.getSpriteSheet(), null)
					);
				if(res.hasData() && res.getBlockData() instanceof EdgeBlockData){
					button.getImage(1).setSpriteSheet(res.getSprites());
					button.getImage(1).setSpriteState(11);
				}else{
					button.getImage(1).setSpriteState(res.getSpriteIDs()[0]);
				}
				this.selectMenu.addContent(button);
			}
		}
	}
	
	@Override
	public void hide() {
		super.hide();
		for(int i = 0; i<SelectableResourceAmount; i++)selectedResourcesVisuals[i].hide();
		selectMenu.hide();
	}

	@Override
	public void show() {
		super.show();
		for(int i = 0; i<SelectableResourceAmount; i++)selectedResourcesVisuals[i].show();	
		selectMenu.show();
	}

	@Override
	public void createVisuals() {
		super.createVisuals();
		for(int i = 0; i<SelectableResourceAmount; i++)selectedResourcesVisuals[i].createVisuals();	
		selectMenu.createVisulas();
	}

	@Override
	public void destroyVisuals() {
		super.destroyVisuals();
		for(int i = 0; i<SelectableResourceAmount; i++)selectedResourcesVisuals[i].destroyVisuals();	
		selectMenu.distroyVisulas();
	}

	@Override
	public void tick() {
		selectMenu.tick();
		for(int i = 0; i<selectedResourcesVisuals.length; i++){
			selectedResourcesVisuals[i].tick();
		}
	}
	
	public int getSelected() {
		return this.selectedResources[selected];
	}

	public void moveSelected(int amount) {
		this.selectedResourcesVisuals[selected].getImage(0).setSpriteState(0);
		this.selected=this.selected+amount;
		if(this.selected>=SelectableResourceAmount)this.selected-=SelectableResourceAmount;
		if(this.selected<0)this.selected+=SelectableResourceAmount;
		this.selectedResourcesVisuals[selected].getImage(0).setSpriteState(1);
	}
}
