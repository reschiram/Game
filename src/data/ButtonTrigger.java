package data;

import Engine.Engine;

public class ButtonTrigger {
	
	private int keyCode;
	private boolean isPressed = false;
	
	private boolean isTriggered = false;
	
	public ButtonTrigger(int keyCode){
		this.keyCode = keyCode;
	}
	
	public void tick(){
//		System.out.println(Engine.getInputManager().getKeyDown().contains(keyCode) +"|"+ Engine.getInputManager().getMouseButton().contains(keyCode));
		if(Engine.getInputManager().getKeyDown().contains(keyCode) || Engine.getInputManager().getMouseButton().contains(keyCode)){
			if(!isTriggered && !isPressed)isTriggered = true;
			isPressed = true;
		}else{
			isPressed = false;
		}
	}
	
	public boolean isTriggered(){
		boolean triggered = isTriggered==true;
		isTriggered = false;
		return triggered;
	}

}
