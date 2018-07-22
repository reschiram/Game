package game.entity.loader;

import java.lang.reflect.InvocationTargetException;

import Data.Location;
import file.File;
import files.FileManager;
import game.entity.Entity;
import game.entity.type.data.EntityData;
import game.entity.type.interfaces.EntityInventory;
import game.inventory.Inventory;
import game.inventory.equipment.EquipmentInventory;
import game.inventory.equipment.EquipmentType;
import game.inventory.items.Item;
import game.inventory.items.ItemType;
import game.map.Map;

public class EntityLoader {
	
	public void save(Entity entity, File file){
		
		file.set("Location.X", entity.getBlockLocation().getX()+"");
		file.set("Location.Y", entity.getBlockLocation().getY()+"");
	
		if(entity.hasData(EntityData.ENTITYINVENTOTYDATA)){
			file.remove("inventory");
			
			Inventory inv = ((EntityInventory)entity).getInventory();
			int itemAmount = inv.getSize();
			
			if(inv instanceof EquipmentInventory)itemAmount = ((EquipmentInventory)inv).getItemSize();
			for(int i = 0; i<itemAmount; i++){
				if(inv.getItem(i)!=null){
					file.set("inventory.Slot_"+i+".type"  , inv.getItem(i).getItemType().getID() + "");
					file.set("inventory.Slot_"+i+".amount", inv.getItem(i).getAmount()			 + "");
				}
			}
			if(itemAmount<inv.getSize()){
				for(int i = itemAmount; i<inv.getSize(); i++){
					if(inv.getItem(i)!=null){
						file.set("inventory.Equipment_"+i+".type"  , inv.getItem(i).getItemType().getID() + "");
						file.set("inventory.Equipment_"+i+".amount", inv.getItem(i).getAmount()			  + "");
					}
				}
			}
		}
		
		FileManager.getFileManager().saveFile(FileManager.PLAYER);
	}
	
	public Entity Load(Class<?> entityClass, File file){
		
		Location loc = new Location(Integer.parseInt(file.get("Location.X").get(0)), Integer.parseInt(file.get("Location.Y").get(0)));
		loc.setLocation(loc.getX()*Map.DEFAULT_SQUARESIZE, loc.getY()*Map.DEFAULT_SQUARESIZE);
		
		Entity entity;		
		try {
			entity = (Entity) entityClass.getConstructor(Location.class).newInstance(loc);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
		for(String key: file.getSubkey("inventory")){
			Item item;
			int slot;
			if(key.substring(0, "Slot".length()).equals("Slot")){
				item = new Item(ItemType.getItemType(file.get("inventory."+key+".type").get(0)));
				slot = Integer.parseInt(key.substring("Slot_".length()));
			}else{
				EquipmentType type = (EquipmentType) ItemType.getItemType(file.get("inventory."+key+".type").get(0));
				item = type.createEquipment();
				slot = Integer.parseInt(key.substring("Equipment_".length()));
			}
			item.setAmount(Integer.parseInt(file.get("inventory."+key+".amount").get(0)));
			((EntityInventory)entity).getInventory().setItem(slot, item);
		}
		
		return entity;	
	}

}
