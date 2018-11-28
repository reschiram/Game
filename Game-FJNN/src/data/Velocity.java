package data;

public class Velocity {

	private double xSpeed;
	private double ySpeed;
	
	public Velocity(){
		
	}
	
	public Velocity(int xSpeed, int ySpeed){
		setVelocity(xSpeed, ySpeed);
	}

	private void setVelocity(int xSpeed, int ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	public int getXSpeed() {
		return (int) xSpeed;
	}

	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getYSpeed() {
		return (int) ySpeed;
	}

	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	public void accelerate(double x, double y){
		xSpeed = x + xSpeed;
		ySpeed = y + ySpeed;
	}
	
	@Override
	public String toString(){
		return xSpeed + " | " + ySpeed;
	}
}
