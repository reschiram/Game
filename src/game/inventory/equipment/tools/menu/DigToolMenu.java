package game.inventory.equipment.tools.menu;

import java.awt.Dimension;

import Data.Location;

public class DigToolMenu extends ToolMenu{

	private static Dimension Size = new Dimension(1200,675);	
	private static Location Location = new Location((int) ((1920-Size.getWidth())/2), (int) ((1080-Size.getHeight())/2));
	
	public DigToolMenu() {
		super(Location, Size);
	}

	@Override
	public void tick() {
	}

}
