package game.overlay;

import java.util.ArrayList;

import Data.Direction;
import Data.Location;
import Data.Queue;
import data.map.Lamp;
import game.GameManager;
import game.entity.Entity;
import game.gridData.map.Mapdata;
import game.map.Map;
import game.pathFinder.PathNode;
import game.tick.TickManager;

public class LightOverlay {
	
	private static long GeneralUpdate = 0;
	public static boolean IsGenerellUpdate(){
		return GeneralUpdate == TickManager.getCurrentTick(); 
	}
	
	
	private ArrayList<LightObject> lights = new ArrayList<>(); 	
	
	public void register(Mapdata data){
		LightObject lightObject = new LightObject(data, data.getLocation(), ((Lamp)data.getResource().getBlockData()).getLightDistance(), ((Lamp)data.getResource().getBlockData()).getLightStrength());
		lights.add(lightObject);
		ArrayList<LightObject> lights = new ArrayList<>();
		lights.add(lightObject);
		updateLights(lights);
	}
	
	public void update(Mapdata data, boolean removed){
		ArrayList<LightObject> lights = new ArrayList<>();
		for(int i = 0; i<this.lights.size(); i++){
			LightObject light = this.lights.get(i);
			if(removed){
				if(light.getMaster().equals(data)){
					removeLight(light, lights);
				}
			}else if(light.contains(data.getLocation()) && GameManager.hasStarted()){
				removeLight(light, lights);
				lights.add(light);
				this.lights.add(light);
			}
		}
		updateLights(lights);
	}
	
	private void removeLight(LightObject light, ArrayList<LightObject> lights) {
		this.lights.remove(light);
		for(int x = (int) (-light.getLightDistance()*light.getLightStrength()); x<=light.getLightDistance()*light.getLightStrength(); x++){
			for(int y = (int) (-light.getLightDistance()*light.getLightStrength()); y<=light.getLightDistance()*light.getLightStrength() && x+y<=light.getLightDistance()*light.getLightStrength(); y++){
				Location loc = new Location(Map.getMap().getBlockXOver(x+light.getLocation().getX()), light.getLocation().getY()+y);
				if(loc.getY()>0 && loc.getY()<Map.getMap().getHeight()){
					Mapdata[] data = Map.getMap().getMapData(loc);
					for(int i = 0; i<data.length; i++){
						if(data[i]!=null)data[i].resetLight();
						for(LightObject new_light : this.lights){
							if(new_light.contains(loc) && !lights.contains(new_light))lights.add(new_light);
						}
					}
				}
			}
		}
	}

	public void updateComplete(){
		GeneralUpdate = TickManager.getCurrentTick();
		updateLights(this.lights);
	}

	private void updateLights(ArrayList<LightObject> lights) {
		for(int i = 0; i<lights.size(); i++){
			LightObject currentLight = lights.get(i);
			Queue<PathNode> last = new Queue<>();
			last.add(new PathNode(currentLight.getLocation(), 0));
			updateMapData(last.get(), currentLight);
			while(!last.isEmpty()){
				PathNode lastNode = last.get();
				last.remove();
				if(lastNode.getDistance()/currentLight.getLightStrength()<currentLight.getLightDistance())for(Direction d: Direction.values()){
					if(!d.equals(Direction.NONE)){
						PathNode nextNode = new PathNode(new Location(Map.getMap().getBlockXOver(lastNode.getLocation().getX()+d.getX()), lastNode.getLocation().getY()+d.getY()), lastNode.getDistance()+1);
						int changed = updateMapData(nextNode, currentLight);
						if(changed==2)last.add(nextNode);
						if(changed == 2 && !lights.equals(this.lights)){
							for(LightObject newLight: this.lights){
								if(newLight.contains(nextNode.getLocation()) && newLight.contains(lights.get(0).getLocation()) && !lights.contains(newLight))lights.add(newLight);
							}
						}
					}
				}
			}
		}
	}
	
	private int updateMapData(PathNode lightNode, LightObject currentLight) {
		if(lightNode.getLocation().getY()<0 || lightNode.getLocation().getY()>=Map.getMap().getHeight())return 0;
		Mapdata[] data = Map.getMap().getMapData(lightNode.getLocation());
		if(data[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP] != null && data[Map.DEFAULT_BUILDLAYER+Entity.DEFAULT_ENTITY_UP].getResource().isOpaque()){
			lightNode.setDistance((int) (lightNode.getDistance()+((currentLight.getLightDistance()*0.2)/currentLight.getLightStrength())));
			if(lightNode.getDistance()/currentLight.getLightStrength()>currentLight.getLightDistance())return 0;
		}
		int changed = 0;
		for(int a = 0; a<data.length; a++){
			if(data[a]!=null){
				double newLightLevel = currentLight.getLightStrength()*(1.0-((double)lightNode.getDistance()/(double)currentLight.getLightDistance()));
				changed = data[a].setLightLevel(((int) Math.ceil(newLightLevel*(Lamp.DEFAULT_LIGHT_STATES-2)))+1);
			}
		}
		return changed;
	}
}
	
	
	