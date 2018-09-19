package game.overlay;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import Data.DataObject;
import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Engine.Engine;
import data.map.Lamp;
import game.Game;

public class DayManager {
		
	private static DayManager DAYMANAGER;
	public static DayManager getDayManager(){
		return DAYMANAGER;
	}
	
	private int tickToTime = 80;
	private Time time;
	
	private int layer = 0;	
	private StringRenderOperation go;
	
	private ArrayList<DataObject<Integer>> currentDayLightLevelData = new ArrayList<DataObject<Integer>>();
	
	public DayManager(){
		System.out.println("new DayManager");
		updateCurrentDayLightLevel(Lamp.DEFAULT_SURFACE_LEVELS-1);
		DAYMANAGER = this; 
		this.time = new Time(12,0,0);
	}
	
	private void updateCurrentDayLightLevel(int newLightLevel) {
		for(int i = 0; i<Lamp.DEFAULT_SURFACE_LEVELS; i++){
			if(currentDayLightLevelData.size()>i){
				this.currentDayLightLevelData.get(i).setData((int) Math.ceil(newLightLevel*((double)i/(double)(Lamp.DEFAULT_SURFACE_LEVELS-1))));
			}else{
				this.currentDayLightLevelData.add(new DataObject<Integer>((int) Math.ceil(newLightLevel*((double)i/(double)Lamp.DEFAULT_SURFACE_LEVELS))));
			}
		}
	}

	public void createTime(int layer, Location loc){
		go = new StringRenderOperation(loc, new Dimension(100,  50), "Time: "+this.time.getTime(), null, Color.WHITE);
		Engine.getEngine(this, this.getClass()).addGraphicOperation(go, layer);
		this.layer = layer;
	}
	
	public void hideTime(){
		go.disabled = true;
	}

	public void showTime(){
		go.disabled = false;
	}
	
	public void destroyTime(){
		Engine.getEngine(this, this.getClass()).removeGraphicOperation(go, layer);
	}
	
	int changed = 0;
	public void tick(){
		this.time.addMilliSeconds(tickToTime);
		go.setText("Time: "+this.time.getTime());
		int newDayLightLevel = getSunLightLevel();
		if(changed < 10 ||currentDayLightLevelData.get(currentDayLightLevelData.size()-1).getData()!=newDayLightLevel){
			updateCurrentDayLightLevel(newDayLightLevel);
			Game.getLightOverlay().updateComplete();
		}
		changed++;
	}
	
	private int getSunLightLevel(){
		int max = Lamp.DEFAULT_LIGHT_STATES-1;
		double x = time.getMilliSecond()/(1000.0*60.0*60.0);
		double a = -0.00009*Math.pow(x, 3);
		double b = 0.001098692*Math.pow(x, 2);
		double c = 0.025471385*x;
		double f = ((a+b+c)*(1.0/0.35))+0.05;
		return (int) Math.ceil(max*f);
	}
	
	public DataObject<Integer> getDayLightLevelData(int state){
		return this.currentDayLightLevelData.get(state);
	}
	
	public int getDayLightLevel(int state){
		return this.currentDayLightLevelData.get(state).getData();
	}

}
