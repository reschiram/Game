package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import data.Funktions;
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
	private ServerMessageManager serverMessageManager;
	
	private Queue<ServerTask> serverTasks = new Queue<>();
	
	Server(ServerManager serverManager){
		
		this.connectionHandler = new ConnectionHandler(this);
		this.serverManager = serverManager;
		this.serverMessageManager = new ServerMessageManager(this);
	}
	
	void openConnection() throws ServerPortException{

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
					serverMessageManager.tick();

					while(!serverTasks.isEmpty()){
						ServerTask task = serverTasks.get();
						serverTasks.remove();
						ServerClient sc = connectionHandler.getServerClient(task.getClientID());
						task.act(sc, serverManager);
					}
					
					connectionHandler.flushAll();
					Funktions.wait(1);
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

	private void handleNewClient(Socket client) {
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

	ServerManager getServerManager() {
		return serverManager;
	}

	void sendToServerClient(ServerMessage serverMessage) {
		this.serverMessageManager.addMessage(serverMessage);
	}

	boolean isConnected() {
		return this.serverSocket!=null && this.serverSocket.isBound();
	}

	public void tick() {
		this.connectionHandler.tick();
	}

	public ArrayList<Long> getConnectedClients() {
		return this.connectionHandler.getConnectedClients();
	}

	public void addServerTask(ServerTask serverTask) {
		this.serverTasks.add(serverTask);
	}

	ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}
}
