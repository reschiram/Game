package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import data.Queue;
import data.exceptions.UnsupportedPackageException;
import data.exceptions.handler.DefaultExceptionHandler;
import data.exceptions.server.ServerPortException;

public class Server {

	private final static int Port = 12345;
	
	private ServerSocket serverSocket;	
	private boolean run = true;

	private ConnectionHandler connectionHandler;
	private ServerManager serverManager;
	
	private Queue<ServerMessage> serverMessages = new Queue<>();
	
	public Server(ServerManager serverManager){
		
		this.connectionHandler = new ConnectionHandler(this);
		this.serverManager = serverManager;
	}
	
	public void openConnection() throws ServerPortException{

		try {
			this.serverSocket = new ServerSocket(Port);
		} catch (IOException e) {
			throw new ServerPortException(e, Port);
		}
		
		loadClientAccepter();
		loadServerMessanger();
	}

	private void loadServerMessanger() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(run){
					while(!serverMessages.isEmpty()){
						ServerMessage message = serverMessages.get();
						serverMessages.remove();
						connectionHandler.getServerClient(message.getId()).sendToClient(message.getMessage());
					}
					try {
						synchronized (Thread.currentThread()) {
							Thread.currentThread().wait(1);							
						}
					} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}).start();
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
					try {
						sc.scanForIncomingData();
					} catch (UnsupportedPackageException e) {
						DefaultExceptionHandler.getDefaultExceptionHandler().getDefaultHandler_UnsupportedPackageException().handleError(e);
					}
				}
			}
		}).start();
	}

	public ServerManager getServerManager() {
		return serverManager;
	}

	public void sendToServerClient(ServerMessage serverMessage) {
		serverMessages.add(serverMessage);
	}
}
