package test.server.main;

import java.util.ArrayList;

import data.DataPackage;
import data.Funktions;
import data.PackageType;
import data.Queue;
import data.exceptions.server.InvalidServerClientIDException;
import data.readableData.StringData;
import server.ServerManager;
import test.data.CMD;
import test.data.CMDAction;
import test.data.Tickable;
import test.server.TestServer;
import test.server.gui.GUI;

public class ServerTestMain {
	
	public static void main(String[] args){
		new ServerTestMain();
	}
	
	private GUI gui;
	private ArrayList<Tickable> tickables = new ArrayList<>();
	private ArrayList<CMD> commands = new ArrayList<>();
	
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

		for(int i = 0; i < tickables.size(); i++) {
			Tickable tickable = tickables.get(i);
			tickable.tick();
		}
		
		handleTasks();		
	}

	private void handleTasks() {
		while(!tasks.isEmpty()){
			Task task = this.tasks.get();
			tasks.remove();

			for(int i = 0; i < commands.size(); i++) {
				CMD cmd = commands.get(i);
				if(cmd.isCommand(task.cmd)){
					if(cmd.getAction().act(task.args)) return;
					else break;
				}
			}
			
			String error = "Unknown command: \""+task.cmd+"\" with arguments: ";
			for(String arg: task.args) error += "["+arg+"] ";
			gui.println(error);
		}
	}


	private void loadCommands() {
		CMD help = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				gui.printHelp();
				return true;
			}
		}, "Help: views this help page.", "help", "h", "?");		

		CMD onlineClients = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				String msg = "This is an over view of all connected Players: \n";
				msg += "currently connected: ";
				ArrayList<Long> connectedClients = testServer.getAllConnectedClients();
				msg += connectedClients.size()+". \n";
				if(args.length >= 1 && args[0].equalsIgnoreCase("-info")){
					msg += "This is the list of the clientIDs: ";
					for(long id : connectedClients)msg += id+", ";
					msg += "\n";
				}
				msg+= "====";
				gui.println(msg);
				return true;
			}
		}, "Connected Clients: views a page where infos about all connected clients are listet.", "onlineClients", "oc");
		onlineClients.setOptions("info");

		CMD sendTextToClient = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 2) return false;
				try {
					testServer.sendPackage(Long.parseLong(args[0]), DataPackage.getPackage(PackageType.readPackageData(64, args[1])));
					gui.println("Message: \""+args[1]+"\" was send to client: "+args[0]);		
				} catch (Exception e) {
					gui.println("Message: \""+args[1]+"\" could not be send. Reason: invalid text");			
				} catch (InvalidServerClientIDException e) {
					gui.println("Message: \""+args[1]+"\" could not be send. Reason: client not found: "+args[0]);		
				}
				return true;
			}
		}, "Send Text To Client: sends a text message to the client.", "sendTextToClient", "sttc");		
		sendTextToClient.setMedatoryArgs("clientID", "Message (max: 63 chars)");

		CMD sendTextToAllClients = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 1) return false;
				try {
					testServer.sendPackageToAllClients(DataPackage.getPackage(PackageType.readPackageData(64, args[0])));
					gui.println("Message: \""+args[0]+"\" was send to alls clients.");		
				} catch (Exception e) {
					gui.println("Message: \""+args[0]+"\" could not be send. Reason: invalid text");			
				}
				return true;
			}
		}, "Send Text To All Clients: sends a text message to all connected clients.", "sendTextToAllClients", "sttac");
		sendTextToClient.setMedatoryArgs("Message (max: 63 chars)");

		CMD kickClient = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 1) return false;
				try {
					String reason = "no reason given";
					if(args.length >= 2) reason += args[1];
					testServer.kickClient(Long.parseLong(args[0]), reason);
					gui.println("Client: "+Long.parseLong(args[0])+" has been kicked.");		
				} catch (Exception e) {
					gui.println("Client: "+Long.parseLong(args[0])+" could not be kicked. Reason: invalid reason input");				
				} catch (InvalidServerClientIDException e) {
					gui.println("Client: "+Long.parseLong(args[0])+" could not be kicked. Reason: No client with the id "+Long.parseLong(args[0])+" was found.");	
				}
				return true;
			}
		}, "Kick: Closes the connection from a client.", "KickClient", "kick");
		kickClient.setMedatoryArgs("clientID");
		
		this.commands.add(help);
		this.commands.add(onlineClients);
		this.commands.add(sendTextToClient);
		this.commands.add(sendTextToAllClients);
		this.commands.add(kickClient);
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

	public ServerManager getServerManager() {
		return this.testServer.getServerManager();
	}


	public void registerTick(Tickable tickable) {
		this.tickables.add(tickable);
	}
	
	public void removeTick(Tickable tickable) {
		this.tickables.remove(tickable);
	}
	
	public void registerCMD(CMD cmd) {
		this.commands.add(cmd);
	}

	public void removeCMD(CMD cmd) {
		this.commands.remove(cmd);
	}


	public ArrayList<CMD> getCommands() {
		return this.commands;
	}
}
