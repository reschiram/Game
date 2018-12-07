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
	
	private Queue<ServerMessage> serverMessages = new Queue<>();
	
	Server(ServerManager serverManager){
		
		this.connectionHandler = new ConnectionHandler(this);
		this.serverManager = serverManager;
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
					while(!serverMessages.isEmpty()){
						ServerMessage message = serverMessages.get();
						serverMessages.remove();
						connectionHandler.getServerClient(message.getId()).sendToClient(message.getMessage());
					}
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
		serverMessages.add(serverMessage);
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
}
