package game.menu;

import java.awt.Dimension;

import Data.Location;
import Data.Image.Image;
import Engine.Engine;
import galaxy.SolarSystem;
import galaxy.SpaceObject;

public class SolarSystemMenu {
	
	private static double SIZE = 10;
	private static int PlanetLayer = 1;	
	
	private SolarSystem solarSytem;
	
	private Image[] planetImages;
	private boolean created = false;
	private Image Background;
	
	public SolarSystemMenu(SolarSystem solarSystem){
		
		this.solarSytem = solarSystem;		
		this.planetImages = new Image[solarSystem.getPlanets().size()+1];
		
		this.planetImages[0] = createImage(solarSystem.getSun());
		for(int i = 0; i<solarSystem.getPlanets().size(); i++){
			this.planetImages[i+1] = createImage(solarSystem.getPlanets().get(i));
		}
	}

	private Image createImage(SpaceObject so) {
		Dimension size = new Dimension((int)(so.getDiameter()*SIZE), (int)(so.getDiameter()*SIZE));
		Location loc = new Location((int)( (so.getDistanceToCenter()*SIZE) - (size.getWidth()/2) ), (int)( (so.getDistanceToCenter()*SIZE) - (size.getHeight()/2) ));
		Image image = new Image(loc, size, "", so.getSpriteSheet(), null);
		image.setSpriteState(so.getSpriteState());
		return image;
	}
	
	public SolarSystemMenu create(){
		if(!created){
			for(int i = 0; i<this.planetImages.length; i++)Engine.getEngine(this, this.getClass()).addImage(planetImages[i], PlanetLayer);
			if(Background!=null)Engine.getEngine(this, this.getClass()).addImage(Background, PlanetLayer);
			
			created = true;
		}
		return this;
	}
	
	public void show(){
		if(!created){
			create();
		}else{
			for(int i = 0; i<this.planetImages.length; i++)planetImages[i].disabled = false;
			if(Background!=null)Background.disabled = false;			
		}
	}
	
	public void hide(){
		for(int i = 0; i<this.planetImages.length; i++)planetImages[i].disabled = true;
		if(Background!=null)Background.disabled = true;
	}

}
