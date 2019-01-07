package console;

import java.util.ArrayList;

import console.gui.ConsoleGUI;
import data.Queue;
import test.data.CMD;

public class ConsoleManager {
	
	private class Task {
		private String cmd;
		private String[] args;
		
		public Task(String cmd, String[] args) {
			super();
			this.cmd = cmd;
			this.args = args;
		}
	}
	
	private ConsoleGUI gui;
	private ArrayList<CMD> commands = new ArrayList<>();
	private Queue<Task> tasks = new Queue<>();

	public ConsoleManager() {
		loadCommands();		
		
		gui = new ConsoleGUI(this);
	}

	private void loadCommands() {
		
	}

	public void invokeCommand(String cmd, String... args) {
		tasks.add(new Task(cmd, args));
	}

	public ArrayList<CMD> getCommands() {
		return commands;
	}
	
	public void tick() {
		while(!tasks.isEmpty()){
			Task task = this.tasks.get();
			tasks.remove();

			for(int i = 0; i < commands.size(); i++) {
				CMD cmd = commands.get(i);
				if(cmd.isCommand(task.cmd)){
					if(cmd.getAction().act(task.args)) return;
					else break;
				}
			}
			
			String error = "Unknown command: \""+task.cmd+"\" with arguments: ";
			for(String arg: task.args) error += "["+arg+"] ";
			gui.println(error);
		}
	}
	
	public ConsoleGUI getGUI() {
		return this.gui;
	}
}
