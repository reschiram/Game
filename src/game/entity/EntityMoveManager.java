package game.entity;

import Data.Direction;
import Data.Location;
import data.Velocity;
import game.gridData.map.Mapdata;
import game.map.Map;

public class EntityMoveManager {
	
	private static int MAX_Y_SPEED = 20;
	
	private Entity entity;
	
	private Velocity velocity;
	private int jump = 7;
	
	private double moveX = 0.0;
	private boolean slowDown = false;
	
	private boolean accelerateX = true;
	private boolean accelerateY = true;
	
	public EntityMoveManager(Entity entity){
		this.entity = entity;
		velocity = new Velocity();
	}
	
	public void tick(){
		if(!isOnGround()){
			if(this.accelerateY){
				this.accelerate(0, Map.getMap().getAcceleration());
			}
		}
		if(this.moveX != 0 && this.accelerateX){
			if(this.slowDown && this.velocity.getXSpeed() == 0){
				this.slowDown = false;
				this.moveX = 0;
			}else this.accelerate(this.moveX, 0);
		}
		
		
		int move = 0;
		if((move = canMoveX()) != 0){
			if(!this.accelerateX)this.velocity.setXSpeed(0);
			this.entity.setLocation(this.entity.getX() + move, this.entity.getY()	    );
		}else{
			if(this.velocity.getXSpeed()!=0)this.velocity.setXSpeed(0);
		}
		
		if((move = canMoveY()) != 0){
			if(!this.accelerateY)this.velocity.setYSpeed(0);
			this.entity.setLocation(this.entity.getX()		 , this.entity.getY() + move);			
		}else{
			if(this.velocity.getYSpeed()!=0) this.velocity.setYSpeed(0);
		}
	}
	
	public void move(Direction d){
		if(d.getX()!=0){
			if(!this.accelerateX){
				this.velocity.setXSpeed(entity.getMaxSpeed()*-d.getX());
			}else{
				this.moveX = -d.getX()*0.2;
				this.slowDown = false;
			}
		}
		if(this.accelerateY){
			if(d.getY()!=0 && isOnGround()){
				if(d.equals(Direction.UP))this.velocity.setYSpeed(-jump);
			}
		}else this.velocity.setYSpeed(entity.getMaxSpeed()*-d.getY());
	}

	public void slowDownXVelocity() {
		if(moveX != 0 && !slowDown){
			slowDown = true;
			moveX = -moveX;
		}
	}

	private boolean isOnGround() {
		for(int x = entity.getX(); x <= entity.getX()+entity.getWidth(); x+=Math.min(Map.DEFAULT_SQUARESIZE-1, entity.getWidth()-1)){
			int y = entity.getY()+entity.getHeight();
			Mapdata[] data = Map.getMap().getMapData(new Location(Map.getMap().getBlockXOver(x/Map.DEFAULT_SQUARESIZE), y/Map.DEFAULT_SQUARESIZE));
			if(data[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER] != null && !data[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER].canHost(entity.getWidth(), entity.getHeight()))return true;
		}
		return false;
	}

	public void accelerate(double x, double y){
		this.velocity.accelerate(x, y);
		if(Math.abs(this.velocity.getXSpeed()) > Math.abs(entity.getMaxSpeed())){
			if(this.velocity.getXSpeed()>0)this.velocity.setXSpeed(entity.getMaxSpeed());	
			else this.velocity.setXSpeed(-entity.getMaxSpeed());
		}
		if(Math.abs(this.velocity.getYSpeed()) > Math.abs(MAX_Y_SPEED)){
			if(this.velocity.getYSpeed()>0)this.velocity.setYSpeed(MAX_Y_SPEED);	
			else this.velocity.setYSpeed(-MAX_Y_SPEED);
		}
	}
	
	public int canMoveX() {
		if(this.velocity.getXSpeed() == 0)return 0;
		int add = Map.DEFAULT_SQUARESIZE-1;
		if(this.entity.getHeight()<add)add = this.entity.getHeight()-1;
		
		int XOff = this.velocity.getXSpeed();
		for(int y = entity.getY(); y <= entity.getY()+entity.getHeight(); y+=add){
			int x = entity.getX()+this.velocity.getXSpeed();
			if(this.velocity.getXSpeed()>0)x+=entity.getWidth()-1;
			x=Map.getMap().getXOver(x);
			Mapdata data = Map.getMap().getMapData(new Location(x/Map.DEFAULT_SQUARESIZE, y/Map.DEFAULT_SQUARESIZE))[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER];
			if(data != null && !data.canHost(entity.getWidth(), entity.getHeight())){
				int dataX = Map.getMap().getXOver(data.getLocation().getX()*Map.DEFAULT_SQUARESIZE);
				if(this.velocity.getXSpeed()<0)dataX=Map.getMap().getXOver(dataX+Map.DEFAULT_SQUARESIZE-1);
				int dx = dataX-(x-this.velocity.getXSpeed());
				if(dx>0)dx--;
				else dx++;
				if(Math.abs(XOff)>Math.abs(dx))XOff = dx;
			}
		}
		return XOff;
	}
	
	public int canMoveY() {
		if(this.velocity.getYSpeed() == 0)return 0;
		int add = Map.DEFAULT_SQUARESIZE-1;
		if(this.entity.getWidth()<add)add = this.entity.getWidth()-1;
		
		int YOff = this.velocity.getYSpeed();
		for(int x = entity.getX(); x <= entity.getX()+entity.getWidth(); x+=add){
			int y = entity.getY()+this.velocity.getYSpeed();
			if(this.velocity.getYSpeed()>0)y+=entity.getHeight()-1;
			Mapdata data = Map.getMap().getMapData(new Location(Map.getMap().getBlockXOver(x/Map.DEFAULT_SQUARESIZE), y/Map.DEFAULT_SQUARESIZE))[Entity.DEFAULT_ENTITY_UP+Map.DEFAULT_BUILDLAYER];
			if(data != null && !data.canHost(entity.getWidth(), entity.getHeight())){
				int dataY = data.getLocation().getY()*Map.DEFAULT_SQUARESIZE;
				if(this.velocity.getYSpeed()<0)dataY+=Map.DEFAULT_SQUARESIZE-1;
				int dy = dataY-(y-this.velocity.getYSpeed());
				if(dy>0)dy--;
				else dy++;
				if(Math.abs(YOff)>Math.abs(dy))YOff = dy;
			}
		}
		return YOff;
	}	
	
	public void setDoAccelerateXVelocity(boolean accelerate){
		if(accelerate){
			this.accelerateX = true;
		}else{
			this.accelerateX = false;
			this.slowDown = false;
			this.moveX = 0.0;
			if(this.velocity.getXSpeed() > 0)this.velocity.setXSpeed(entity.getMaxSpeed());
			else if(this.velocity.getXSpeed() < 0)this.velocity.setXSpeed(-entity.getMaxSpeed());
		}
	}
	
	public void setDoAccelerateYVelocity(boolean accelerate){
		if(accelerate){
			this.accelerateY = true;
		}else{
			this.accelerateY = false;
			if(this.velocity.getYSpeed() > 0)this.velocity.setYSpeed(entity.getMaxSpeed());
			else if(this.velocity.getYSpeed() < 0)this.velocity.setYSpeed(-entity.getMaxSpeed());
		}
	} 

}
