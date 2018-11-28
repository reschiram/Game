package game.menu;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Data.Hitbox;
import Data.Location;
import Data.Events.Action;
import Data.Image.Image;
import Engine.Engine;
import data.Mouse;
import data.ResourcePart;
import data.VehicleResource;
import game.map.Map;
import game.vehicle.BluePrint;
import game.vehicle.data.VehicleArmorData;
import game.vehicle.data.VehicleWeaponData;
import game.gridData.vehicle.*;
import sprites.Sprites;

public class VehicleEditor {
	
	private BluePrint bluePrint;
	
	private Grid grid;
	private Hotbar hotbar;
	private Menu sideMenu;
	private StatsMenu statsMenu;
	private boolean visible = false;
	
	private Image Background;
	
	private VehicleData[][] creationVisual;
	private Location location;
	
	private VehicleResource currentResource;
	
	public VehicleEditor(){		
		Background = new Image(new Location(0,0), new Dimension(1920, 1080), "", Sprites.Backgrounds.getSpriteSheet(), null);
		statsMenu = new StatsMenu(new Location(0,1080/2-200), 6);
		statsMenu.show();
		hotbar = new Hotbar(9, new Location((1920-60*9)/2,1000), 6);
		sideMenu = new Menu(new Image(new Location(1920,0), new Dimension(300, 1049), "", Sprites.Sidebar.getSpriteSheet(), null), 5);
		for(int x = 0; x< 5; x++){
			for(int y = 0; y<18; y++){
				sideMenu.addButton(1, new Image(new Location(1920-x*50-70,y*50+90), new Dimension(40, 40), "", Sprites.Slot.getSpriteSheet(), null), new Action() {
					@Override
					public void act(Object caller, Object... data) {	
					}
				});	
			}
		}
		for(int i = 0; i<VehicleResource.getVehicleResources().length; i++){
			VehicleResource res = VehicleResource.getVehicleResources()[i];
			int y = i/5;
			int x = i-y*5;
			Image image = new Image(new Location(1920-x*50-65,y*50+95), new Dimension(30, 30), "", res.getSprites(), null);
			image.setSpriteState(res.getSpriteIDs()[0]);
			int a = 0;
			if(res.getData() instanceof VehicleWeaponData)a=2;
			if(res.getData() instanceof VehicleArmorData )a=1;
			sideMenu.addButton(2+a, image, new Action() {
				VehicleResource resource = res;
				@Override
				public void act(Object caller, Object... data) {	
					currentResource = resource;
					Mouse.getMouse().setImage(resource.getSprites(), res.getSpriteIDs()[0]);
					if(currentResource!=null){
//						System.out.println(currentResource.getID());
						statsMenu.create(currentResource.getData()).show();
//						System.out.println("SHOW");
					}else statsMenu.destroy();
				}
			});	
		}
//		sideMenu.disableButtons(2);
//		sideMenu.disableButtons(4);
		
		Image[] IconImages = new Image[3];
		IconImages[0] =  new Image(new Location(1920-115,25), new Dimension(30, 30), "", Sprites.VehiclePartTypesIcons.getSpriteSheet(), null);
		IconImages[1] =  new Image(new Location(1920-165,20), new Dimension(40, 40), "", Sprites.VehiclePartTypesIcons.getSpriteSheet(), null);
		IconImages[2] =  new Image(new Location(1920-205,25), new Dimension(30, 30), "", Sprites.VehiclePartTypesIcons.getSpriteSheet(), null);
		for(int i = 0; i<IconImages.length; i++){
			Image icon = IconImages[i];
			icon.setSpriteState(i);
			int hilf = i;
			sideMenu.addButton(1, icon, new Action() {	
				Image[] images = IconImages;
				int data = hilf;
				@Override
				public void act(Object caller, Object... data) {
					images[1].setSpriteState(this.images[this.data].getSpriteState());
					if(this.images[this.data].getSpriteState()==0){
						images[0].setSpriteState(2);
						images[2].setSpriteState(1);
//						sideMenu.enableButtons(2);
						sideMenu.disableButtons(3);
//						sideMenu.disableButtons(4);
					}else if(this.images[this.data].getSpriteState()==2){
						images[0].setSpriteState(1);
						images[2].setSpriteState(0);
//						sideMenu.enableButtons(4);
//						sideMenu.disableButtons(2);
						sideMenu.disableButtons(3);
					}else if(this.images[this.data].getSpriteState()==1){
						images[0].setSpriteState(0);
						images[2].setSpriteState(2);	
						sideMenu.enableButtons(3);
//						sideMenu.disableButtons(2);
//						sideMenu.disableButtons(4);					
					}
				}
			});
		}
		
	}

	long time = System.currentTimeMillis();
	
	public void tick(){
		if(this.bluePrint!=null && visible){
			this.sideMenu.tick();
			this.hotbar.tick();
			
			//SideMenu
			if(System.currentTimeMillis()-time>100){
				time = System.currentTimeMillis();
				if(sideMenu.isVisible()){
					if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_Q)){
						sideMenu.hide();
					}
				}else{					
					if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_Q)){
						sideMenu.show();
					} 
				}
			}
			
			//HotBar&PlaceMent
			if(!sideMenu.isVisible()){
				//PlaceMent
				Point p = new Point((Engine.getInputManager().MousePosition().x-location.getX())/Map.DEFAULT_SQUARESIZE, (Engine.getInputManager().MousePosition().y-location.getY())/Map.DEFAULT_SQUARESIZE);
				if(p.x>= 0 && p.y>= 0 && p.x<bluePrint.getWidth() && p.y<bluePrint.getHeigth()){
					if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1) && 
						currentResource!=null && bluePrint.get(p)!= currentResource.getID() && !hotbar.contains(Engine.getInputManager().MousePosition())){
							addPart(p);
					}
				}
			}else{
				//Hotbar To Mouse while sideMenu
				if(hotbar.getState()!=-1 && currentResource !=null){
//					System.out.println("SetResource ->"+currentResource.getID()+"|"+hotbar.getState());
					hotbar.setRecource(currentResource, hotbar.getState());
					Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(),0);
					hotbar.setSelected(-1);		
					currentResource=null;
				}
			}
			//SetCurrentResource
			if(hotbar.getSlected()!=null && !hotbar.getSlected().equals(currentResource)){
				currentResource=(VehicleResource)hotbar.getSlected();
				if(currentResource!=null){
//					System.out.println(currentResource.getID());
					statsMenu.create(currentResource.getData()).show();
//					System.out.println("SHOW");
				}else statsMenu.destroy();
				if(!sideMenu.isVisible()){
					//Hotbar To Mouse while no sideMenu
					if(currentResource==null)Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(), 0);
					else{
//						System.out.println(currentResource.getID());
						Mouse.getMouse().setImage(currentResource.getSprites(), currentResource.getSpriteIDs()[0]);
					}
				}
			}
		}
	}
	
	private void addPart(Point p) {
//		System.out.println(bluePrint.get(p));
		if(bluePrint.get(p)!=VehicleResource.getNONID() || creationVisual[p.x][p.y]!=null)remove(p);
		bluePrint.set(p,currentResource.getID());
//		System.out.println(bluePrint.get(p));
		for(ResourcePart part: VehicleResource.getVehicleResource(bluePrint.get(p)).getResourceParts())remove(part.getLocation().toPoint());
		setImage(p);
	}

	private void remove(Point p) {
		VehicleData data = creationVisual[p.x][p.y];
		VehicleBlock block = null;
		if(data instanceof VehicleBlock) block = (VehicleBlock)data;
		else if(data instanceof VehicleDummieBlock) block = ((VehicleDummieBlock)data).getBlock();
		creationVisual[p.x][p.y] = null;
		block.destroyVisual();
		for(VehicleData party: block.blockParts){
			creationVisual[party.getLocation().getX()][party.getLocation().getY()] = null;
		}
	}

	private void setImage(Point p) {
		if(!new Hitbox(0, 0, 20, 20).contains(p))return;
		if(bluePrint.get(p) == VehicleResource.getNONID()) return;
		VehicleResource resource = VehicleResource.getVehicleResource(bluePrint.get(p));
		VehicleBlock block = new VehicleBlock(resource, 5, new Location(p));
		for(VehicleData data: block.create()){
			if(!data.equals(block)){
				creationVisual[p.x+data.getLocation().getX()][p.y+data.getLocation().getY()] = data;
				data.show();
			}
		}
		creationVisual[p.x][p.y] = block;
		block.show();
		block.setLocation(new Location(p.x*Map.DEFAULT_SQUARESIZE+location.x, p.y*Map.DEFAULT_SQUARESIZE+location.y));
//		for(int i = 0; i<resource.getResourceParts().length; i++){
//			SpriteSheet spriteSheet = null;
//			int spriteID = 0;
//			if(i==0){
//				spriteSheet = resource.getSprites().getSpriteSheet();
//				spriteID = resource.getSpriteIDs()[0];
//				i--;
//			}else{
//				spriteSheet = resource.getResourceParts()[i].getSprites().getSpriteSheet();
//				spriteID = resource.getResourceParts()[i].getSpriteIDs()[0];
//			}
//			
//			Image image = creationVisual[p.x][p.y];
//			if(image==null){
//				image = new Image(new Location(p.x*Map.DEFAULT_SQUARESIZE+location.x, p.y*Map.DEFAULT_SQUARESIZE+location.y), new Dimension(Map.DEFAULT_SQUARESIZE, Map.DEFAULT_SQUARESIZE),
//					"", spriteSheet, null);
//				Engine.getEngine(this, this.getClass()).addImage(image, 5);
//				creationVisual[p.x][p.y] = image; 
//			}
//			image.setSpriteSheet(spriteSheet);
//			image.setSpriteState(spriteID);
//		}
//		System.out.println(image.getLocation().toString());
	}

	public void show(BluePrint blueprint){
		Engine.getEngine(this, this.getClass()).addImage(Background, 1);
		this.bluePrint = blueprint;
		creationVisual = new VehicleData[blueprint.getWidth()][blueprint.getHeigth()];
		location = new Location((Engine.getWindowSize().width-bluePrint.getWidth()*Map.DEFAULT_SQUARESIZE)/2, (Engine.getWindowSize().height-bluePrint.getHeigth()*Map.DEFAULT_SQUARESIZE)/2);
		for(int x = 0; x<blueprint.getWidth(); x++){
			for(int y = 0; y<blueprint.getHeigth(); y++){
				Point p = new Point(x, y);
					setImage(p);
			}
		}
//		Connected = new Image[blueprint.getWidth()][blueprint.getHeigth()];
		grid = new Grid(6, bluePrint.getWidth(), bluePrint.getHeigth(), this.location, false);
		hotbar.show();
		visible = true;
		statsMenu.show();
		if(currentResource!=null)Mouse.getMouse().setImage(currentResource.getSprites(), currentResource.getSpriteIDs()[0]);
	}
	
	public void hide(){
		Engine.getEngine(this, this.getClass()).removeImage(1, Background);
		if(hotbar!=null)hotbar.hide();
		if(grid!=null)grid.destroyVisuals();
		if(sideMenu!=null)sideMenu.hideDirect();
		if(statsMenu!=null){
			statsMenu.hide();
			statsMenu.destroy();
		}
		grid=null;
		visible = false;
		Mouse.getMouse().setImage(Sprites.Mouse.getSpriteSheet(), 0);
		for(int x = 0; x<creationVisual.length; x++){
			for(int y = 0; y<creationVisual[x].length; y++){
				if(creationVisual[x][y]!=null) creationVisual[x][y].destroyVisual();
//				if(Connected[x][y]!=null) Engine.getEngine(this, this.getClass()).removeImage(6, Connected[x][y]);
			}
		}
	}

	public boolean isVisible() {
		return visible;
	}

}
