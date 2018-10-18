package data.map;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Data.Image.Image;
import Data.Image.SpriteSheet;
import data.MapResource;
import game.gridData.map.Mapdata;
import game.map.Map;
import sprites.Sprites;

public class EdgeBlockData extends UpdatableBlockData{
	
	private SpriteSheet sheet;
	private Color[] colors = new Color[4];
	
	private int resource;
	
	public EdgeBlockData(String name, Color color1, Color color2, Color color3, Color color4) {
		super(name);
		colors[0] = color1;
		colors[1] = color2;
		colors[2] = color3;
		colors[3] = color4;
	}
	
	@Override
	public void loadData(MapResource resource){
		SpriteSheet edges = Sprites.Edges.getSpriteSheet();
		BufferedImage[] bis = new BufferedImage[edges.getSpriteAmount()];
		int[] ids = new int[edges.getSpriteAmount()];
		
		for(int i = 0; i<edges.getSpriteAmount(); i++){
			BufferedImage bi = Image.cloneImage(resource.getSprites().getSprite(resource.getSpriteIDs()[0]).getImage(0));
			BufferedImage edge = edges.getSprite(i).getImage(0);
			for(int x = 0; x<edge.getWidth(); x++){
				for(int y = 0; y<edge.getHeight(); y++){
					Color edgeC = new Color(edge.getRGB(x,y), true);
					if(edgeC.getAlpha()==255){
//						System.out.println(edgeC.getBlue()+"|"+edgeC.getRed()+"|"+edgeC.getRed()+" -> "+);
						if(edgeC.getBlue()+edgeC.getRed()+edgeC.getRed()==169*3){
//							System.out.println("Render");
							bi.setRGB(x, y, colors[0].getRGB());
						}else if(edgeC.getBlue()+edgeC.getRed()+edgeC.getGreen()==123*3){
//							System.out.println("Render");
							bi.setRGB(x, y, colors[1].getRGB());
						}else if(edgeC.getBlue()+edgeC.getRed()+edgeC.getGreen()== 97*3){
//							System.out.println("Render");
							bi.setRGB(x, y, colors[2].getRGB());
						}else if(edgeC.getBlue()+edgeC.getRed()+edgeC.getGreen()== 76*3){
//							System.out.println("Render");
							bi.setRGB(x, y, colors[3].getRGB());
						}
					}
				}
			}
			bis[i] = bi;
			ids[i] = i;
		}
		this.sheet = new SpriteSheet(bis);
		resource.setSprites(this.sheet, ids);
		
		this.resource = resource.getID();
	}

	@Override
	public void update(Mapdata data, int mx, int y) {
		
		int x = Map.getMap().getXOver((mx-1)*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE;
		int state = 0;
		Mapdata currentData = Map.getMap().getChunk(x,y).getMapData(x, y  , false)[0];
		if(currentData!=null && currentData.getResource().getID()==this.resource)state +=  1;
//		if(Map.getMap().getChunks()[x/Map.DEFAULT_CHUNKSIZE][(y-1)/Map.DEFAULT_CHUNKSIZE].getMapData(x, y-1, false)[0]!=null)state +=  2;
//		if(Map.getMap().getChunks()[x/Map.DEFAULT_CHUNKSIZE][(y+1)/Map.DEFAULT_CHUNKSIZE].getMapData(x, y+1, false)[0]!=null)state +=  4;
		
		x 	 = Map.getMap().getXOver((mx+1)*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE;
		currentData = Map.getMap().getChunk(x,y).getMapData(x, y  , false)[0];
		if(currentData!=null && currentData.getResource().getID()==this.resource)state +=  10;
//		if(Map.getMap().getChunks()[x/Map.DEFAULT_CHUNKSIZE][(y-1)/Map.DEFAULT_CHUNKSIZE].getMapData(x, y-1, false)[0]!=null)state +=  20;
//		if(Map.getMap().getChunks()[x/Map.DEFAULT_CHUNKSIZE][(y+1)/Map.DEFAULT_CHUNKSIZE].getMapData(x, y+1, false)[0]!=null)state +=  40;
		
		x = mx;
		y+=1;
		if(y<Map.getMap().getHeight()){
			currentData = Map.getMap().getChunk(x,y).getMapData(x, y  , false)[0];
			if(currentData!=null && currentData.getResource().getID()==this.resource)state += 100;		
		}
		y-=2;
		if(y>=0){
			currentData = Map.getMap().getChunk(x,y).getMapData(x, y  , false)[0];
			if(currentData!=null && currentData.getResource().getID()==this.resource)state +=1000;
		}
		
		if(state<10){
			     if(state ==   0)data.setSpriteState(23);
			else if(state ==   1)data.setSpriteState( 5);
		}else if(state <  100){
			     if(state ==  10)data.setSpriteState( 4);
			else if(state ==  11)data.setSpriteState(14);
		}else if(state < 1000){
			     if(state == 100)data.setSpriteState( 3);
			else if(state == 101)data.setSpriteState( 2);
			else if(state == 110)data.setSpriteState( 0);
			else if(state == 111)data.setSpriteState( 1);
		}else{
			     if(state ==1000)data.setSpriteState(13);
			else if(state ==1001)data.setSpriteState(22);
			else if(state ==1010)data.setSpriteState(20);
			else if(state ==1011)data.setSpriteState(21);
			else if(state ==1100)data.setSpriteState(15);
			else if(state ==1101)data.setSpriteState(12);
			else if(state ==1110)data.setSpriteState(10);
			else if(state ==1111)data.setSpriteState(11);
		}
		
//		System.out.println(x+"|"+y+"->"+state);
	}

}
