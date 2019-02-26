package client.commData;

import java.nio.ByteBuffer;
import java.util.Arrays;

import Data.Location;
import data.readableData.ReadableData;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;

public class DroneTargetData extends ReadableData<DroneTargetInfos>{
	
	private int blockPosX;
	private int blockPosY;
	private int resID;
	private boolean done;
	private boolean isBuild;
	private boolean isNull;
	private boolean doAdd;

	public DroneTargetData(String name) {
		super(name, 4 + 4 + 4 + 1 + 1 + 1 + 1);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		blockPosX 	= readInt		(Arrays.copyOfRange(data, 						0, 4						));
		blockPosY 	= readInt		(Arrays.copyOfRange(data, 						4, 4 + 4					));
		resID		= readInt		(Arrays.copyOfRange(data, 					4 + 4, 4 + 4 + 4				));
		done		= readboolean	(Arrays.copyOfRange(data, 				4 + 4 + 4, 4 + 4 + 4 + 1			));
		isBuild		= readboolean	(Arrays.copyOfRange(data, 			4 + 4 + 4 + 1, 4 + 4 + 4 + 1 + 1		));
		isNull		= readboolean	(Arrays.copyOfRange(data, 		4 + 4 + 4 + 1 + 1, 4 + 4 + 4 + 1 + 1 + 1	));
		doAdd		= readboolean	(Arrays.copyOfRange(data, 	4 + 4 + 4 + 1 + 1 + 1, 4 + 4 + 4 + 1 + 1 + 1 + 1));

		this.data = new DroneTargetInfos(resID, new Location(blockPosX, blockPosY), done, isBuild, isNull, doAdd);
	}

	private boolean readboolean(byte[] data) {
		return (data[0] == 1);
	}

	private int readInt(byte[] data) {
		return ByteBuffer.wrap(data).getInt();
	}

	@Override
	public void readData(Object data) throws Exception {
		if(data instanceof BDroneTarget) {
			BDroneTarget target = (BDroneTarget) data;
//			System.out.println("publish BDroneTarget: " + target.isDone() + "|" + false + "|" + target.getBlockLocation());
			this.blockPosX = target.getBlockLocation().getX();
			this.blockPosY = target.getBlockLocation().getY();
			this.resID = target.getID();
			this.done = target.isDone();
			this.isBuild = true;
			this.isNull = false;
			this.doAdd = true;
		} else if(data instanceof DDroneTarget) {
			DDroneTarget target = (DDroneTarget) data;
//			System.out.println("publish DDroneTarget: " + target.isDone() + "|"  + false + "|" + target.getBlockLocation());
			this.blockPosX = target.getBlockLocation().getX();
			this.blockPosY = target.getBlockLocation().getY();
			this.resID = 0;
			this.done = target.isDone();
			this.isBuild = false;
			this.isNull = false;
			this.doAdd = true;
		} else if(data instanceof DroneTargetInfos) {
			DroneTargetInfos target = (DroneTargetInfos) data;
//			System.out.println("publish CDroneTargetInfos: " + target.isDone() + "|"  + target.isNull() + "|" + target.getBlockLocation());
			this.blockPosX = target.getBlockLocation().getX();
			this.blockPosY = target.getBlockLocation().getY();
			this.resID = target.getResID();
			this.done = target.isDone();
			this.isBuild = target.isBuild();
			this.isNull = target.isNull();
			this.doAdd = target.doAdd();
		} else {
//			System.out.println("publish null: " + true + "|"  + true + "|" + 0 + "|" + 0);
			this.blockPosX = 0;
			this.blockPosY = 0;
			this.resID = 0;
			this.done = true;
			this.isNull = true;
		}

		this.data = new DroneTargetInfos(resID, new Location(blockPosX, blockPosY), done, isBuild, isNull, doAdd);
	}

	@Override
	public void readString(String data) throws Exception {
		String[] infos = data.split("|");
		blockPosX = Integer.parseInt(infos[0]);
		blockPosY = Integer.parseInt(infos[1]);
		resID = Integer.parseInt(infos[2]);
		done = Boolean.parseBoolean(infos[3]);
		isBuild = Boolean.parseBoolean(infos[4]);
		isNull = Boolean.parseBoolean(infos[5]);
		doAdd = Boolean.parseBoolean(infos[6]);

		this.data = new DroneTargetInfos(resID, new Location(blockPosX, blockPosY), done, isBuild, isNull, doAdd);
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength];
		ByteBuffer warpper = ByteBuffer.wrap(data);
		warpper.putInt(blockPosX);
		warpper.putInt(blockPosY);
		warpper.putInt(resID);
		warpper.put(done ? new byte[]{1} : new byte[]{0});
		warpper.put(isBuild ? new byte[]{1} : new byte[]{0});
		warpper.put(isNull ? new byte[]{1} : new byte[]{0});
		warpper.put(doAdd ? new byte[]{1} : new byte[]{0});
		return data;
	}

	@Override
	public String toString() {
		return blockPosX + "|" +blockPosY + "|" + resID + "|" + done + "|" + isBuild + "|" + isNull + "|" + doAdd;
	}

	@Override
	public DroneTargetData clone() {
		DroneTargetData targetData = new DroneTargetData(this.getName());
		try {			
			targetData.blockPosX = this.blockPosX;
			targetData.blockPosY = this.blockPosY;
			targetData.resID = this.resID;
			targetData.done = this.done;
			targetData.isBuild = this.isBuild;
			targetData.isNull = this.isNull;
			targetData.doAdd = this.doAdd;
			
			targetData.data = new DroneTargetInfos(resID, new Location(blockPosX, blockPosY), done, isBuild, isNull, doAdd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetData;
	}

}
