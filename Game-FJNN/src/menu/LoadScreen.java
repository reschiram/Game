package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Data.Location;
import Data.Image.Image;
import Data.Image.SpriteSheet;
import Engine.Engine;

public class LoadScreen {
	
	private double progress = 0.0;
	
	private Image background;
	private Image progresssBar;
	private int layer;
	private boolean created = false;
	
	public LoadScreen(int layer){
		this.layer = layer;
		
		BufferedImage bi = Image.createColorImage(1920, 1080, Color.black);
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.WHITE);
		g.drawString("LOADING", (1920-60)/2, 1080/2-80);

		Dimension progressBarDim = new Dimension(600,100);
		Location  progressBarLoc = new Location((1920-progressBarDim.width)/2, (1080-progressBarDim.height)/2);
		
		g.setColor(Color.GRAY);
		g.drawRect(progressBarLoc.getX()-1, progressBarLoc.getY()-1, progressBarDim.width+2, progressBarDim.height+1);
		
		this.background = new Image(new Location(0,0), new Dimension(bi.getWidth(), bi.getHeight()), "", new SpriteSheet(bi), null);
		
		this.progresssBar = new Image(progressBarLoc, new Dimension(1, (int)progressBarDim.getHeight()), "", new SpriteSheet(Image.createColorImage(progressBarDim.width, progressBarDim.height, Color.GREEN)), null);
	}
	
	public LoadScreen create(){
		if(!created){
			Engine.getEngine(this, this.getClass()).addImage(background, layer);
			Engine.getEngine(this, this.getClass()).addImage(progresssBar, layer);
			
			created = true;
		}
		return this;
	}
	
	public void setProgress(double progress){
		this.progress = progress;
		this.progresssBar.setDimension((int)(600*progress), 100);
	}
	
	public double getProgress(){
		return progress;
	}
	
	public void show(){
		if(!created)create();
		else{
			background.disabled = false;
			progresssBar.disabled = false;
		}
	}
	
	public void hide(){
		background.disabled = true;
		progresssBar.disabled = true;		
	}
	
	public void destroyVisuals(){
		Engine.getEngine(this, this.getClass()).removeImage(layer, background);
		Engine.getEngine(this, this.getClass()).removeImage(layer, progresssBar);
	}

}
