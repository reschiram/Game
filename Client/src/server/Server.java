package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private final static int Port = 12345;
	
	private ServerSocket serverSocket;	
	private boolean run = true;
	
	public Server(){
		try {
			this.serverSocket = new ServerSocket(Port);
		} catch (IOException e) {e.printStackTrace();}
		
		loadClientAccepter();
	}

	private void loadClientAccepter() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(run){
					try {
						Socket client = serverSocket.accept();
						handleNewClient(client);
					} catch (IOException e) {e.printStackTrace();}
				}
			}
		}).start();
	}

	protected void handleNewClient(Socket client) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
			}
		}).start();
	}

}
