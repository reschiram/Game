package anim;

import java.lang.reflect.InvocationTargetException;

import Data.Animation.Animation;
import Data.Image.Image;
import data.Resource;

public class AnimationType {
	
	public static AnimationType ITEM;
	public static AnimationType NORMAL;
	public static AnimationType CANCEL;
	public static AnimationType WATER;
	public static AnimationType	NONE;

	public static AnimationType create() {
		NORMAL 	= new AnimationType(1000, "Normal", NormalAnimation.class);
		CANCEL 	= new AnimationType( 100, "Cancel", CancelAnimation.class);
		WATER 	= new AnimationType( 100, "Water" , WaterAnimation.class );
		NONE 	= new AnimationType( 000, "None"  , null				 );	
		ITEM 	= new AnimationType(  10, "None"  , ItemAnimation.class  );			
		return null;
	}
	
	
	private String type;
	private Class<?> animationClass;
	private double duration;
	
	private AnimationType(double duration, String type, Class<?> animationClass){
		this.type = type;
		this.animationClass = animationClass;
		this.duration = duration;
	}
	
	public String getType(){
		return type;
	}
	
	public static AnimationType getAnimationType(String type){
		if(type.equals(NONE.type))       return NONE;
		else if(type.equals(WATER.type)) return WATER;
		return null;
	}
	
	public Animation newAnimation(boolean newImage, int layer, Image image, Resource... resource){
		if(this.animationClass!=null){
			try {
				if(resource.length>0) return (Animation) this.animationClass.getDeclaredConstructor(Boolean.class, Double.class, Integer.class, Image.class, Resource.class).newInstance(newImage, this.duration, layer, image, resource[0]);
				else return (Animation) this.animationClass.getDeclaredConstructor(Boolean.class, Double.class, Integer.class, Image.class).newInstance(newImage, this.duration, layer, image);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Animation newAnimation(boolean newImage, int layer, Image image, Integer... ids){
		if(this.animationClass!=null){
			try {
				return (Animation) this.animationClass.getDeclaredConstructor(Boolean.class, Double.class, Integer.class, Image.class, Integer[].class).newInstance(newImage, this.duration, layer, image, ids);
//				else return (Animation) this.animationClass.getDeclaredConstructor(Boolean.class, Double.class, Integer.class, Image.class).newInstance(newImage, this.duration, layer, image);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


}
