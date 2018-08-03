package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import Data.Location;
import Data.GraphicOperation.StringRenderOperation;
import Engine.Engine;
import anim.AnimationType;
import data.MapResource;
import data.Mouse;
import files.FileManager;
import game.dev.mapEditor.MapEditor;
import game.inventory.items.ItemType;
import game.tick.TickManager;
import menu.LoadScreen;
import sprites.Sprites;

public class GameManager {
		
	private Game game;
	private MapEditor mapEditor;
	private StringRenderOperation FPSCounter;
	private StringRenderOperation LatencyCounter;
	
	LoadScreen LoadScreen;
	
	public GameManager(){
		new Engine(1920, 1080, new Dimension(800,800));
		Engine.getEngine(this, this.getClass()).getWindow().setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		Engine.getEngine(this, this.getClass()).addLayer(false, true, false, 10);
		LoadScreen = new LoadScreen(10).create();
		
		FileManager.Load();
		LoadScreen.setProgress(0.2);
		
		Sprites.create();
		AnimationType.create();
		ItemType.Load();
		MapResource.create();
		LoadScreen.setProgress(0.3);
		
		this.setKillAble();		
		new TickManager(this);	
		LoadScreen.setProgress(0.4);
		
		new Mouse(10);	
		LoadScreen.setProgress(0.5);

		
		FPSCounter = new StringRenderOperation(new Location(1800,50), new Dimension(120, 50), "FPS:"+Engine.getEngine(this, this.getClass()).getFPS(), null, Color.WHITE);
		Engine.getEngine(this, this.getClass()).addGraphicOperation(FPSCounter, 10);
		LatencyCounter = new StringRenderOperation(new Location(1800,80), new Dimension(120, 50), "Latency:"+TickManager.getLatency()+" tick(s)", null, Color.WHITE);
		Engine.getEngine(this, this.getClass()).addGraphicOperation(LatencyCounter, 10);
		start();				
		LoadScreen.destroyVisuals();
	}
	
	private void start() {
		game = new Game(this);
		game.start();
//		this.mapEditor = new MapEditor(new MapLoader(FileManager.MAP_TEST));
	}

	long time = System.currentTimeMillis();
	public void tick(){
		if(Mouse.getMouse()!=null)Mouse.getMouse().tick();
		if(game!=null)game.tick();
		else if(mapEditor!=null)mapEditor.tick();
		if(System.currentTimeMillis()-time > 500 && FPSCounter!=null){
			FPSCounter.setText("FPS:"+Engine.getEngine(this, this.getClass()).getFPS());
			LatencyCounter.setText("Latency:"+TickManager.getLatency()+" tick(s)");
		}
	}
	
	private void setKillAble(){
		Engine.getInputManager().addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
	}
}