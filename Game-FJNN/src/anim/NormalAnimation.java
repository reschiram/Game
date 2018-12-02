package anim;

import Data.Animation.Animation;
import Data.Image.Image;
import data.Resource;

public class NormalAnimation extends Animation{

	public NormalAnimation(boolean newImage, double duration, int layer, Image I, Resource res) {
		super(newImage, duration, layer, I, res.getSpriteIDs());
		// TODO Auto-generated constructor stub
	}

}
