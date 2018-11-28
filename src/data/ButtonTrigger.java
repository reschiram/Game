package data;

import Engine.Engine;
import game.tick.TickManager;

public class ButtonTrigger {
	
	public static ButtonTrigger WheelTrigger = new ButtonTrigger(-1);
	
	private int keyCode;
	private boolean isPressed = false;	
	private boolean isTriggered = false;
	
	public ButtonTrigger(int keyCode){
		this.keyCode = keyCode;
	}
	
	public void tick(){
		if(ButtonPressed()){
			if(!isTriggered && !isPressed)isTriggered = true;
			isPressed = true;
		}else{
			isPressed = false;
		}
	}
	
	private boolean ButtonPressed() {
		if(keyCode>0){
			if(Engine.getInputManager().getKeyDown().contains(keyCode) || Engine.getInputManager().getMouseButton().contains(keyCode))return true;
		}else{
			if(keyCode == -1){
				if(Engine.getInputManager().getMouseWheelsMove((long) (TickManager.getTickDuration()*(1.5+TickManager.getLatency())))!=0)return true;
			}
		}
		return false;
	}

	public boolean isTriggered(){
		boolean triggered = isTriggered==true;
		isTriggered = false;
		return triggered;
	}

	public int getKey() {
		return keyCode;
	}

}
