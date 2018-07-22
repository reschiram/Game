package anim;

import java.awt.Color;

import Data.Animation.Animation;
import Data.Events.Action;
import Data.Image.Image;

public class ItemAnimation extends Animation{

	public ItemAnimation(Boolean newImage, Double duration, Integer layer, Image I, Integer[] spriteIds) {
		super(newImage, duration, layer, I, getSpriteIds(spriteIds));
		
//		Color c = new Color(I.getImage().getRGB(0, 0), true);
//		System.out.println(c.getRed() +"|"+ c.getGreen() +"|"+ c.getBlue() +"|"+ c.getAlpha());
		
		this.setAction(new Action() {
			
			int moved     =  0;
			int maxMoved  = 20;
			int direction =  1;
			
			long lastmoved = System.currentTimeMillis();
			
			@Override
			public void act(Object caller, Object... data) {
//				System.out.println(moved);
				if(System.currentTimeMillis()-lastmoved> duration){
//					System.out.println(I.disabled);
//					Color c = new Color(I.getImage().getRGB(0, 0), true);
//					System.out.println(c.getRed() +"|"+ c.getGreen() +"|"+ c.getBlue() +"|"+ c.getAlpha());
//					System.out.println(I.getX()+"|"+I.getY()+"|"+direction);
					lastmoved = System.currentTimeMillis();
					if(direction>0){
						if(moved>=maxMoved)direction=-1;
					}else if(direction<0){
						if(moved<=0)direction=1;
					}
					I.setLocation(I.getX(),  I.getY()+direction);
					moved+=direction;
				}
			}
		});
	}

	private static int[] getSpriteIds(Integer[] spriteIds) {
		int[] ids = new int[spriteIds.length];
		for(int i = 0; i<ids.length; i++)ids[i] = spriteIds[i];
		return ids;
	}

}
