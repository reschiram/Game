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
	public static ArrayList<String> CMD_OnlineClients = new ArrayList<>();
	public static ArrayList<String> CMD_SendTextToClient = new ArrayList<>();
	public static ArrayList<String> CMD_SendTextToAllClients = new ArrayList<>();
	public static ArrayList<String> CMD_KickClient = new ArrayList<>();
	
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
			}else if(CMD_OnlineClients.contains(task.cmd)){
				String msg = "This is an over view of all connected Players: \n";
				msg += "currently connected: ";
				ArrayList<Long> connectedClients = testServer.getAllConnectedClients();
				msg += connectedClients.size()+". \n";
				if(task.args.length >= 1 && task.args[0].equalsIgnoreCase("info")){
					msg += "This is the list of the clientIDs: ";
					for(long id : connectedClients)msg += id+", ";
					msg += "\n";
				}
				msg+= "====";
				this.gui.println(msg);
			}else if(CMD_SendTextToClient.contains(task.cmd) && task.args.length >= 2){
				try {
					this.testServer.sendPackage(Long.parseLong(task.args[0]), DataPackage.getPackage(PackageType.readPackageData(64, task.args[1])));
					gui.println("Message: \""+task.args[1]+"\" was send to client: "+task.args[0]);		
				} catch (Exception e) {
					gui.println("Message: \""+task.args[1]+"\" could not be send. Reason: invalid text");			
				} catch (InvalidServerClientIDException e) {
					gui.println("Message: \""+task.args[1]+"\" could not be send. Reason: client not found: "+task.args[0]);		
				}
			}else if(CMD_SendTextToAllClients.contains(task.cmd) && task.args.length >= 1){
				try {
					this.testServer.sendPackageToAllClients(DataPackage.getPackage(PackageType.readPackageData(64, task.args[0])));
					gui.println("Message: \""+task.args[0]+"\" was send to alls clients.");		
				} catch (Exception e) {
					gui.println("Message: \""+task.args[0]+"\" could not be send. Reason: invalid text");			
				}
			}else if(CMD_KickClient.contains(task.cmd) && task.args.length >= 1){
				try {
					String reason = "no reason given";
					if(task.args.length >= 2) reason += task.args[1];
					this.testServer.kickClient(Long.parseLong(task.args[0]), reason);
					gui.println("Client: "+Long.parseLong(task.args[0])+" has been kicked.");		
				} catch (Exception e) {
					gui.println("Client: "+Long.parseLong(task.args[0])+" could not be kicked. Reason: invalid reason input");				
				} catch (InvalidServerClientIDException e) {
					gui.println("Client: "+Long.parseLong(task.args[0])+" could not be kicked. Reason: No client with the id "+Long.parseLong(task.args[0])+" was found.");	
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
		
		CMD_SendTextToAllClients.add("sendTextToAllClients");
		CMD_SendTextToAllClients.add("sttac");
		
		CMD_OnlineClients.add("onlineClients");
		CMD_OnlineClients.add("oc");
		
		CMD_KickClient.add("KickClient");
		CMD_KickClient.add("kick");
	}

	private void loadServer() {		
		DataPackage.setType(new PackageType(64, "Unknown_Data", new StringData ("Text", DataPackage.MAXPACKAGELENGTH-1)));
		
		testServer = new TestServer(this);
	}

	public void invokeCommand(String cmd, String... args) {
		this.tasks.add(new Task(cmd, args));
	}


	public GUI getGUI() {
		return this.gui;
	}
}
