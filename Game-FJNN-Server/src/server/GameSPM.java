package server;

import Data.Location;
import client.GameCPM;
import data.PackageType;
import data.entities.ServerDroneEntity;
import data.entities.ServerItemEntity;
import data.entities.ServerPlayerEntity;
import data.server.request.ServerEntityRequest;
import game.entity.type.EntityType;
import game.map.MapGenerationData;

public class GameSPM extends GameCPM{
	
	public GameSPM() {
		super();
	}
	
	public PackageType[] getMapMessages(MapGenerationData mapData) throws Exception {
		int width = mapData.getWidth();
		int height = mapData.getHeight();

		int messageCount = (int) Math.ceil(((double) (width * height)) / ((double) MapDownloadData_DataCount));

		PackageType[] data = new PackageType[messageCount + 1];
		data[0] = PackageType.readPackageData(DataPackage_MapDownloadInfos, width, height, mapData.getSeed(), messageCount);

		for (int i = 0; i < messageCount; i++) {
			data[i + 1] = getMapDataPackage(i, mapData);
		}
		
		System.out.println("Test null PackageTypes: ");
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) System.out.println("Null: "+i);
			else {
				String message = "Null: "+i+" at:";
				for(int a = 0; a < data[i].getDataStructures().length; a++) {
					if(data[i].getDataStructures()[a] == null || data[i].getDataStructures()[a].getData() == null)message+=a+","; 
				}
				if(!message.equals("Null: "+i+" at:"))System.out.println(message);
			}
		}
		System.out.println("=======");
		
		return data;
	}

	public PackageType getMapDataPackage(int id, MapGenerationData mapData) throws Exception {
		int width = mapData.getWidth();
		int height = mapData.getHeight();
		
		Integer[] content = new Integer[1 + (MapDownloadData_DataCount * 4)];
		content[0] = id;
		
		for (int a = 0; a < MapDownloadData_DataCount; a++) {
			int index = (id * MapDownloadData_DataCount) + a;
			int y = index / width;
			int x = index - (y * width);
			
			if(x < width && y < height) {
				content[1 + (a * 4) + 0] = mapData.getGroundData()[x][y][0];
				content[1 + (a * 4) + 1] = mapData.getGroundData()[x][y][1];
				content[1 + (a * 4) + 2] = mapData.getBuildData ()[x][y][0];
				content[1 + (a * 4) + 3] = mapData.getBuildData ()[x][y][1];
			}else {
				content[1 + (a * 4) + 0] = 0;
				content[1 + (a * 4) + 1] = 0;
				content[1 + (a * 4) + 2] = 0;
				content[1 + (a * 4) + 3] = 0;
			}
		}
		return PackageType.readPackageData(DataPackage_MapDownloadData, content); 
	}

	public PackageType passRequestMessage(ServerEntityRequest request) throws Exception {
		if(request.getServerEntity() instanceof ServerDroneEntity) {
			ServerDroneEntity sde = (ServerDroneEntity) request.getServerEntity();
			System.out.println("RequestEntity: "+sde);
			
			return PackageType.readPackageData(DataPackage_EntityCreationRequest_Drone, request.getRequestID(),
					request.getClientRequestID(), EntityType.Drone.getID(),
					sde.getDroneType(), sde.getDroneHost().getId(),
					sde.getBlockLocation().getX(), sde.getBlockLocation().getY())
			;
		}else if(request.getServerEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity spe = (ServerPlayerEntity) request.getServerEntity();
			System.out.println("RequestEntity: "+spe);
			
			return PackageType.readPackageData(DataPackage_EntityCreationRequest_Player, request.getRequestID(),
					request.getClientRequestID(), EntityType.Player.getID(),
					spe.getBlockLocation().getX(), spe.getBlockLocation().getY())
			;
		}else if(request.getServerEntity() instanceof ServerItemEntity) {
			ServerItemEntity sie = (ServerItemEntity) request.getServerEntity();
			System.out.println("RequestEntity: "+sie);
			
			return PackageType.readPackageData(DataPackage_EntityCreationRequest_ItemEntity, request.getRequestID(),
					request.getClientRequestID(), EntityType.ItemEntity.getID(), sie.getType().getID(),
					sie.getBlockLocation().getX(), sie.getBlockLocation().getY())
			;
		}
		return null;
	}

	public PackageType answerRequest(long currentClientID, ServerEntityRequest request, int currentClientRequestID)throws Exception {
		return PackageType.readPackageData(DataPackage_EntityCreationResponse,
				currentClientRequestID, request.getServerEntity().getId(),
				request.getServerEntity().getExtraInfos(currentClientID))
		;			
	}

	public PackageType createDroneEnergyUpdateMessage(int droneID, double energyLoad, boolean isLoading) throws Exception {
		return PackageType.readPackageData(DataPackage_EntityCreationResponse,
				droneID, energyLoad, isLoading);
	}

	public PackageType createDroneTargetUpdateMessage(int droneID, Location blockTarget, int targetLevel) throws Exception {
		return PackageType.readPackageData(DataPackage_EntityCreationResponse,
				droneID, blockTarget.getX(), blockTarget.getY(), targetLevel);
	}

	public PackageType createDroneActionTargetUpdateMessage(int droneID, int nextActionTargetId, int typeId) throws Exception {
		return PackageType.readPackageData(DataPackage_EntityCreationResponse,
				droneID, nextActionTargetId, typeId);
	}
}