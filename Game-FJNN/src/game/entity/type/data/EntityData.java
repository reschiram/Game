package game.entity.type.data;

public abstract class EntityData {

	public static int LIGHTENTITYDATA 	  = 0;
	public static int ENTITYINVENTOTYDATA = 1;

	private int code = -1;
	
	protected EntityData(){
		if(this instanceof LightEntityData		) code = LIGHTENTITYDATA;
		if(this instanceof EntityInventoryData	) code = ENTITYINVENTOTYDATA;
	}
	
	public int getCode(){
		return code;
	}
}
