package anim;

import java.util.ArrayList;
import java.util.HashMap;

import Data.Animation.Animation;
import Data.Animation.AnimationManager;
import Data.Events.Action;
import Data.Image.Image;
import data.Resource;

public class WaterAnimation extends Animation{
	
	public static HashMap<Resource, ArrayList<WaterAnimation>> Data = new HashMap<>();
	
	
	private Resource res;

	public WaterAnimation(Boolean newImage, Double duration, Integer layer, Image Image, Resource res) {
		super(newImage, duration, layer, Image, res.getSpriteIDs());
		
		this.res = res;
		
//		start();
		
		this.setAction(new Action() {
			@Override
			public void act(Object caller, Object... data) {
				if(index >= spriteIDs.length)index = 0;
				
				ArrayList<WaterAnimation> animations = Data.get(res);
				Image.setSpriteState(index);
				
//				System.out.println("anim->"+index);
				
				for(int i = 0; i<animations.size(); i++){
					if(animations.get(i).res.getID()!=res.getID())System.out.println("Error");
					animations.get(i).setImage(index);
				}
				index++;
			}
		});
		
	}
	
	@Override
	public boolean tick(){
		time += System.currentTimeMillis()-lastTime;
		lastTime = System.currentTimeMillis();
		if(duration<=time){
			anim.act(this);
			time-=duration;
			return true;
		}
		return false;
	}
	
	@Override
	public Animation start(){
		if(!Data.containsKey(res)) Data.put(res, new ArrayList<>());
//		System.out.println(Data.get(res).size());
		if(Data.get(res).size()==0){
//			System.out.println("REGISTER");
			this.id = AnimationManager.register(this);
		}else if(Data.get(res).size()>0) AnimationManager.remove(this);
		Data.get(res).add(this);
		active = true;
		lastTime = System.currentTimeMillis();
		return this;
	}
	
	@Override
	public void setIds(int... ids){
		if(Data.containsKey(res) && Data.get(res).size()>0){
			Data.get(res).get(0).setIdsIndependent(ids);
		}
		index = 0;
		spriteIDs = ids;
	}
	
	private void setIdsIndependent(int[] ids){
		index = 0;
		spriteIDs = ids;		
	}
	
	@Override
	public void stop(){
//		System.out.println("Stop");
		if(Data.containsKey(res) && Data.get(res).size()>0){
//			System.out.println(this+" -> "+Data.get(res).get(0));
			if(Data.get(res).get(0).equals(this) && Data.get(res).size()>1){
				while(Data.get(res).size()>1 && Data.get(res).get(1).equals(this)){
					Data.get(res).remove(1);
				}
				if(Data.get(res).size()>1)Data.get(res).get(1).id = AnimationManager.register(Data.get(res).get(1));
//				System.out.println("Register -> "+Data.get(res).get(1));
			}
			Data.get(res).remove(this);
			AnimationManager.remove(this);
		}
	//	active = false;
		image.setSpriteState(res.getSpriteIDs()[0]);
	}

	@Override
	public void setImage(int index) {
		if(Data.containsKey(res) && Data.get(res).size()>0){
			Data.get(res).get(0).index = index;
		}
		image.setSpriteState(spriteIDs[index]);
	}

}
