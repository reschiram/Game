package game.entity.player.playerDrone;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import Data.Direction;
import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import data.ButtonTrigger;
import data.ImageData;
import data.Mouse;
import game.entity.Entity;
import game.entity.player.Player;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
import game.entity.type.data.EntityInventoryData;
import game.entity.type.data.LightEntityData;
import game.entity.type.interfaces.EntityInventory;
import game.entity.type.interfaces.EntityLight;
import game.inventory.Inventory;
import game.inventory.ItemCollector;
import game.inventory.items.Item;
import game.map.Map;
import game.pathFinder.PathFinder;

public class PlayerDrone extends Entity implements EntityInventory, EntityLight{
	
	
	private ItemCollector itemCollector;
	private PathFinder pathFinder;
	private Player player;
	
	private int maxDistance = 500;
	private static Point screenCenter = new Point(1920/2, 1080/2);

	private long lastTickAtHost = 0;
	private int maxTickAwayFromHost = 500;
	private long FirstTickAtHost = 0;
	
	private HashMap<Integer, DroneTarget> targets = new HashMap<>();
	private DroneTarget currentDroneTarget;
	private ButtonTrigger targetTrigger = new ButtonTrigger(MouseEvent.BUTTON1);
	
	public PlayerDrone(Player player) {
		super(new ArrayList<>());
		this.entityTypes.add(EntityType.Drone);
		this.entityTypes.add(EntityType.LightEntity);
		
		EntityType type = EntityType.Drone;
		Image image = new Image(player.getLocation().clone(), type.getSize(), "", type.getSpriteSheet(), null);
		image.setSpriteState(type.getSpriteIds()[0]);
		super.create(type.createAnimation(false, Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP+1, image), image.getLocation().clone(), type.getSize(), type.getSpeed(),
				DEFAULT_DIRECTION, Map.DEFAULT_BUILDLAYER+DEFAULT_ENTITY_UP+1, new ImageData(new Location(0,0), image));

		EntityInventoryData invData = (EntityInventoryData)EntityType.Player.getData(EntityData.ENTITYINVENTOTYDATA);
		this.itemCollector = new ItemCollector(this, invData.createInventory(), 1.2);
		this.pathFinder = new PathFinder(this, 20);
		this.player = player;
		
		this.moveManager.setDoAccelerateXVelocity(false);
		this.moveManager.setDoAccelerateYVelocity(false);
	}		
	
	@Override
	public void tick(){
		super.tick();
		itemCollector.tick();
		targetTrigger.tick();
		pathFinder.tick();
		
//		System.out.println(this.getLocation() + "|" +this.images[0].getImage().getLocation() + "|" +this.images[0].getImage().disabled + "|"+ layer);
		
		if(pathFinder.hasTarget()){
			int[] directions = this.pathFinder.nextDirection();
			this.moveManager.move(Direction.getDirection(directions[0], 0			 ));
			boolean leftRigth 	= directions[0]==0 || this.moveManager.canMoveX()==0;
			this.moveManager.move(Direction.getDirection(			0 , directions[1]));
			boolean upDown 		= directions[1]==0 || this.moveManager.canMoveY()==0;
			System.out.println(leftRigth+"|"+upDown+" -> "+directions[0]+"|"+directions[1]);
			if(leftRigth && upDown && pathFinder.isDone() && getLastTick() - lastTickAtHost <= maxTickAwayFromHost){
//				System.out.println(currentDroneTarget);
				if(currentDroneTarget!=null){
					
					int maxx = this.getWidth ()/2+Map.DEFAULT_SQUARESIZE/2;
					int maxy = this.getHeight()/2+Map.DEFAULT_SQUARESIZE/2;
					
					int distance = getDistance(currentDroneTarget, true);
//					System.out.println(distance + "|" + (20+Math.sqrt(maxx*maxx+maxy*maxy)));
					
					if(distance <= 20+Math.sqrt(maxx*maxx+maxy*maxy)){
						if(currentDroneTarget.interact()){
							targets.remove(currentDroneTarget.getLocation().getX()+currentDroneTarget.getLocation().getY()*Map.getMap().getWidth());
							currentDroneTarget = null;
							this.pathFinder.setTarget(null);
						}
					}
				}else this.pathFinder.setTarget(null);
			}
		}else if(!targets.isEmpty() && getLastTick() - lastTickAtHost <= maxTickAwayFromHost){
			currentDroneTarget = getNextDroneTarget();
			if(currentDroneTarget!=null)pathFinder.setTarget(currentDroneTarget.getPixelLocation());
		}
		
		if(getLastTick() - lastTickAtHost > maxTickAwayFromHost){
//			if(this.pathFinder.hasTarget())System.out.println(this.pathFinder.getBlockTarget() +" -> "+ this.player.getBlockLocation());			
			currentDroneTarget = null;
			if(this.getBlockLocation().isEqual(player.getBlockLocation())){
				if(FirstTickAtHost==0)FirstTickAtHost = getLastTick();
				else if(getLastTick()-FirstTickAtHost>100){
					lastTickAtHost = getLastTick();
					FirstTickAtHost = 0;
					for(int i = 0; i<this.itemCollector.getInventory().getSize(); i++){
						Item item = this.itemCollector.getInventory().getItem(i);
						if(item!=null && this.player.getInventory().canAdd(item.getItemType())){
							player.getInventory().addItem(item);
							this.itemCollector.getInventory().removeItem(item);
						}
					}
					pathFinder.setTarget(null);
				}
			}else if(!this.pathFinder.hasTarget() || !this.pathFinder.getBlockTarget().isEqual(this.player.getBlockLocation()))this.pathFinder.setBlockTarget(player.getBlockLocation());
		}else{
			if(targetTrigger.isTriggered()){
				nextTarget();
				currentDroneTarget = null;
			}
		}
	}
	
	private DroneTarget getNextDroneTarget() {
//		System.out.println("---> NEXT <---");	
		int distance = -1;
		DroneTarget next = null;
		for(DroneTarget target: targets.values()){
			int d = getDistance(target, false);
			if(distance == -1 || d < distance){
				boolean canReach = canReach(target.getLocation()); 
				if(canReach){
//					System.out.println(d);
					distance = d;
					next = target;
				}
			}
		}
		return next;
	}

	private int getDistance(DroneTarget target, boolean quest) {
//		System.out.print("---> "+quest+" >>> ");
//		if(quest)System.out.println(this.getLocation().toString() + "->" +this.getBlockLocation().toString()+"->"+this.getWidth() +"|"+this.getHeight());
//		System.out.println(this.hitbox.toString() + " -> " + this.images[0].getImage().getLocation().toString());
		int x = Math.abs(Map.getMap().getXOver(this.getX()+this.getWidth() /2) - Map.getMap().getXOver(target.getLocation().getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));
		if(x>Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)x-=Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE;
		int y = Math.abs(					   this.getY()+this.getHeight()/2  - 					  (target.getLocation().getY()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2));
//		System.out.println(Map.getMap().getXOver(this.getX()+this.getWidth ()/2) + " - " + Map.getMap().getXOver(target.getLocation().getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2) + " = " + x);
//		System.out.println(                     (this.getY()+this.getHeight()/2) + " - " +                      (target.getLocation().getY()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2) + " = " + y);
//		System.out.println(x+"->"+y);
		return (int) Math.sqrt(x*x+y*y);
	}

	private void nextTarget() {
		int x = Map.getMap().getXOver((int)Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX());
		int y = 					  (int)Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY() ;
		int dx = Math.abs(x-player.getLocation().getX()); 
		int dy = Math.abs(y-player.getLocation().getY());
		
//		System.out.println(x+"|"+y+"->"+dx+"|"+dy);
		
		int d = dx*dx+dy*dy;		
		if(d>maxDistance*maxDistance){
			Location MouseOff = Mouse.getMouse().getOff();
			
			double m = (double)MouseOff.getY()/(Math.abs(MouseOff.getX())+1.0);
			m*=m;
			double w = maxDistance*maxDistance;
			
			double ax = Math.sqrt((w)/(m+1.0));	
			y = (int) Math.round(Math.sqrt(m)*ax);
			x = (int) ax;
			if(MouseOff.getX()>0)x*=-1;
			if(MouseOff.getY()>0)y*=-1;
			else if(x==0)y=(int) maxDistance;
			
			x = (int)(screenCenter.getX() + this.getWidth() /2 + x); 
			y = (int)(screenCenter.getY() + this.getHeight()/2 + y);
			
//			System.out.println(x+"|"+y);
			
			x+=Map.getMap().getMoved().getX();
			y+=Map.getMap().getMoved().getY();				
		}
		
//		System.out.println(x+"|"+y);
		
		this.pathFinder.setTarget(new Location(Map.getMap().getXOver(x),y));
	}

	public void addInterActionLocation(int resID, Location location) {
		int key = location.getX()+location.getY()*Map.getMap().getWidth();
		if(targets.containsKey(key)){
			if(targets.get(key).getID() == resID) return;
		}
		targets.put(location.getX()+location.getY()*Map.getMap().getWidth(), new DroneTarget(location, resID).createVisuals());
	}

	public void removeTarget(Location loc) {
		int key = loc.getX()+loc.getY()*Map.getMap().getWidth();
		if(targets.containsKey(key)){
			this.targets.get(key).destroyVisulas();
			this.targets.remove(key);
		}
		if(currentDroneTarget!=null && this.currentDroneTarget.getLocation().equals(loc)){
			this.currentDroneTarget=null;
			this.pathFinder.setTarget(null);
		}
	}

	@Override
	public Inventory getInventory() {
		return itemCollector.getInventory();
	}


	@Override
	public int getLightDistance() {
		LightEntityData lightdata = (LightEntityData)EntityType.Drone.getData(EntityData.LIGHTENTITYDATA);
		return lightdata.getLightDistance();
	}

	@Override
	public double getLightStrength() {
		LightEntityData lightdata = (LightEntityData)EntityType.Drone.getData(EntityData.LIGHTENTITYDATA);
		return lightdata.getLightStrength();
	}
}
