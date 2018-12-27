package server.test;

import java.util.ArrayList;

import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.exceptions.UnknownUserException;
import data.exceptions.UserAlreadyKnwonException;
import data.exceptions.UserDatabaseReadingException;
import data.exceptions.UserInfoException;
import data.user.User;
import server.ServerUserManager;
import test.data.CMD;
import test.data.CMDAction;
import test.server.main.ServerTestMain;

public class TestServer implements ClientConnectionValidationEventListener, ClientLogoutEventListener{
	
	public static void main(String args[]){
		new TestServer();
	}
	
	private ServerTestMain serverMain;
	private ServerUserManager serverUserManager;
	
	public TestServer(){
		serverMain = new ServerTestMain();
		serverUserManager = new ServerUserManager(serverMain.getServerManager(), "fdsa4321");
		
		serverUserManager.getUserEventManager().registerClientConnectionValidationEventListener(this, 5);
		serverUserManager.getUserEventManager().registerClientLogoutEventListener(this, 5);
		
		loadCommands();
		
		serverMain.registerTick(serverUserManager);
	}
	
	private void loadCommands() {
		CMD addUser = new CMD(new CMDAction() {			
			@Override
			public boolean act(String[] args) {
				if(args.length < 2) return false;
				try {
					serverUserManager.registerNewUser(new User("", args[0], args[1]));
					serverMain.getGUI().println("User: \""+args[0]+"\" has been succesfully registered.");
				} catch (UserDatabaseReadingException e) {
					serverMain.getGUI().println("User: \""+args[0]+"\" could not be added. Reason: Error ("+e.getErrorMessage()+")");
				} catch (UserInfoException e) {
					serverMain.getGUI().println("User: \""+args[0]+"\" could not be added. Reason: wrong user info (username = \""+args[0]+"\", password = \""+args[1]+"\")");					
				} catch (UserAlreadyKnwonException e) {
					serverMain.getGUI().println("User: \""+args[0]+"\" could not be added. Reason: user already known.");	
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
					serverUserManager.delteUser(args[0]);
					serverMain.getGUI().println("User: \""+args[0]+"\" has been succesfully delteted.");
				} catch (UserDatabaseReadingException e) {
					serverMain.getGUI().println("User: \""+args[0]+"\" could not be delteted. Reason: Error ("+e.getErrorMessage()+")");
				} catch (UnknownUserException e) {
					serverMain.getGUI().println("User: \""+args[0]+"\" could not be delteted. Reason: user is unknown.");	
				}
				return true;
			}
		}, "Delete User: delets a new user from the userdb.", "removeUser");	
		removeUser.setMedatoryArgs("userID");
		
		CMD userInfoPage = new CMD(new CMDAction() {
			@Override
			public boolean act(String[] args) {
				String msg = "This is an overview over all registered user. \n";
				ArrayList<User> registeredUsers = serverUserManager.getAllRegisteredUsers();
				ArrayList<User> validatedOnlineUsers = serverUserManager.getAllValidatedOnlineUsers();
				msg += "currently Online: "+validatedOnlineUsers.size()+" / "+registeredUsers.size();
				if(args.length >= 1 && args[0].equalsIgnoreCase("-listUsers")) {
					msg += "\nregistered Users: ";
					for(User user: registeredUsers)msg+= user.toString()+", ";
				}
				serverMain.getGUI().println(msg);
				return true;
			}
		}, "User Info Page: views a list of information about all registered Users.", "userInfos");
		userInfoPage.setOptions("listUsers");
		
		this.serverMain.registerCMD(addUser);
		this.serverMain.registerCMD(removeUser);
		this.serverMain.registerCMD(userInfoPage);
	}

	@Override
	public void clientLogout(ClientLogoutEvent event) {
		this.serverMain.getGUI().println("Client: "+event.getClientID()+" has logged out as user: "+event.getValidatedUser().getUsername()+" has logged out");
	}

	@Override
	public void handleClientLogginIn(ClientConnectionValidationEvent event) {
		if(event.isLoggedIn())this.serverMain.getGUI().println("Client: "+event.getClientID()+" has registered as User: "+event.getUser().toString());
		else this.serverMain.getGUI().println("Client: "+event.getClientID()+" has failed to Login");
	}


}
