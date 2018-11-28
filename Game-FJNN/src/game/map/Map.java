package game.map;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Data.Location;
import Engine.Engine;
import data.MapResource;
import data.Mouse;
import data.ResourcePart;
import data.map.Lamp;
import data.map.UpdatableBlockData;
import game.tick.TickManager;
import game.Game;
import game.entity.Entity;
import game.entity.manager.EntityManager;
import game.gridData.map.*;
import game.pathFinder.system.PathSystem;

public class Map {
	
	public static final int DEFAULT_SQUARESIZE  = 64;
	public static final int DEFAULT_CHUNKSIZE   = 10;
	public static final int DEFAULT_GROUNDLAYER =  0;
	public static final int DEFAULT_BUILDLAYER  =  2;
	
	public static final int DEFAULT_GROUNDLEVEL = 50; 
	
	private static Map Map;
	public static Map getMap() {
		return Map;
	}
	
	private int Width;
	private int Height;
	private Location Moved = new Location(0,0); 
	private int seed;
	
	private MapChunk[][] chunks;	
	private EntityManager entityManager;
	private PathSystem pathSystem;

	private double xm = 0;
	private double ym = 0;
	private int speed = 5;
	
	private double acceleration = 0.2;
	
	private boolean finalized = false;
	
	public Map(int width, int height, int seed){
		Map = this;
		Engine.getEngine(this, this.getClass()).addLayer(false, false, false, 0,1);
		Engine.getEngine(this, this.getClass()).addLayer(true , false, false, 2,3);
		this.Width   = width;
		this.Height  = height;
		this.chunks  = new MapChunk[this.Width/DEFAULT_CHUNKSIZE +1][this.Height/DEFAULT_CHUNKSIZE +1];
		
		this.seed = seed;

		entityManager = new EntityManager(chunks.length, chunks[0].length);
		Engine.setGameWidth(Width*DEFAULT_SQUARESIZE);
		
		pathSystem = new PathSystem(this);
	}
	
	public Map(int[][][] ground, int[][][] build, int seed){
		Map = this;
		Engine.getEngine(this, this.getClass()).addLayer(false, false, false, 0,1);
		Engine.getEngine(this, this.getClass()).addLayer(true , false, false, 2,3);
		this.Width   = ground.length;
		this.Height  = ground[0].length;
		this.chunks  = new MapChunk[this.Width/DEFAULT_CHUNKSIZE +1][this.Height/DEFAULT_CHUNKSIZE +1]   ;
		
		for(int x = 0; x<this.Width; x++){
			for(int y = 0; y<this.Height; y++){
				for(int i = 0; i<2; i++){
					if(ground[x][y][i]!=0)addToGround(ground[x][y][i], x, y);
					if(build [x][y][i]!=0)addToBuild (build [x][y][i], x, y);
				}
			}
		}
		
		this.seed = seed;

		entityManager = new EntityManager(chunks.length, chunks[0].length);
		Engine.setGameWidth(Width*DEFAULT_SQUARESIZE);
		
		pathSystem = new PathSystem(this);
	}

	public boolean hasResource(MapResource res, int x, int y){
		MapChunk chunk = getChunk(x, y);
		return chunk.getMapData(x, y, res.isGround())[res.getLayerUp()]!=null && chunk.getMapData(x, y, res.isGround())[res.getLayerUp()].getResource().getID() == res.getID();
	}

	public int getWidth() {
		return Width;
	}

	public int getHeight() {
		return Height;
	}
	
	private void setBlock(Location location, MapResource resource){
		MapBlock b = null;
		if(resource.isGround())b = new MapBlock(resource, DEFAULT_GROUNDLAYER+resource.getLayerUp(), location);
		else b = new MapBlock(resource, DEFAULT_BUILDLAYER+resource.getLayerUp(), location);
		deleteBlock(location, resource.getLayerUp(), resource.isGround());
		for(ResourcePart resPart : resource.getResourceParts()){
			Location loc = new Location(location.x+resPart.getLocation().x, location.y+resPart.getLocation().y);
			deleteBlock(loc, resource.getLayerUp(), resource.isGround());
		}
		Mapdata[] parts = b.create();
		for(int i = 1; i<parts.length; i++){
			getChunk(parts[i].getLocation()).set(parts[i]);
			update(parts[i].getLocation().getX(), parts[i].getLocation().getY());
			Game.getLightOverlay().update(parts[i], false);
		}
		b.show();
		getChunk(location).set(b);
		update(location.getX(), location.getY());
		Game.getLightOverlay().update(b, false);
	}
	
	private void update(int x, int y){
		if(!finalized)return;
		updateSurface(x);
		updateBlock(x, y);
		
		x=getBlockXOver(x+1);
		updateBlock(x, y);
		
		x=getBlockXOver(x-2);
		updateBlock(x, y);
		
		x=getBlockXOver(x+1);
		y++;
		if(y<Height)updateBlock(x, y);
		
		y-=2;
		if(y>=0)updateBlock(x, y);
		
	}
	
	private void updateBlock(int x, int y){
		boolean found = false;
		Mapdata[] data = getChunk(x, y).getMapData(x,y);
		for(int i = data.length-1; i>=0; i--){
			if(data[i]!=null){
				if(found)data[i].hide();
				else data[i].show();
				if(data[i].getResource().isOpaque())found= true;
				if(data[i].getResource().hasData() && data[i].getResource().getBlockData() instanceof UpdatableBlockData)((UpdatableBlockData)data[i].getResource().getBlockData()).update(data[i], x, y);
			}
		}
	}

	private void updateSurface(int dx){
		int surface = Lamp.DEFAULT_SURFACE_LEVELS-1;
		for(int y = 0; y<=this.getHeight(); y++){
			Mapdata[] data = getMapData(new Location(dx, y));
			int found = -1;
			for(int i = 0; i<data.length; i++){
				if(data[i]!=null){
					found = i;
					data[i].setSurface(surface);
				}
			}
			if(surface>0  && (data[DEFAULT_BUILDLAYER]!=null || (found!=-1 && surface!=Lamp.DEFAULT_SURFACE_LEVELS-1)) && data[found].getResource().isOpaque() && !data[found].isAlwaysSurface())surface--;
		}
	}
	
	public void deleteBlock(Location location, int layerUp, boolean isGround) {
		Mapdata mapdata = getChunk(location).getMapData(location, isGround)[layerUp];
		if(mapdata!=null){
			if(mapdata instanceof MapBlock){
				MapBlock b = (MapBlock)mapdata;
				for(MapDummieBlock part: b.blockParts){
					getChunk(part.getLocation()).remove(part);
					update(part.getLocation().getX(), part.getLocation().getY());
					Game.getLightOverlay().update(part, true);
				}
				mapdata.destroyVisual();
				getChunk(b.getLocation()).remove(b);
				update(b.getLocation().getX(), b.getLocation().getY());
				Game.getLightOverlay().update(mapdata, true);
			}else if(mapdata instanceof MapDummieBlock){
				MapDummieBlock part = (MapDummieBlock)mapdata;
				MapBlock block = part.getBlock();
				deleteBlock(block.getLocation(), layerUp, isGround);
			}
		}
	}
	
	public void add(int res,Location location, boolean isGround){
		if(res==0 || MapResource.getMapResource(res)==null){
			for(int i = 0; i<2; i++){
				deleteBlock(location, i, isGround);
			}
		}else{			
			setBlock(location, MapResource.getMapResource(res));
		}
	}
	
	private MapChunk getChunk(Location location) {
		return getChunk(location.x, location.y);
	}
	
	public MapChunk getChunk(int x, int y) {
//		System.out.print(x+"|"+y+" -> ");
		x/=DEFAULT_CHUNKSIZE;
		y/=DEFAULT_CHUNKSIZE;
		if(this.chunks[x][y]==null)this.chunks[x][y] = new MapChunk(x*DEFAULT_CHUNKSIZE, y*DEFAULT_CHUNKSIZE, DEFAULT_CHUNKSIZE, DEFAULT_CHUNKSIZE);
		return this.chunks[x][y];
	}

	public void addToGround(int res, int x, int y) {		
		add(res, new Location(x, y), true);
	}
	
	public void addToBuild(int res, int x, int y) {
		add(res, new Location(x, y), false);
	}
	
	public void move(){
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_UP)){
			ym+=speed*TickManager.getDeltaTime();
			if(this.getMoved().getY()-ym<0)ym-=speed*TickManager.getDeltaTime();
			else while(ym>1){
				Engine.getEngine(this, this.getClass()).moveLayer(0, -1, 0,1,2,3,4);
				ym--;
				Mouse.YOff--;
				this.Moved.setLocation(this.Moved.getX(), this.Moved.getY()-1);
			}
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_DOWN)){
			ym-=speed*TickManager.getDeltaTime();
			while(ym<-1){
				Engine.getEngine(this, this.getClass()).moveLayer(0, 1, 0,1,2,3,4);
				ym++;
				Mouse.YOff++;
				this.Moved.setLocation(this.Moved.getX(), this.Moved.getY()+1);
			}
		}
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_RIGHT)){
			xm+=speed*TickManager.getDeltaTime();
			while(xm>1){
				Engine.getEngine(this, this.getClass()).moveLayer(1, 0, 0,1,2,3,4);
				xm--;
				Mouse.XOff++;
				this.Moved.setLocation(this.Moved.getX()+1, this.Moved.getY());
			}
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_LEFT)){
			xm-=speed*TickManager.getDeltaTime();
			if(this.getMoved().getX()+xm<0)xm+=speed*TickManager.getDeltaTime();
			else while(xm<-1){
				Engine.getEngine(this, this.getClass()).moveLayer(-1, 0, 0,1,2,3,4);
				xm++;
				Mouse.XOff--;
				this.Moved.setLocation(this.Moved.getX()-1, this.Moved.getY());
			}
		}
	}

	public MapBlock[] getBlocks(Location loc) {
		MapBlock[] blocks = new MapBlock[4];
		for(int i = 0; i<2; i++){
			Mapdata data = getChunk(loc).getMapData(loc, true)[i];
			if(data!=null){
				if(data instanceof MapBlock)blocks[0] = (MapBlock) data;
				else blocks[0+i] = ((MapDummieBlock)data).getBlock();
			}
			data = getChunk(loc).getMapData(loc, false)[i];
			if(data!=null){
				if(data instanceof MapBlock)blocks[2] = (MapBlock) data;
				else blocks[2+i] = ((MapDummieBlock)data).getBlock();
			}
		}
		return blocks;
	}

	public void hide() {
		for(int x = 0; x<chunks.length; x++){
			for(int y = 0; y<chunks[x].length; y++){
				if(chunks[x][y]!=null)chunks[x][y].hide();
			}
		}
	}

	public void show() {
		for(int x = 0; x<chunks.length; x++){
			for(int y = 0; y<chunks[x].length; y++){
				if(chunks[x][y]!=null)chunks[x][y].show();
			}
		}
	}

	public Location getMoved() {
		return this.Moved;
	}

	public MapChunk[][] getChunks() {
		return chunks;
	}

	public ArrayList<MapBlock> getBlocks(Class<?> blockDataType, Location location, Dimension dimension){
		ArrayList<MapBlock> blocks = new ArrayList<>();
		for(int x = location.x/(DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE); x<Math.ceil((location.x+dimension.getWidth())/(DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE)); x++){
			for(int y = location.y/(DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE); y<Math.ceil((location.y+dimension.getHeight())/(DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE)); y++){
				int mx = getXOver(x*DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE)/(DEFAULT_CHUNKSIZE*DEFAULT_SQUARESIZE);
				MapChunk chunk = this.chunks[mx][y];
				if(chunk!=null && chunk.hasType(blockDataType)){
					blocks.addAll(chunk.getBlockType(blockDataType));
				}
			}
		}
		return blocks;
	}

	public Mapdata[] getMapData(Location location) {
		location.x = getBlockXOver(location.getX());
		MapChunk chunk = getChunk(location);
		Mapdata[] data = new Mapdata[4];
		for(int i = 0; i<2; i++)data[i  ] = chunk.getMapData(location, true )[i];
		for(int i = 0; i<2; i++)data[i+2] = chunk.getMapData(location, false)[i];
		return data;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setMoved(int x, int y) {
		if(x==0 && y==0)return;
		this.Moved.setLocation(getXOver(this.Moved.getX()-x), this.Moved.getY()-y);
//		System.out.println(x+"->"+y);
		Engine.getEngine(this, this.getClass()).moveLayer(-x, -y, DEFAULT_GROUNDLAYER, DEFAULT_GROUNDLAYER+1, DEFAULT_BUILDLAYER, DEFAULT_BUILDLAYER+1);
	}

	public double getAcceleration() {
		return acceleration;
	}
	
	public int getXOver(int x){
		if(x<0)x = getWidth()*DEFAULT_SQUARESIZE+x;
		else if(x>= getWidth()*DEFAULT_SQUARESIZE)x = x-getWidth()*DEFAULT_SQUARESIZE;
		return x;
	}
	
	public int getBlockXOver(int x) {
		if(x<0)x = getWidth()+x;
		else if(x>= getWidth())x = x-getWidth();
		return x;
	}
	
	public int getSeed(){
		return seed;
	}
	
	public void finalize(){
		for(int x = 0; x<Width; x++){
			for(int y = 0; y<Height; y++){
				updateBlock(x, y);
			}
			updateSurface(x);
		}
		this.finalized = true;
	}

	public boolean entityCanAcces(Entity entity, int x, int y) {
		Location loc = new Location(x, y);
		Mapdata data = getMapData(loc)[Entity.DEFAULT_ENTITY_UP+DEFAULT_BUILDLAYER];
		if(((data !=null && !data.canHost(entity.getWidth(), entity.getHeight())))|| !entity.canReach(loc)) return false;
		return true;
	}

	public PathSystem getPathSystem() {
		return pathSystem;
	}
}
