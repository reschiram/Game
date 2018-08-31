package game.overlay;

import java.awt.Color;
import java.awt.Dimension;

import Data.DataObject;
import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Engine.Engine;
import data.map.Lamp;

public class DayManager {
		
	private static DayManager DAYMANAGER;
	public static DayManager getDayManager(){
		return DAYMANAGER;
	}
	
	private int tickToTime = 8000;
	private Time time;
	
	private int layer = 0;	
	private StringRenderOperation go;
	
	private int currentDayLightLevel = Lamp.DEFAULT_LIGHT_STATES-2;
	private DataObject<Integer> currentDayLightLevelData = new DataObject<Integer>(currentDayLightLevel);
	
	public DayManager(){
		System.out.println("new DayManager");
		DAYMANAGER = this; 
		this.time = new Time(12,0,0);
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
	
	public void tick(){
		this.time.addMilliSeconds(tickToTime);
		go.setText("Time: "+this.time.getTime());
		int newDayLightLevel = getSunLightLevel();
		if(currentDayLightLevel!=newDayLightLevel){
			System.out.println(newDayLightLevel);
			this.currentDayLightLevel = newDayLightLevel;
			this.currentDayLightLevelData.setData(newDayLightLevel);
		}
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
	
	public DataObject<Integer> getDayLightLevelData(){
		return this.currentDayLightLevelData;
	}
	
	public int getDayLightLevel(){
		return this.currentDayLightLevel;
	}

}
