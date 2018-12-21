package client.test.main;

import java.util.Timer;
import java.util.TimerTask;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.readableData.StringData;
import client.test.gui.GUI;
import client.test.TestClient;

public class ClientTestMain {	
	
	private class Task{
		private int type;
		private String task;
		private String args[];
		
		private Task(int type, String task, String[] args) {
			this.type = type;
			this.task = task;
			this.args = args;
		}
	}

	private GUI gui;

	private Queue<Task> tasks = new Queue<>();
	
	public ClientTestMain(TestClient testClient){
		this.gui = new GUI(this);
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(!tasks.isEmpty()){
					Task task = tasks.get();
					tasks.remove();
					if(task.task.equals("send")){
						try {
							testClient.sendToServer(DataPackage.getPackage(PackageType.readPackageData(task.type, task.args)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(testClient!=null)testClient.tick();
			}
		};
		new Timer().schedule(task, 1, 1);
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType(30, "test2", new StringData("test30", 30), new StringData("test50", 50), new StringData("test20", 20)));
		
		this.gui.updatePackageTypeList();
	}

	public void addTask(String task, int type, String... string) {
		this.tasks.add(new Task(type, task, string));
	}

	public GUI getGUI() {
		return this.gui;
	}
}
