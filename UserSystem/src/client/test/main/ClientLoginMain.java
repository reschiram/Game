package client.test.main;

import client.test.TestClient;
import client.test.gui.LoginGUI;
import data.Funktions;
import data.events.ClientLoginEvent;
import test.client.main.ClientTestMain;

public class ClientLoginMain {

	public static void main(String args[]){
		if(args.length==2 && !args[1].equals(""))new ClientLoginMain(args[0], Integer.parseInt(args[1]));
		else new ClientLoginMain(args[0], 12345);
	}

	private TestClient testClient;
	private ClientTestMain testMain;
	
	private LoginGUI gui;
	
	public ClientLoginMain(String ip, int port) {
		this.testClient = new TestClient(this, ip, port);		
		this.gui = new LoginGUI(this);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (testMain == null) {
					testClient.tick();
					Funktions.wait(1);
				}
			}
		}).start();
	}

	public void println(String text) {
		if(this.testMain !=null)this.testMain.getGUI().println(text);
	}

	public void login(String username, String password) {
		this.testClient.login(username, password);
	}

	public void loginSuccesfull() {
		this.gui.destroy();
		this.testMain = new ClientTestMain(this.testClient.getClientManager());
	}

	public void loginFailed(ClientLoginEvent event) {
		this.gui.println("Login failed for user:"+event.getUser().getUsername());
	}

}
