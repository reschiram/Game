package test.server.main;

import java.util.ArrayList;

import data.DataPackage;
import data.Funktions;
import data.PackageType;
import data.Queue;
import data.exceptions.server.InvalidServerClientIDException;
import data.readableData.StringData;
import test.server.TestServer;
import test.server.gui.GUI;

public class ServerTestMain {
	
	public static ArrayList<String> CMD_Help = new ArrayList<>();
	public static ArrayList<String> CMD_SendTextToClient = new ArrayList<>();
	
	public static void main(String[] args){
		new ServerTestMain();
	}
	
	private GUI gui;
	
	private class Task{
		private String cmd;
		private String args[];
		
		private Task(String cmd, String... args) {
			this.cmd = cmd;
			this.args = args;
		}
	}
	
	private TestServer testServer;	
	private Queue<Task> tasks = new Queue<>();
	
	public ServerTestMain(){		
		loadCommands();		
		
		gui = new GUI(this);
		
		loadServer();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					tick();
					Funktions.wait(1);
				}
			}
		}).start();
	}


	private void tick() {
		if(testServer.isConnected()) testServer.tick();
		
		while(!tasks.isEmpty()){
			Task task = this.tasks.get();
			tasks.remove();
			
			if(CMD_Help.contains(task.cmd)){
				gui.printHelp();
			}else if(CMD_SendTextToClient.contains(task.cmd) && task.args.length >= 2){
				try {
					this.testServer.sendPackage(Long.parseLong(task.args[0]), DataPackage.getPackage(PackageType.readPackageData(64, task.args[1])));
					gui.println("Message: \""+task.args[1]+"\" was send to client: "+task.args[0]);		
				} catch (Exception e) {
					gui.println("Message: \""+task.args[1]+"\" could not be send. Reason: invalid text");			
				} catch (InvalidServerClientIDException e) {
					gui.println("Message: \""+task.args[1]+"\" could not be send. Reason: client not found: "+task.args[0]);		
				}
			}else {
				String error = "Unknown command: \""+task.cmd+"\" with arguments: ";
				for(String arg: task.args) error += "["+arg+"] ";
				gui.println(error);
			}
		}
	}

	private void loadCommands() {
		CMD_Help.add("help");
		CMD_Help.add("h");
		CMD_Help.add("?");
		
		CMD_SendTextToClient.add("sendTextToClient");
		CMD_SendTextToClient.add("sttc");
	}

	private void loadServer() {		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		DataPackage.setType(new PackageType(30, "test2", new StringData("test30", 30), new StringData("test50", 50), new StringData("test20", 20)));
		
		testServer = new TestServer();
	}

	public void invokeCommand(String cmd, String... args) {
		this.tasks.add(new Task(cmd, args));
	}
}
