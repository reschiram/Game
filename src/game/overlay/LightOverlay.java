package game.overlay;

import java.awt.Dimension;
import java.util.ArrayList;

import Data.Hitbox;
import Data.Location;
import data.map.Lamp;
import game.entity.Entity;
import game.entity.manager.EntityManager;
import game.entity.type.EntityType;
import game.entity.type.data.EntityData;
import game.entity.type.interfaces.EntityLight;
import game.gridData.map.MapBlock;
import game.gridData.map.Mapdata;
import game.map.Map;
import game.tick.TickManager;

public class LightOverlay {
	
	private Dimension WindowDimension = new Dimension((int)(1920*1.5), (int)(1080*1.5));
	private ArrayList<LightObject> lights = new ArrayList<>(); 
	
	public LightOverlay(int layer){
	}
	
	public void tick(){
		Location loadLocation = new Location((int)(Map.getMap().getMoved().getX())-(int)(WindowDimension.getWidth()/(1.5*2)), Map.getMap().getMoved().getY()-(int)(WindowDimension.getHeight()/(1.5*2)));
		if(loadLocation.getY()<0)loadLocation.setLocation(loadLocation.getX(), 0);
		
		Hitbox hbLeft = new Hitbox(Map.getMap().getXOver(loadLocation.getX()), loadLocation.getY(),
				(Map.getMap().getWidth()*Map.DEFAULT_SQUARESIZE)-Map.getMap().getXOver(loadLocation.getX()), (int) WindowDimension.getHeight());
		
//		System.out.println(hbLeft.toString());
		
		if(hbLeft.getWidth()>WindowDimension.getWidth())hbLeft.setDimension(0,0);
		else{
			loadLocation.setLocation(0, loadLocation.getY());
		}
		
		Hitbox hbRight = new Hitbox(loadLocation.getX(), loadLocation.getY(), (int) (WindowDimension.getWidth()-hbLeft.getWidth()), (int) WindowDimension.getHeight());

//		System.out.println(hbLeft.toString()+ " -> " +hbRight.toString());
		
		ArrayList<MapBlock> Lampblocks = Map.getMap().getBlocks(Lamp.class, hbRight.getLocation(), hbRight.getDimension());		
		ArrayList<Entity> Lampentitys = EntityManager.getEntityManager().getEntityPixelLocation(EntityType.LightEntity, hbRight.getLocation(), hbRight.getDimension());
		ArrayList<Entity> entitys = EntityManager.getEntityManager().getEntityPixelLocation(EntityType.Entity, hbRight.getLocation(), hbRight.getDimension());
		
		if(hbLeft.getWidth()>0){
			Lampblocks .addAll(Map.getMap().getBlocks(Lamp.class, hbLeft.getLocation(), hbLeft.getDimension()));
			Lampentitys.addAll(EntityManager.getEntityManager().getEntityPixelLocation(EntityType.LightEntity, hbLeft.getLocation(), hbLeft.getDimension()));
			entitys    .addAll(EntityManager.getEntityManager().getEntityPixelLocation(EntityType.Entity, hbLeft.getLocation(), hbLeft.getDimension()));
		}
		
//		System.out.println(Lampentitys.size());
		for(int a = 0; a<lights.size(); a++){
			LightObject light = lights.get(a);			
			if(light.getMaster() instanceof MapBlock){
				
				boolean contains = false;
				for(int i = 0; i<Lampblocks.size(); i++){
					if(light.getMaster().equals(Lampblocks.get(i))){
						contains = true;
						Lampblocks.remove(i);
						i--;
					}
				}
				if(!contains){
					lights.remove(a);
					a--;
					reset(light, entitys);
				}
			}else if(light.getMaster() instanceof Entity && ((Entity)light.getMaster()).hasData(EntityData.LIGHTENTITYDATA)){
				
				boolean contains = false;
				for(int i = 0; i<Lampentitys.size(); i++){
					if(light.getMaster().equals(Lampentitys.get(i))){
						contains = true;
						if(!light.getLocation().isEqual(Lampentitys.get(i).getBlockLocation())){
							reset(light, entitys);
							light.setLocation(Lampentitys.get(i).getBlockLocation());
						}
//						System.out.println(contains);
						Lampentitys.remove(i);
						i--;
					}
				}
				if(!contains){
					reset(light, entitys);
					lights.remove(a);
					a--;
				}
			}
		}
		
		for(Mapdata Lampblock : Lampblocks ){
			lights.add(new LightObject(Lampblock , Lampblock .getLocation(),
					((Lamp)Lampblock.getResource().getBlockData()).getLightDistance()	, ((Lamp)Lampblock.getResource().getBlockData()).getLightStrength()	));
		}
		for(Entity  LampEntity: Lampentitys){
			lights.add(new LightObject(LampEntity, LampEntity.getBlockLocation(),
					((EntityLight)LampEntity).getLightDistance()						, ((EntityLight)LampEntity).getLightStrength()						)); 
		}
		
//		System.out.println(DayManager.getDayManager().getDayLightLevel()+"->"+lights.size());
		
		for(LightObject light: lights){
			int maxDistance = (int) Math.sqrt(light.getLightDistance()*light.getLightDistance());
			for(int x = -light.getLightDistance(); x<light.getLightDistance(); x++){
				for(int y = -light.getLightDistance(); y<light.getLightDistance(); y++){
					
					int distance = (int)Math.sqrt(x*x+y*y); 					
					Location location = new Location(light.getLocation().getX()+x, light.getLocation().getY()+y); 
					location.x = Map.getMap().getXOver(location.getX()*Map.DEFAULT_SQUARESIZE)/Map.DEFAULT_SQUARESIZE;
					if(distance <= maxDistance && location.getX()>=0 && location.getY()>=0){
						int newLightLevel = (int) Math.round((Lamp.DEFAULT_LIGHT_STATES-1)*(1.0-(double)((double)distance/(double)maxDistance)));
						newLightLevel= (int) Math.round(newLightLevel*light.getLightStrength());
//						System.out.println(newLightLevel);
						Mapdata[] block = Map.getMap().getMapData(location);
						for(int i = 0; i<block.length; i++){
							if(block[i] !=null && (block[i].lastLightUpdate() != TickManager.getCurrentTick() || newLightLevel>block[i].getLightLevel()))block[i].setLightLevel(newLightLevel);
						}
						Location pixelLoc = new Location(Map.getMap().getXOver(location.getX()*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2), location.y*Map.DEFAULT_SQUARESIZE + Map.DEFAULT_SQUARESIZE/2);
						for(Entity entity: entitys){
							if(entity.getHitbox().contains(pixelLoc)){
								Mapdata b = Map.getMap().getMapData(entity.getBlockLocation())[Map.DEFAULT_GROUNDLAYER];
								if(entity.getLastLightUpdate() != TickManager.getCurrentTick() || b.getLightLevel()>entity.getLightLevel())entity.setLightLevel(b.getLightLevel());
							}
						}
					}
				}
			}
		}
	}

	private void reset(LightObject light, ArrayList<Entity> entitys) {
		for(int x = -light.getLightDistance(); x<light.getLightDistance(); x++){
			for(int y = -light.getLightDistance(); y<light.getLightDistance(); y++){
				Location location = new Location(light.getLocation().getX()+x, light.getLocation().getY()+y); 
				if(location.getX()>=0 && location.getY()>=0){
					Mapdata[] block = Map.getMap().getMapData(location);
					for(int i = 0; i<block.length; i++){
						if(block[i] !=null){
							block[i].setLightLevel(0);
						}
					}
					Location pixelLoc = new Location(location.getX()*Map.DEFAULT_SQUARESIZE, location.y*Map.DEFAULT_SQUARESIZE);
					for(Entity entity: entitys){
						if(entity.getHitbox().contains(pixelLoc))entity.setLightLevel(0);
					}
				}
			}
		}
	}

}
