package test.server.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import test.server.main.ServerTestMain;

public class GUI {

	public static Font DEFAULT_FONT = new Font("TimesNewRoman", Font.BOLD, 20);

	private ServerTestMain main;
	private Dimension size = new Dimension(800, 600);
	
	private JFrame window;
	private JTextArea output;
	private JTextField input;
	private JButton send;
	
	public GUI(ServerTestMain main){
		this.main = main;
		
		generateVisuals();
		
		loadFunctions();
		
		setKillAble();
		
		update();
	}

	private void loadFunctions() {
		this.send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = input.getText();
				String[] commandParts = command.split(" ");
				if(commandParts.length == 1) main.invokeCommand(commandParts[0]);
				else if (commandParts.length > 1){
					String[] args = new String[commandParts.length-1];
					for(int i = 1; i < commandParts.length; i++)args[i-1] = commandParts[i];
					main.invokeCommand(commandParts[0], args);
				}
			}
		});
	}

	private void generateVisuals() {
		this.window = new JFrame("Server");
		this.window.setSize(size);
		this.window.setVisible(true);
		this.window.setLayout(null);
		
		this.output = new JTextArea();
		this.output.setSize((int) (size.getWidth() - 25), 450);
		this.output.setLocation(5, 10);
		this.output.setEnabled(false);
		this.output.setFont(DEFAULT_FONT);
		this.output.setForeground(Color.BLACK);
		JScrollPane pane = new JScrollPane(output);
		pane.setBounds(output.getBounds());
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.window.add(pane);
		
		this.input = new JTextField();
		this.input.setFont(DEFAULT_FONT);
		this.input.setLocation(5, output.getHeight() + output.getY() + 10);
		this.input.setSize((int) (size.getWidth() - 150), 50);
		this.window.add(input);
		
		this.send = new JButton("SEND");
		this.send.setFont(DEFAULT_FONT);
		this.send.setLocation(input.getWidth() + input.getX() + 25, output.getHeight() + output.getY() + 10);
		this.send.setSize(100, 50);
		this.window.add(send);
	}

	private void update() {
		this.window.setSize((int) window.getWidth()+1, (int) window.getHeight()+1);
		this.window.setSize(window.getSize());
	}
	
	private void setKillAble(){
		this.window.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
	}

	public void printHelp() {
		String helpMessage =  "This is an overview of all commands. \n"
							+ "Commands are invoked in the following structure: [command] -option1 -option2 ... [arg1] [arg2] ... (arg) (arg) ... \n"
							+ "  -options are optional. [args] are mandatory. (args) are optional. \n"
							+ "The following is an overview of all commands: \n";
		
		helpMessage += "  Help: views this help page. Can be invoked by the following commands: \n";
		helpMessage += "    Commands: ";
		for(String cmd: ServerTestMain.CMD_Help) helpMessage += cmd + ", ";
		helpMessage += "\n";
		helpMessage += "    Args: None";
		helpMessage += "\n";
		
		helpMessage += "  Connected Clients: views a page where infos about all connected clients are listet. Can be invoked by the following commands: \n";
		helpMessage += "    Commands: ";
		for(String cmd: ServerTestMain.CMD_OnlineClients) helpMessage += cmd + ", ";
		helpMessage += "\n";
		helpMessage += "    Args: -info";
		helpMessage += "\n ";
		
		helpMessage += "  sendTextToClient: sends a text message to the client. Can be invoked by the following commands: \n";
		helpMessage += "    Commands: ";
		for(String cmd: ServerTestMain.CMD_SendTextToClient) helpMessage += cmd + ", ";
		helpMessage += "\n";
		helpMessage += "    Args: [arg1 = clientID] [arg2 = Text (max: 63 chars)]";
		helpMessage += "\n";
		
		helpMessage += "  sendTextToAllClients: sends a text message to all connected clients. Can be invoked by the following commands: \n";
		helpMessage += "    Commands: ";
		for(String cmd: ServerTestMain.CMD_SendTextToAllClients) helpMessage += cmd + ", ";
		helpMessage += "\n";
		helpMessage += "    Args: [arg1 = Text (max: 63 chars)]";
		helpMessage += "\n ";
		
		helpMessage += "  Kick: Closes the connection from a client. Can be invoked by the following commands: \n";
		helpMessage += "    Commands: ";
		for(String cmd: ServerTestMain.CMD_KickClient) helpMessage += cmd + ", ";
		helpMessage += "\n";
		helpMessage += "    Args: [arg1 = ID] (arg2 = reason)";
		helpMessage += "\n ====";
		
		this.println(helpMessage);
	}

	public void println(String text) {
		this.output.setText(this.output.getText() + text + "\n");
	}

}
