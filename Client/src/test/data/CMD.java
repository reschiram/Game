package test.data;

public class CMD {
	
	private String[] cmdNames;
	private String[] options;
	private String[] medatoryArgs;
	private String[] optionalArgs;
	
	private String description;
	
	private CMDAction action;
	
	public CMD(CMDAction action, String description, String... cmdNames) {
		this.action = action;
		this.cmdNames = cmdNames;
		this.description = description;
		
		this.options = new String[0];
		this.medatoryArgs = new String[0];
		this.setOptionalArgs(new String[0]); 
	}

	public CMD setOptions(String... options) {
		this.options = options;
		return this;
	}

	public CMD setMedatoryArgs(String... medatoryArgs) {
		this.medatoryArgs = medatoryArgs;
		return this;
	}

	public CMD setOptionalArgs(String... optionalArgs) {
		this.optionalArgs = optionalArgs;
		return this;
	}
	
	public String getHelpPageEntry(){
		String entry = "  " + description + " Can be invoked by the following commands: \n";
		entry += "    Commands: ";
		for (String cmd : cmdNames) entry += cmd + ", ";
		entry += "\n";
		entry += "    Args: ";
		for (String option : options) entry += "-" + option + " ";
		for (String arg : medatoryArgs) entry += "[" + arg + "] ";
		for (String arg : optionalArgs) entry += "(" + arg + ") ";
		return entry;
	}

	public CMDAction getAction() {
		return action;
	}

	public String[] getCmdNames() {
		return cmdNames;
	}

	public String[] getOptions() {
		return options;
	}

	public String[] getMedatoryArgs() {
		return medatoryArgs;
	}

	public String[] getOptionalArgs() {
		return optionalArgs;
	}

	public boolean isCommand(String cmd) {
		for(String cmdName: cmdNames) {
			if(cmdName.equalsIgnoreCase(cmd)) return true;
		}
		return false;
	}

}
