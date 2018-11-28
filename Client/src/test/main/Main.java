package test.main;

import java.util.Timer;
import java.util.TimerTask;

import data.DataPackage;
import data.PackageType;
import data.Queue;
import data.readableData.DoubleData;
import data.readableData.IntegerData;
import data.readableData.StringData;
import test.TestClient;
import test.gui.*;

public class Main {	
	
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
	
	public static void main(String args[]){
		if(args.length==2 && !args[1].equals(""))new Main(args[0], Integer.parseInt(args[1]));
		else new Main(args[0],0);
	}

	private GUI gui;
	private TestClient testClient;

	private Queue<Task> tasks = new Queue<>();
	
	public Main(String ip, int port){
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
			}
		};
		new Timer().schedule(task, 1, 1);
		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType( 0, "Test", new IntegerData("Ganze Zahl"), new DoubleData("rationale Zahl"), new StringData ("Text", DataPackage.MAXPACKAGELENGTH-(1+4+8))));
		this.gui.updatePackageTypeList();
		
		System.out.println(ip +"|"+ port);
		this.testClient = new TestClient(this, ip, port);
	}

	public void addTask(String task, int type, String... string) {
		this.tasks.add(new Task(type, task, string));
	}

	public GUI getGUI() {
		return this.gui;
	}
}
