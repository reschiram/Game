package game.overlay;

import java.util.ArrayList;

import Data.DataObject;
import Data.Direction;
import Data.Location;
import Data.Queue;
import data.map.Lamp;
import game.GameManager;
import game.entity.Entity;
import game.gridData.map.Mapdata;
import game.map.Map;
import game.pathFinder.PathNode;

public class LightOverlay {
	
	private class LightLevel{
		private Mapdata[] data;
		private DataObject<Integer> lightLevel = new DataObject<>(0);
		private long lastLightUpdate = -1;
		
		public LightLevel(Map map, int x, int y){
			int cx = x/Map.DEFAULT_CHUNKSIZE;
			int cy = y/Map.DEFAULT_CHUNKSIZE;
			this.data = map.getChunks()[cx][cy].getMapData()[x-cx*Map.DEFAULT_CHUNKSIZE][y-cy*Map.DEFAULT_CHUNKSIZE];
		}
		
		public boolean setLightLevel(int lightLevel){
			int surfaceLevel = getSurfaceLevel();
			if(surfaceLevel>0){
				DataObject<Integer> surfaceLightLevel = DayManager.getDayManager().getDayLightLevelData(surfaceLevel);
				if(surfaceLightLevel.getData()>this.lightLevel.getData())this.lightLevel = surfaceLightLevel;
				if(this.lightLevel.getData()>=lightLevel){
					updateMapData();
					if(lastLightUpdate != GameManager.TickManager.getCurrentTick()){
						lastLightUpdate = GameManager.TickManager.getCurrentTick();
						return true;
					}else return false;
				}
			}
			if(lastLightUpdate != GameManager.TickManager.getCurrentTick() || lightLevel>this.lightLevel.getData()){
				this.lastLightUpdate = GameManager.TickManager.getCurrentTick();
				this.lightLevel = new DataObject<Integer>(lightLevel);
				updateMapData();
				return true;
			}
			return false;
		}

		private void updateMapData() {
			for(int a = 0; a<data.length; a++){
				if(data[a]!=null){
					data[a].setLightLevel(this.lightLevel.getData());
				}
			}
		}

		private int getSurfaceLevel() {
			for(int a = 0; a<data.length; a++){
				if(data[a]!=null && data[a].isSurface()){
					return data[a].getSurfaceLevel();
				}
			}
			return -1;
		}

		public boolean setLightLevel(PathNode node, LightObject light) {
			if(data[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP] != null && data[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP].getResource().isOpaque()){
				node.setDistance((int) (node.getDistance()+((light.getLightDistance()*0.2)/light.getLightStrength())));
				if(node.getDistance()/light.getLightStrength()>light.getLightDistance())return false;
			}
			int lightLevel = (int) Math.round(light.getLightStrength()*(Lamp.DEFAULT_LIGHT_STATES-2.0)*(1.0-((1.0+node.getDistance())/(double)light.getLightDistance())))+1;
			if(lightLevel>Lamp.DEFAULT_LIGHT_STATES-1)return false;
			return this.setLightLevel(lightLevel);
		}
	}
	
	private ArrayList<LightObject> lights = new ArrayList<>(); 	
	
	private LightLevel[][] lightLevel;
	
	public LightOverlay(){
	}

	public void load(Map map) {
		this.lightLevel = new LightLevel[map.getWidth()][map.getHeight()];
		for(int x = 0; x<this.lightLevel.length; x++){
			for(int y = 0; y<this.lightLevel.length; y++){
				this.lightLevel[x][y] = new LightLevel(map, x, y);
			}
		}
	}
	
	
	public void register(Mapdata data){
		LightObject lightObject = new LightObject(data, data.getLocation(), ((Lamp)data.getResource().getBlockData()).getLightDistance(), ((Lamp)data.getResource().getBlockData()).getLightStrength());
		this.lights.add(lightObject);
	}
	
	public void update(Mapdata data, boolean removed){
		for(int i = 0; i<this.lights.size(); i++){
			LightObject light = this.lights.get(i);
			if(light.getMaster().equals(data) && removed){
				light.changeState(LightObject.State_REMOVED);
			}else if(light.contains(data.getLocation()) && GameManager.hasStarted()){
				light.changeState(LightObject.State_EDIT);
			}
		}
	}

	public void updateComplete(){
		for(LightObject light: this.lights){
			light.changeState(LightObject.State_EDIT);
		}
	}
	
	public void tick(){
		for(int i = 0; i<lights.size(); i++){
			LightObject light = this.lights.get(i);
			if(light.getState() != LightObject.State_UNCHANGED){
				if(light.getState() == LightObject.State_IGNORE){
					this.lights.remove(i);
					i--;
				}else if(light.getState() == LightObject.State_REMOVED){
					this.lights.remove(i);
					i--;
					removeLight(light);
				}
			}
		}
		update();
	}
	
	private void update() {
		ArrayList<LightObject> lights = new ArrayList<>();
		for(LightObject light: this.lights){
			if(light.getState() != LightObject.State_UNCHANGED){
				lights.add(light);
			}
		}
		for(int i = 0; i<lights.size(); i++){
			updateLight(lights.get(i), lights);
		}
	}


	private void updateLight(LightObject light, ArrayList<LightObject> updateLights) {
		Queue<PathNode> last = new Queue<>();
		last.add(new PathNode(light.getLocation(), 0));
		updateLightLevel(last.get(), light);
		while(!last.isEmpty()){
			PathNode lastNode = last.get();
			last.remove();
			if(lastNode.getDistance()/light.getLightStrength()<light.getLightDistance())for(Direction d: Direction.values()){
				if(!d.equals(Direction.NONE)){
					PathNode nextNode = new PathNode(new Location(Map.getMap().getBlockXOver(lastNode.getLocation().getX()+d.getX()), lastNode.getLocation().getY()+d.getY()), lastNode.getDistance()+1);
					boolean changed = updateLightLevel(nextNode, light);
					if(changed){
						last.add(nextNode);
						for(LightObject infulenced_light: this.lights){
							if(infulenced_light.contains(nextNode.getLocation()) && infulenced_light.getLocation().distance_Math(light.getLocation())<=light.getMaxDistance()){
								if(!updateLights.contains(infulenced_light)){
									infulenced_light.changeState(LightObject.State_EDIT);
									updateLights.add(infulenced_light);
								}
							}
						}
					}
				}
			}
		}
		light.setState(LightObject.State_UNCHANGED);
	}


	private boolean updateLightLevel(PathNode node, LightObject light) {
		boolean change = this.lightLevel[node.getLocation().getX()][node.getLocation().getY()].setLightLevel(node, light);
		return change;
	}


	private void removeLight(LightObject light) {
		for(int x = (int) (-light.getLightDistance()*light.getLightStrength()); x<=light.getLightDistance()*light.getLightStrength(); x++){
			for(int y = (int) (-light.getLightDistance()*light.getLightStrength()); y<=light.getLightDistance()*light.getLightStrength() && x+y<=light.getLightDistance()*light.getLightStrength(); y++){
				Location loc = new Location(Map.getMap().getBlockXOver(x+light.getLocation().getX()), light.getLocation().getY()+y);
				if(loc.getY()>0 && loc.getY()<Map.getMap().getHeight()){
					LightLevel lightLevel = this.lightLevel[loc.getX()][loc.getY()];
					lightLevel.setLightLevel(1);
					for(LightObject infulenced_light: this.lights){
						if(infulenced_light.contains(loc))infulenced_light.changeState(LightObject.State_EDIT);
					}
				}
			}
		}
	}
}
	
	
	