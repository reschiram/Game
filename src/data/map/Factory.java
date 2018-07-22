package data.map;

import Data.Location;
import game.map.Map;
import game.menu.FactoryMenu;
import game.menu.VehicleEditor;
import game.vehicle.BluePrint;

public class Factory extends InteractableBlockData{
	
	private FactoryMenu menu;
	
	private BluePrint blueprint;
	private VehicleEditor editor;
	
	public Factory(String name){
		super(name);
		menu = new FactoryMenu(this, 5);
	}

	long time = System.currentTimeMillis();
	@Override
	public void act(Location Loc) {
		if(blueprint!=null){
			editor.tick();
		}else{
			if(this.menu.isVisible()){
				blueprint = this.menu.tick();
				if(blueprint!=null){
					if(editor==null)editor = new VehicleEditor();
					editor.show(blueprint);
				}
			}else{
				this.menu.create();
				this.menu.show();
			}
		}
	}
	
	public void stop(){
		blueprint = null;	
		if(menu!=null){
			menu.hide();
			if(menu.getCurrentBluePrint()!=null)menu.getCurrentBluePrint().save();
			menu.setSelected(null);
		}
		if(editor!=null && editor.isVisible()){
			editor.hide();
			Map.getMap().show();
		}
	}

}
