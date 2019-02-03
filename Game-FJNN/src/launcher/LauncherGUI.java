package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import test.client.gui.GUI;
import test.client.gui.InputHint;

public class LauncherGUI {

	private JFrame window;
	private JTextField username;
	private JPasswordField password;
	private JButton login;
	private JTextArea output;
	
	private Dimension size = new Dimension(450, 270);
	
	private Launcher main;

	public LauncherGUI(Launcher main){
		this.main = main;
		
		generateVisuals();
		
		loadFunctions();
		
		setKillAble();
		
		update();
	}

	private void loadFunctions() {
		this.login.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.login(username.getText(), new String(password.getPassword()));
			}
		});
	}

	private void update() {
		this.window.setSize((int) window.getWidth()+1, (int) window.getHeight()+1);
		this.window.setSize(window.getSize());
	}

	private void generateVisuals() {
		this.window = new JFrame("Client");
		this.window.setSize(size);
		this.window.setVisible(true);
		this.window.setLayout(null);
		
		this.username = new JTextField();
		this.username.setFont(GUI.DEFAULT_FONT);
		this.username.setBounds(10, 10, 200, 50);
		this.window.add(new InputHint("Username", this.username.getX()+5, this.username.getY()+5, this.username.getWidth()-10, this.username.getHeight()-10).getGraphics());
		this.window.add(this.username);
		
		this.password = new JPasswordField();
		this.password.setFont(GUI.DEFAULT_FONT);
		this.password.setBounds(this.username.getX()+this.username.getWidth()+10, 10, 200, 50);
		this.window.add(new InputHint("Password", this.password.getX()+5, this.password.getY()+5, this.password.getWidth()-10, this.password.getHeight()-10).getGraphics());
		this.window.add(this.password);
		
		this.login = new JButton("Login");
		this.login.setFont(GUI.DEFAULT_FONT);
		this.login.setBounds(110, 70, 200, 50);
		this.window.add(this.login);
		
		this.output = new JTextArea();
		this.output.setBounds(10, 130, 412, 100);
		this.output.setEditable(false);
		this.output.setFont(GUI.DEFAULT_FONT);
		this.output.setForeground(Color.BLACK);
		JScrollPane pane = new JScrollPane(output);
		pane.setBounds(output.getBounds());
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.window.add(pane);
	}

	/**
	 * enables the program to kill every thread ones the window is closed
	 */
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

	public void destroy() {
		this.window.setVisible(false);
	}

	public void println(String text) {
		if(this.output.getText().equals(""))this.output.setText(text);
		else this.output.setText(this.output.getText()+"\n"+text);
	}
}
