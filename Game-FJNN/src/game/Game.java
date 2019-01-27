package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Engine.Engine;
import client.GameCM;
import file.File;
import file.ktv.KTV_File;
import data.VehicleResource;
import events.GameEventManager;
import events.entity.EntityCreationEvent;
import files.FileManager;
import game.entity.loader.EntityLoader;
import game.entity.player.Player;
import game.entity.requester.EntityRequester;
import game.entity.type.EntityType;
import game.inventory.equipment.EquipmentInventory;
import game.inventory.menu.EquipmentInventoryMenu;
import game.map.Map;
import game.map.MapLoader;
import game.menu.Interface.GameInterface;
import game.overlay.DayManager;
import game.overlay.LightOverlay;
import game.vehicle.BluePrint;
import game.vehicle.data.VehicleData;
import launcher.MapDownloader;

public class Game {

	private static LightOverlay LIGHTOVERLAY;
	public static LightOverlay getLightOverlay() {
		return LIGHTOVERLAY;
	}
	
	private Map map;
	private GameInterface gameInterface;
	private MapLoader mapLoader;
	private DayManager dayManager;
	private EquipmentInventoryMenu inventoryMenu;
	private GameCM gameCM;
	
	private boolean started = false;		
	private StringRenderOperation PlayerPosition = new StringRenderOperation(new Location(10,20), new Dimension(200, 40), "Player-Position: X:0 Y:0", null, Color.white);
	
	private GameManager gM;
	
	public Game(GameManager gameManager){
		this.gM = gameManager;
		
		VehicleData.Load();
		VehicleResource.create();
		EntityType.create();
		
		for(File f:FileManager.getFileManager().getKTVFillesInFolder("BluePrints", false)){
			BluePrint.BLUEPRINTS.add(new BluePrint((KTV_File)f));
		}
		
		gameInterface = new GameInterface(this);
		Engine.getEngine(this, this.getClass()).addLayer(false, true, false, 4,5,6,8);
		
		LIGHTOVERLAY = new LightOverlay();
		
		dayManager = new DayManager();
	}

	public void start(MapDownloader mapDownloader) {
		if(mapDownloader != null) {
			map = mapDownloader.getMap();
		}else {
			mapLoader = new MapLoader(FileManager.MAP_TEST,39485636);
			if(mapLoader.getMap()==null)mapLoader.loadMap();
			map = mapLoader.getMap();	
		}
		
		LIGHTOVERLAY.load(map);
		
		Engine.getEngine(this, this.getClass()).addGraphicOperation(PlayerPosition, 10);
		
		dayManager.createTime(10, new Location(PlayerPosition.Hitbox.getX(), PlayerPosition.Hitbox.getY()+20));
		
		EntityRequester.getEntityRequester().requestPlayer(new Location(0, 10));
//		if(FileManager.PLAYER.get("Location.X")!=null) player = (Player) new EntityLoader().Load(Player.class, FileManager.PLAYER);
//		else player = new Player(new Location(0, 0));
//		player.show();
		
//		inventoryMenu = new EquipmentInventoryMenu((EquipmentInventory) player.getInventory(), player.getLocation(), 6);
//		inventoryMenu.createVisuals().hide();		
		
		started = true;
		
		LIGHTOVERLAY.updateComplete();
		map.finalize();
	}

	public void tick() {
		if(this.started){
			this.gameInterface.			tick();
			this.map.getEntityManager().tick();
			this.dayManager.			tick();
			this.inventoryMenu.			tick();
			
			LIGHTOVERLAY.				tick();

			if (Player.getPlayer() != null) {
				Location blockLoc = Player.getPlayer().getBlockLocation();
				this.PlayerPosition.setText("Player-Position: (X: " + blockLoc.getX() + " Y: " + blockLoc.getY() + ")");
			} else {
				this.PlayerPosition.setText("Player-Position: (X: " + 0 + " Y: " + 0 + ")");				
			}
			
//			if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_Z)){
//				mapLoader.saveMap();
//				new EntityLoader().save(player, FileManager.PLAYER);
//			}
		}else if(mapLoader!=null){
			this.gM.LoadScreen.setProgress(0.5+0.49*mapLoader.getProgress());
		}
	}

	public Map getMap() {
		return this.map;
	}

}
