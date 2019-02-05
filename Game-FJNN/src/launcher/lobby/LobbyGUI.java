package launcher.lobby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import test.client.gui.GUI;

public class LobbyGUI {

	private JFrame window;
	private JTextField message;
	private JTextArea output;
	private JTextArea players;
	private JButton start;
	private JProgressBar progressBar;

	private Dimension size = new Dimension(800, 600);

	private Lobby main;

	public LobbyGUI(Lobby main) {
		this.main = main;

		generateVisuals();

		loadFunctions();

		setKillAble();

		update();
	}

	private void loadFunctions() {
		this.start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.sendStartGame();
			}
		});

		this.message.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.sendMessage(message.getText());
			}
		});
	}

	private void update() {
		this.window.setSize((int) window.getWidth() + 1, (int) window.getHeight() + 1);
		this.window.setSize(window.getSize());
	}

	private void generateVisuals() {
		this.window = new JFrame("Client");
		this.window.setSize(size);
		this.window.setVisible(true);
		this.window.setLayout(null);

		this.output = new JTextArea();
		this.output.setBounds(10, 10, (int) 600, (int) 490);
		this.output.setEditable(false);
		this.output.setFont(GUI.DEFAULT_FONT);
		this.output.setForeground(Color.BLACK);
		JScrollPane pane = new JScrollPane(output);
		pane.setBounds(output.getBounds());
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.window.add(pane);

		this.message = new JTextField();
		this.message.setFont(GUI.DEFAULT_FONT);
		this.message.setBounds(output.getX(), output.getY() + output.getHeight() + 10, output.getWidth(), 50);
		this.window.add(this.message);

		this.players = new JTextArea();
		this.players.setEditable(false);
		this.players.setFont(GUI.DEFAULT_FONT);
		this.players.setForeground(Color.BLACK);
		this.players.setLocation(output.getWidth() + output.getX() + 10, output.getY());
		this.players.setSize((int) (size.getWidth() - (players.getX() + 35)), output.getHeight());
		this.window.add(this.players);

		this.start = new JButton("Start");
		this.start.setFont(GUI.DEFAULT_FONT);
		this.start.setEnabled(false);
		this.start.setBounds(players.getX(), message.getY(), players.getWidth(), message.getHeight());
		this.start.setVisible(false);
		this.window.add(this.start);

		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(start.getBounds());
		this.progressBar.setFont(GUI.DEFAULT_FONT);
		this.progressBar.setStringPainted(true);
		this.window.add(this.progressBar);
	}

	/**
	 * enables the program to kill every thread ones the window is closed
	 */
	private void setKillAble() {
		this.window.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

	public void destroy() {
		this.window.setVisible(false);
	}

	public void println(String text) {
		if (this.output.getText().equals("")) this.output.setText(text);
		else this.output.setText(this.output.getText() + "\n" + text);
	}

	public void updateProgressBar(double d) {
		this.progressBar.setValue((int) Math.round(d * 100));
		if ((int) d == 1) {
			this.progressBar.setVisible(false);
			this.start.setVisible(true);
		}
	}

	public void enableStart(boolean isHost) {
		this.start.setEnabled(isHost);
	}

	public void updatePlayers(ArrayList<SPlayer> players) {
		String newInfo = "";
		for (SPlayer player : players) {
			newInfo += (player.isHost() ? "*" : " ") + player.getUsername() + "\n";
		}
		this.players.setText(newInfo);
	}

}
