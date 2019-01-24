package console;

import java.util.ArrayList;

import console.gui.ConsoleGUI;
import data.Queue;
import data.exceptions.UnknownUserException;
import data.exceptions.UserAlreadyKnwonException;
import data.exceptions.UserDatabaseReadingException;
import data.exceptions.UserInfoException;
import data.exceptions.server.InvalidServerClientIDException;
import data.user.User;
import main.ServerMain;
import server.KickTask;
import test.data.CMD;
import test.data.CMDAction;

public class ConsoleManager {
	
	private class Task {
		private String cmd;
		private String[] args;
		
		public Task(String cmd, String[] args) {
			super();
			this.cmd = cmd;
			this.args = args;
		}
	}
	
	private ServerMain main;
	private ConsoleGUI gui;
	private ArrayList<CMD> commands = new ArrayList<>();
	private Queue<Task> tasks = new Queue<>();
	
	public ConsoleManager(ServerMain main) {
		this.main = main;
		loadCommands();		
		
		gui = new ConsoleGUI(this);
	}

	public void invokeCommand(String cmd, String... args) {
		tasks.add(new Task(cmd, args));
	}

	public ArrayList<CMD> getCommands() {
		return commands;
	}
	
	public void tick() {
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
	
	public ConsoleGUI getGUI() {
		return this.gui;
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
				ArrayList<Long> connectedClients = main.getGameSM().getAllConnectedClients();
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

		CMD kickClient = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 1) return false;
				try {
					String reason = "no reason given";
					if(args.length >= 2) reason += args[1];
					main.getGameSM().getServerManager().addServerTask(new KickTask(Long.parseLong(args[0]), reason));
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
		
		CMD addUser = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 2) return false;
				try {
					main.getGameSM().getServerUserManager().registerNewUser(new User("", args[0], args[1]));
					gui.println("User: \""+args[0]+"\" has been succesfully registered.");
				} catch (UserDatabaseReadingException e) {
					gui.println("User: \""+args[0]+"\" could not be added. Reason: Error ("+e.getErrorMessage()+")");
				} catch (UserInfoException e) {
					gui.println("User: \""+args[0]+"\" could not be added. Reason: wrong user info (username = \""+args[0]+"\", password = \""+args[1]+"\")");					
				} catch (UserAlreadyKnwonException e) {
					gui.println("User: \""+args[0]+"\" could not be added. Reason: user already known.");	
				}
				return true;
			}
		}, "Add User: adds a new user to the userdb.", "addUser");	
		addUser.setMedatoryArgs("username, password");
		
		CMD removeUser = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 1) return false;
				try {
					main.getGameSM().getServerUserManager().delteUser(args[0]);
					gui.println("User: \""+args[0]+"\" has been succesfully delteted.");
				} catch (UserDatabaseReadingException e) {
					gui.println("User: \""+args[0]+"\" could not be delteted. Reason: Error ("+e.getErrorMessage()+")");
				} catch (UnknownUserException e) {
					gui.println("User: \""+args[0]+"\" could not be delteted. Reason: user is unknown.");	
				}
				return true;
			}
		}, "Delete User: delets a new user from the userdb.", "removeUser");	
		removeUser.setMedatoryArgs("userID");
		
		CMD userInfoPage = new CMD(new CMDAction() {
			@Override
			public boolean act(String[] args) {
				String msg = "This is an overview over all registered user. \n";
				ArrayList<User> registeredUsers = main.getGameSM().getServerUserManager().getAllRegisteredUsers();
				ArrayList<User> validatedOnlineUsers = main.getGameSM().getServerUserManager().getAllValidatedOnlineUsers();
				msg += "currently Online: "+validatedOnlineUsers.size()+" / "+registeredUsers.size();
				if(args.length >= 1 && args[0].equalsIgnoreCase("-listUsers")) {
					msg += "\nregistered Users: ";
					for(User user: registeredUsers)msg+= user.toString()+", ";
				}
				gui.println(msg);
				return true;
			}
		}, "User Info Page: views a list of information about all registered Users.", "userInfos");
		userInfoPage.setOptions("listUsers");
		
		
		this.commands.add(help);
		this.commands.add(onlineClients);
		this.commands.add(kickClient);
		this.commands.add(addUser);
		this.commands.add(removeUser);
		this.commands.add(userInfoPage);
	}
}
