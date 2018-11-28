package game.menu.Interface;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Data.Location;
import Engine.Engine;
import data.MapResource;
import data.map.InteractableBlockData;
import game.Game;
import game.map.Map;
import game.gridData.map.MapBlock;

public class GameInterface {
	
	private Game game;
	private InteractableBlockData currentData;
	
	
	public GameInterface(Game game){
		this.game = game;
	}

	public void tick() {
		Point p = new Point(Map.getMap().getXOver((int)(Engine.getInputManager().MousePosition().getX()+Map.getMap().getMoved().getX()))/Map.DEFAULT_SQUARESIZE,
				(int)(Engine.getInputManager().MousePosition().getY()+Map.getMap().getMoved().getY())/Map.DEFAULT_SQUARESIZE);
		if(currentData!=null){
			if(Engine.getInputManager().getKeyDown().contains(KeyEvent.VK_ESCAPE)){
				currentData.stop();
				currentData = null;
			}else currentData.act(new Location(p));
		}else if(Engine.getInputManager().getMouseButton().contains(MouseEvent.BUTTON1)){
//			System.out.println(p.toString());
			MapBlock[] blocks = this.game.getMap().getBlocks(new Location(p));
			for(int i = 0; i<blocks.length; i++){
				MapBlock block = blocks[i];
				if(block!=null){
					if(((MapResource)block.getResource()).hasData() && ((MapResource)block.getResource()).getBlockData() instanceof InteractableBlockData){
						InteractableBlockData data = (InteractableBlockData)((MapResource)block.getResource()).getBlockData();
						data.act(new Location(p));
						this.currentData = data;
					}
				}
			}
		}
	}
	
	
}
