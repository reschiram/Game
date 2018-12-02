package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private final static int Port = 12345;
	
	private ServerSocket serverSocket;	
	private boolean run = true;

	private ConnectionHandler connectionHandler;
	private ServerManager serverManager;
	
	public Server(ServerManager serverManager){
		try {
			System.out.println(Port);
			this.serverSocket = new ServerSocket(Port);
		} catch (IOException e) {e.printStackTrace();}
		
		this.connectionHandler = new ConnectionHandler(this);
		this.serverManager = serverManager;
		
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
				ServerClient sc = connectionHandler.registerNewConnection(client);
				sc.acceptConnection();
				while(sc!=null && sc.isConnected()){
					sc.scanForIncomingData();
				}
			}
		}).start();
	}

	public ServerManager getServerManager() {
		return serverManager;
	}
}
