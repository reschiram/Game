package anim;

import Data.Animation.Animation;
import Data.Animation.AnimationManager;
import Data.Events.Action;
import Data.Image.Image;
import data.Resource;

public class CancelAnimation extends Animation{

	public CancelAnimation(Boolean newImage, Double duration, Integer layer, Image I, Resource res) {
		super(newImage, duration, layer, I, res.getSpriteIDs());
		
		setAction(new Action() {
			
			@Override
			public void act(Object caller, Object... data) {
//				System.out.println(index);
				if(index>=spriteIDs.length){
					image.disabled = true;
					AnimationManager.remove(getAnimation());
					return;
				}
				image.setSpriteState(spriteIDs[index]);
				index++;				
			}
		});
	}

	protected Animation getAnimation() {
		return this;
	}
	
	@Override
	public Animation start(){
		active = true;
		image.disabled = false;
		if(index>=spriteIDs.length)AnimationManager.register(getAnimation());
		index = 0;
		return this;
	}

}
