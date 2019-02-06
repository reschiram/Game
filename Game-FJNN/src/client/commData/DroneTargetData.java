package client.commData;

import java.nio.ByteBuffer;
import java.util.Arrays;

import Data.Location;
import data.readableData.BooleanData;
import data.readableData.IntegerData;
import data.readableData.ReadableData;
import game.entity.player.playerDrone.BDroneTarget;
import game.entity.player.playerDrone.DDroneTarget;

public class DroneTargetData extends ReadableData<DroneTargetInfos>{
	
	private IntegerData blockPosX = new IntegerData("blockPos_X");
	private IntegerData blockPosY = new IntegerData("blockPos_Y");
	private IntegerData resID = new IntegerData("resID");
	private BooleanData done = new BooleanData("isDone");
	private BooleanData isBuild = new BooleanData("isBuild");
	private BooleanData isNull = new BooleanData("isNull");
	private BooleanData doAdd = new BooleanData("doAdd");

	public DroneTargetData(String name) {
		super(name, 4 + 4 + 4 + 1 + 1 + 1);
	}

	@Override
	public void readData(byte[] data) throws Exception {
		blockPosX	.readData(Arrays.copyOfRange(data, 						0, 4						));
		blockPosY	.readData(Arrays.copyOfRange(data, 						4, 4 + 4					));
		resID		.readData(Arrays.copyOfRange(data, 					4 + 4, 4 + 4 + 4				));
		done		.readData(Arrays.copyOfRange(data, 				4 + 4 + 4, 4 + 4 + 4 + 1			));
		isBuild		.readData(Arrays.copyOfRange(data, 			4 + 4 + 4 + 1, 4 + 4 + 4 + 1 + 1		));
		isNull		.readData(Arrays.copyOfRange(data, 		4 + 4 + 4 + 1 + 1, 4 + 4 + 4 + 1 + 1 + 1	));
		doAdd		.readData(Arrays.copyOfRange(data, 	4 + 4 + 4 + 1 + 1 + 1, 4 + 4 + 4 + 1 + 1 + 1 + 1));

		this.data = new DroneTargetInfos(resID.getData().intValue(),
				new Location(blockPosX.getData().intValue(), blockPosY.getData().intValue()),
				done.getData().booleanValue(), isBuild.getData().booleanValue(), isNull.getData().booleanValue(),
				doAdd.getData().booleanValue());
	}

	@Override
	public void readData(Object data) throws Exception {
		if(data instanceof BDroneTarget) {
			BDroneTarget target = (BDroneTarget) data;
			this.blockPosX.readData(target.getBlockLocation().getX());
			this.blockPosY.readData(target.getBlockLocation().getY());
			this.resID.readData(target.getID());
			this.done.readData(target.isDone());
			this.isBuild.readData(true);
			this.isNull.readData(false);
			this.doAdd.readData(true);
		} else if(data instanceof DDroneTarget) {
			DDroneTarget target = (DDroneTarget) data;
			this.blockPosX.readData(target.getBlockLocation().getX());
			this.blockPosY.readData(target.getBlockLocation().getY());
			this.resID.readData(-1);
			this.done.readData(target.isDone());
			this.isBuild.readData(false);
			this.isNull.readData(false);
			this.doAdd.readData(true);
		} else if(data instanceof DroneTargetInfos) {
			DroneTargetInfos target = (DroneTargetInfos) data;
			this.blockPosX.readData(target.getBlockLocation().getX());
			this.blockPosY.readData(target.getBlockLocation().getY());
			this.resID.readData(target.getResID());
			this.done.readData(target.isDone());
			this.isBuild.readData(target.isBuild());
			this.isNull.readData(target.isNull());
			this.doAdd.readData(target.doAdd());
		} else {
			this.blockPosX.readData(-1);
			this.blockPosY.readData(-1);
			this.resID.readData(-1);
			this.done.readData(true);
			this.isBuild.readData(false);
			this.isNull.readData(true);
			this.doAdd.readData(false);
		}

		this.data = new DroneTargetInfos(resID.getData().intValue(),
				new Location(blockPosX.getData().intValue(), blockPosY.getData().intValue()),
				done.getData().booleanValue(), isBuild.getData().booleanValue(), isNull.getData().booleanValue(),
				doAdd.getData().booleanValue());
	}

	@Override
	public void readString(String data) throws Exception {
		String[] infos = data.split("|");
		blockPosX.readData(infos[0]);
		blockPosY.readData(infos[1]);
		resID.readData(infos[2]);
		done.readData(infos[3]);
		isBuild.readData(infos[4]);
		isNull.readString(infos[5]);
		doAdd.readString(infos[6]);

		this.data = new DroneTargetInfos(resID.getData().intValue(),
				new Location(blockPosX.getData().intValue(), blockPosY.getData().intValue()),
				done.getData().booleanValue(), isBuild.getData().booleanValue(), isNull.getData().booleanValue(),
				doAdd.getData().booleanValue());
	}

	@Override
	public byte[] toData() throws Exception {
		byte[] data = new byte[this.byteLength];
		ByteBuffer.wrap(data).put(this.blockPosX.toData());
		ByteBuffer.wrap(data).put(this.blockPosY.toData());
		ByteBuffer.wrap(data).put(this.resID.toData());
		ByteBuffer.wrap(data).put(this.done.toData());
		ByteBuffer.wrap(data).put(this.isBuild.toData());
		ByteBuffer.wrap(data).put(this.isNull.toData());
		ByteBuffer.wrap(data).put(this.doAdd.toData());
		return null;
	}

	@Override
	public String toString() {
		return blockPosX.toString() + "|" +blockPosY.toString() + "|" + resID.toString() + "|" + done.toString() + "|" + isBuild.toString() + "|" + isNull.toString() + "|" + doAdd.toString();
	}

	@Override
	public DroneTargetData clone() {
		DroneTargetData targetData = new DroneTargetData(this.getName());
		if(this.data != null) {
			try {
				targetData.readData(this.data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return targetData;
	}

}
