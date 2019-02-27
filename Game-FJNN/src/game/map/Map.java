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
import events.GameEventManager;
import events.map.MapBlockAddEvent;
import events.map.MapBlockDeleteEvent;
import game.Game;
import game.GameManager;
import game.entity.Entity;
import game.entity.manager.EntityManager;
import game.entity.player.Player;
import game.gridData.map.*;
import game.pathFinder.system.PathSystem;

public class Map {
	
	public static final int DEFAULT_SQUARESIZE  = 64;
	public static final int DEFAULT_CHUNKSIZE   = 10;
	public static final int DEFAULT_GROUNDLAYER =  0;
	public static final int DEFAULT_BUILDLAYER  =  2;
	
	public static final int DEFAULT_GROUNDLEVEL = 50; 
	
	private static final int DEFAULT_LOADRADIUS =  3; 
	
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
	
	public Map(int[][][] mapInfo, int seed) {
		loadMap_Step1(mapInfo.length, mapInfo[0].length);
		
		for(int x = 0; x < this.chunks.length; x++){
			for(int y = 0; y < this.chunks[x].length; y++){
				for(int i = 0; i<2; i++){					
					this.chunks[x][y] = new MapChunk(x, y, mapInfo);
				}
			}
		}
		
		loadMap_Step2(seed);
	}

	public Map(int[][][] groundData, int[][][] buildData, int seed) {
		loadMap_Step1(groundData.length, groundData[0].length);
		
		for(int x = 0; x < this.chunks.length; x++){
			for(int y = 0; y < this.chunks[x].length; y++){
				for(int i = 0; i<2; i++){					
					this.chunks[x][y] = new MapChunk(x, y, groundData, buildData);
				}
			}
		}
		
		loadMap_Step2(seed);
	}
	
	private void loadMap_Step1(int width, int height) {
		Map = this;
		
		Engine.getEngine(this, this.getClass()).addLayer(false, false, false, DEFAULT_GROUNDLAYER, DEFAULT_GROUNDLAYER + 1);
		Engine.getEngine(this, this.getClass()).addLayer(true, false, false, DEFAULT_BUILDLAYER, DEFAULT_BUILDLAYER + 1);
		
		this.Width = width;
		this.Height = height;
		this.chunks = new MapChunk
				[(int) Math.ceil(((double) this.Width ) / ((double) DEFAULT_CHUNKSIZE))]
				[(int) Math.ceil(((double) this.Height) / ((double) DEFAULT_CHUNKSIZE))];
	}
	
	private void loadMap_Step2( int seed) {		
		this.seed = seed;

		entityManager = new EntityManager(chunks.length, chunks[0].length);
		Engine.setGameWidth(Width*DEFAULT_SQUARESIZE);
		
		pathSystem = new PathSystem(this);		
	}


	public int getWidth() {
		return Width;
	}

	public int getHeight() {
		return Height;
	}

	private void setBlock(Location blockLocation, MapResource resource, boolean publishToServer) {
		MapBlock b = null;
		if (resource.isGround())b = new MapBlock(resource, DEFAULT_GROUNDLAYER + resource.getLayerUp(), blockLocation);
		else b = new MapBlock(resource, DEFAULT_BUILDLAYER + resource.getLayerUp(), blockLocation);
		
		deleteBlock(blockLocation, resource.getLayerUp(), resource.isGround(), publishToServer);
		
		for (ResourcePart resPart : resource.getResourceParts()) {
			Location loc = new Location(blockLocation.x + resPart.getLocation().x,
					blockLocation.y + resPart.getLocation().y);
			deleteBlock(loc, resource.getLayerUp(), resource.isGround(), publishToServer);
		}
		
		Mapdata[] parts = b.create();
		for (int i = 1; i < parts.length; i++) {
			getChunk(parts[i].getLocation()).set(parts[i]);
			update(parts[i].getLocation().getX(), parts[i].getLocation().getY());
			Game.getLightOverlay().update(parts[i], false);
		}
		b.show();		
		getChunk(blockLocation).set(b);
		update(blockLocation.getX(), blockLocation.getY());
		Game.getLightOverlay().update(b, false);

		if (publishToServer) GameEventManager.getEventManager().publishEvent(new MapBlockAddEvent(b));
	}
	
	private MapChunk getChunk(Location blockLocation) {
		return chunks[blockLocation.getX() / DEFAULT_CHUNKSIZE][blockLocation.getY() / DEFAULT_CHUNKSIZE];
	}

	private void update(int x, int y){
		if(!this.chunks[x / DEFAULT_CHUNKSIZE][y / DEFAULT_CHUNKSIZE].isFinalized())return;
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
		Mapdata[] data = this.chunks[x / DEFAULT_CHUNKSIZE][y / DEFAULT_CHUNKSIZE].getMapData(x,y);
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
		for(int y = 0; y < this.getHeight(); y++){
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
	
	public void deleteBlock(Location location, int layerUp, boolean isGround, boolean publishToServer) {
		Mapdata mapdata = getMapData(location)[layerUp + (isGround ? 0 : 2)];
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
				
				if(publishToServer) GameEventManager.getEventManager().publishEvent(new MapBlockDeleteEvent(b, false));
			}else if(mapdata instanceof MapDummieBlock){
				MapDummieBlock part = (MapDummieBlock)mapdata;
				MapBlock block = part.getBlock();
				deleteBlock(block.getLocation(), layerUp, isGround, publishToServer);
			}
		}
	}
	
	public void add(int res,Location location, boolean isGround, boolean publishToServer){
		if(res==0 || MapResource.getMapResource(res)==null){
			for(int i = 0; i<2; i++){
				deleteBlock(location, i, isGround, publishToServer);
			}
		}else{			
			setBlock(location, MapResource.getMapResource(res), publishToServer);
		}
	}

	public void addToGround(int res, int x, int y, boolean publishToServer) {		
		add(res, new Location(x, y), true, publishToServer);
	}
	
	public void addToBuild(int res, int x, int y, boolean publishToServer) {
		add(res, new Location(x, y), false, publishToServer);
	}
	
	public void move(){
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_UP)){
			ym+=speed*GameManager.TickManager.getDeltaTime();
			if(this.getMoved().getY()-ym<0)ym-=speed*GameManager.TickManager.getDeltaTime();
			else while(ym>1){
				Engine.getEngine(this, this.getClass()).moveLayer(0, -1, 0,1,2,3,4);
				ym--;
				Mouse.YOff--;
				this.Moved.setLocation(this.Moved.getX(), this.Moved.getY()-1);
			}
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_DOWN)){
			ym-=speed*GameManager.TickManager.getDeltaTime();
			while(ym<-1){
				Engine.getEngine(this, this.getClass()).moveLayer(0, 1, 0,1,2,3,4);
				ym++;
				Mouse.YOff++;
				this.Moved.setLocation(this.Moved.getX(), this.Moved.getY()+1);
			}
		}
		if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_RIGHT)){
			xm+=speed*GameManager.TickManager.getDeltaTime();
			while(xm>1){
				Engine.getEngine(this, this.getClass()).moveLayer(1, 0, 0,1,2,3,4);
				xm--;
				Mouse.XOff++;
				this.Moved.setLocation(this.Moved.getX()+1, this.Moved.getY());
			}
		}else if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_LEFT)){
			xm-=speed*GameManager.TickManager.getDeltaTime();
			if(this.getMoved().getX()+xm<0)xm+=speed*GameManager.TickManager.getDeltaTime();
			else while(xm<-1){
				Engine.getEngine(this, this.getClass()).moveLayer(-1, 0, 0,1,2,3,4);
				xm++;
				Mouse.XOff--;
				this.Moved.setLocation(this.Moved.getX()-1, this.Moved.getY());
			}
		}
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

	public Mapdata[] getMapData(Location blockLocation) {
		blockLocation.x = getBlockXOver(blockLocation.getX());
		
		int cx = blockLocation.getX() / DEFAULT_CHUNKSIZE;
		int cy = blockLocation.getY() / DEFAULT_CHUNKSIZE;

		Mapdata[] data = new Mapdata[4];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.chunks[cx][cy].getMapData(blockLocation.getX(), blockLocation.getY())[i];
		}
		return data;
	}

	public Mapdata[] getMapData(int blockX, int blockY) {
		blockX = getBlockXOver(blockX);
		
		int cx = blockX / DEFAULT_CHUNKSIZE;
		int cy = blockY / DEFAULT_CHUNKSIZE;
		
		Mapdata[] data = new Mapdata[4];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.chunks[cx][cy].getMapData(blockX, blockY)[i];
		}
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
		while(x<0)x = getWidth()*DEFAULT_SQUARESIZE+x;
		while(x>= getWidth()*DEFAULT_SQUARESIZE)x = x-getWidth()*DEFAULT_SQUARESIZE;
		return x;
	}
	
	public int getBlockXOver(int x) {
		while(x<0)x = getWidth()+x;
		while(x>= getWidth())x = x-getWidth();
		return x;
	}
	
	public int getSeed(){
		return seed;
	}
	
	public void finalize(MapChunk chunk) {		
		chunk.setFinalized(true);
		
		for (int x = 0; x < DEFAULT_CHUNKSIZE; x++) {
			for (int y = 0; y < DEFAULT_CHUNKSIZE; y++) {
				update(x, y);
				for (int i = 0; i < chunk.getMapData().length; i++) {
					if (chunk.getMapData()[x][y][0] != null)
						Game.getLightOverlay().update(getMapData(new Location(x, y))[0], false);
				}
			}
		}
	}

	public boolean entityCanAcces(Entity entity, int x, int y) {
		Location loc = new Location(x, y);
		Mapdata data = getMapData(loc)[Entity.DEFAULT_ENTITY_UP+DEFAULT_BUILDLAYER];
		if(((data !=null && !data.canHost(entity.getWidth(), entity.getHeight())))|| !entity.canReach(loc)) return false;
		return true;
	}

	public boolean entityCanExist(Entity entity, int x, int y) {
		Location loc = new Location(x, y);
		Mapdata data = getMapData(loc)[Entity.DEFAULT_ENTITY_UP+DEFAULT_BUILDLAYER];
		if(data !=null && !data.canHost(entity.getWidth(), entity.getHeight())) return false;
		return true;
	}

	public PathSystem getPathSystem() {
		return pathSystem;
	}

	public void addDirect(int resID, int blockX, int blockY, boolean sendToServer) {
		MapResource res = MapResource.getMapResource(resID);
		if(res == null) return;
		Location blockLoc = new Location(blockX, blockY);
		MapBlock block = new MapBlock(res, (res.isGround() ? DEFAULT_GROUNDLAYER : DEFAULT_BUILDLAYER) + res.getLayerUp(), blockLoc);
		
		Mapdata[] parts = block.create();
		for(int i = 1; i<parts.length; i++){
			getChunk(parts[i].getLocation()).set(parts[i]);
		}
		block.show();
		getChunk(blockLoc).set(block);
		if(sendToServer) GameEventManager.getEventManager().publishEvent(new MapBlockAddEvent(block));
	}

	public void addMapBlock(int blockX, int blockY, int resID, boolean publishToServer) {
		blockX = getBlockXOver(blockX);
		
		this.setBlock(new Location(blockX, blockY), MapResource.getMapResource(resID), publishToServer);
	}

	public void tick() {
		if(Player.getPlayer() != null) {
			Player p = Player.getPlayer();
			
			Location blockLoc = p.getBlockLocation();
			int cx = getBlockXOver(blockLoc.getX()) / DEFAULT_CHUNKSIZE;
			int cy = blockLoc.getY() / DEFAULT_CHUNKSIZE;
			
			for (int dx = cx - DEFAULT_LOADRADIUS; dx <= cx + DEFAULT_LOADRADIUS; dx++) {
				for (int dy = cy - DEFAULT_LOADRADIUS; dy <= cy + DEFAULT_LOADRADIUS; dy++) {
					if(dy >= 0 && dy < this.chunks[0].length) {
						int x = Map.getBlockXOver(dx * DEFAULT_CHUNKSIZE) / DEFAULT_CHUNKSIZE;
						this.chunks[x][dy].load(this);
					}
				}
			}
		}
	}

	public MapChunk getChunk(int blockX, int blockY) {
		return this.chunks[blockX / DEFAULT_CHUNKSIZE][blockY / DEFAULT_CHUNKSIZE];
	}
}
