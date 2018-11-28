package test.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.DataPackage;
import data.PackageType;
import data.readableData.ReadableData;
import test.main.Main;

public class GUI {

	public static Font DEFAULT_FONT = new Font("TimesNewRoman", Font.BOLD, 20);
	
	private JFrame window;
	private JLabel packageTypeName;
	private JComboBox<Integer> packageType;	
	private JTextArea output = new JTextArea();
	private JButton send;
	private JLabel seperator;
	private JPanel input;
	
	private ArrayList<JTextField> inputs = new ArrayList<>();	
	private Dimension size = new Dimension(500, 360);
	
	private Main main;
	
	public GUI(Main main){
		this.main = main;
		
		generateVisuals();
		
		loadFunctions();
		
		setKillAble();
		
		update();
	}

	private void loadFunctions() {
		this.packageType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				packageTypeName.setText(DataPackage.getPackageType((int) packageType.getSelectedItem()).getName());
				loadInputMenu();
			}
		});
		
		this.send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.addTask("send", (int) packageType.getSelectedItem(), readInput());
			}
		});
		
		this.window.addComponentListener(new ComponentListener() {

			HashMap<Component, int[]> originalSize = new HashMap<>();
			
			public void componentShown(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				double dw = window.getWidth() /size.getWidth();
				double dh = window.getHeight()/size.getHeight();
				
				updateSize(dw, dh, window.getContentPane().getComponents());				
				
			}

			private void updateSize(double dw, double dh, Component[] components) {
				for(Component comp: components){
					if(originalSize.containsKey(comp)){
						int[] compdata = originalSize.get(comp);
						comp.setSize((int) (compdata[2] * dw), (int) (compdata[3] * dh));
						comp.setLocation((int) (compdata[0] * dw), (int) (compdata[1] * dh));
					}else{
						originalSize.put(comp, new int[]{comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight()});
					}
					
					if(comp instanceof JPanel){
						updateSize(dw, dh, ((JPanel)comp).getComponents());
					}
				}
			}
		});
	}

	protected String[] readInput() {
		String[] args = new String[this.inputs.size()];
		for(int i = 0; i<args.length; i++){
			args[i] = this.inputs.get(i).getText();
		}
		return args;
	}

	private void loadInputMenu() {
		this.input.removeAll();
		this.inputs.clear();
		
		ReadableData<?>[] dataStructures = DataPackage.getPackageType((int) packageType.getSelectedItem()).getDataStructures();
		int off = 10;
		int length = (int) Math.ceil((this.input.getWidth()-off*2)/(double)dataStructures.length);
		if(length<100){
//			this.window.setSize((int) (dataStructures.length*100.0+off*length+20.0), window.getHeight());
			length = (int) (100/(window.getWidth()/size.getWidth()));
		}
		for(int i = 0; i<dataStructures.length; i++){
			JTextField input = new JTextField();
			input.setBounds(i*length+off, 5, length-off, 50);
			input.setFont(DEFAULT_FONT);
			this.input.add(new InputHint(dataStructures[i].getName(), input.getX()+5, input.getY(), input.getWidth()-5, input.getHeight()).getGraphics());
			this.input.add(input);
			this.inputs.add(input);
		}
		
		this.update();
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
		
		this.packageType = new JComboBox<Integer>();
		this.packageType.setBounds(5, 5, 100, 40);
		this.packageType.setFont(DEFAULT_FONT);
		this.window.add(packageType);
		
		this.packageTypeName = new JLabel();
		this.packageTypeName.setVisible(true);
		this.packageTypeName.setBounds(packageType.getX()+packageType.getWidth()+10, this.packageType.getY(), 250, 40);
		this.packageTypeName.setFont(DEFAULT_FONT);
		this.window.add(packageTypeName);
		
		this.output = new JTextArea();
		this.output.setSize((int) (size.getWidth() - 25), 190);
		this.output.setLocation(5, (int) (size.getHeight() - (output.getHeight() + 45)));
		this.output.setEnabled(false);
		this.output.setFont(DEFAULT_FONT);
		this.output.setForeground(Color.BLACK);
		JScrollPane pane = new JScrollPane(output);
		pane.setBounds(output.getBounds());
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.window.add(pane);
		
		this.send = new JButton("Send");
		this.send.setLocation(packageTypeName.getX() + packageTypeName.getWidth() + 10, packageTypeName.getY());
		this.send.setSize((int) (size.getWidth() - (send.getX() + send.getWidth() + 20)), 40);
		this.send.setFont(DEFAULT_FONT);
		this.window.add(send);
		
		this.seperator = new JLabel();
		this.seperator.setBackground(Color.black);
		this.seperator.setOpaque(true);
		this.seperator.setBounds(5, output.getY()-2, (int) size.getWidth() - 25, 2);
		this.window.add(seperator);
		
		this.input = new JPanel();
		this.input.setBackground(new Color(220, 220, 220));
		this.input.setOpaque(true);
		this.input.setBounds(5, packageType.getY() + packageType.getHeight() + 10, (int) (size.getWidth()-25), 68);
		this.input.setFont(DEFAULT_FONT);
		this.input.setLayout(null);
		this.window.add(input);
	}

	public void print(String text) {
		this.output.setText(this.output.getText()+text);
	}

	public void println(String text) {
		this.output.setText(this.output.getText()+text+"\n");
	}

	public void updatePackageTypeList() {
		this.packageType.removeAllItems();
		for(PackageType packageType: DataPackage.getPackageTypes()){
			this.packageType.addItem(packageType.getId());
		}
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
}
