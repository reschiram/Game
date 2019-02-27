package data.map;

import data.MapResource;
import game.gridData.map.Mapdata;
import game.map.Map;

public class Grass extends UpdatableBlockData{

	public Grass(String name) {
		super(name);
	}
	
	MapResource res;
	
	@Override
	public void update(Mapdata data, int mx, int y){
//		System.out.print("update:"+mx+"|"+y+"->");
//		if(res.isGround())System.out.println("update");
		
		int x = Map.getMap().getXOver((mx-1)*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE;
		int state = 0;
		Mapdata currentData = Map.getMap().getMapData(x, y)[res.isGround() ? Map.DEFAULT_GROUNDLAYER : Map.DEFAULT_BUILDLAYER];
		if(currentData!=null && (!res.isGround() || currentData.getResource().getID()!=MapResource.Air_Background.getID()))state += 1;
		currentData = Map.getMap().getMapData(x, y-1)[res.isGround() ? Map.DEFAULT_GROUNDLAYER : Map.DEFAULT_BUILDLAYER];
		if(currentData!=null && (!res.isGround() || currentData.getResource().getID()!=MapResource.Air_Background.getID()))state += 2;
		
		x 	 = Map.getMap().getXOver((mx+1)*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE;
		currentData = Map.getMap().getMapData(x, y)[res.isGround() ? Map.DEFAULT_GROUNDLAYER : Map.DEFAULT_BUILDLAYER];
		if(currentData!=null && (!res.isGround() || currentData.getResource().getID()!=MapResource.Air_Background.getID()))state += 10;
		currentData = Map.getMap().getMapData(x, y-1)[res.isGround() ? Map.DEFAULT_GROUNDLAYER : Map.DEFAULT_BUILDLAYER];
		if(currentData!=null && (!res.isGround() || currentData.getResource().getID()!=MapResource.Air_Background.getID()))state += 20;
		
		if     (state== 0)data.setSpriteState(3);
		else if(state== 1)data.setSpriteState(2);
		else if(state== 3)data.setSpriteState(6);
		else if(state==10)data.setSpriteState(1);
		else if(state==11)data.setSpriteState(0);
		else if(state==13)data.setSpriteState(4);
		else if(state==30)data.setSpriteState(7);
		else if(state==31)data.setSpriteState(5);
		else if(state==33)data.setSpriteState(8);
	}
	
	@Override
	public void loadData(MapResource res){
		this.res = res;
	}

}
