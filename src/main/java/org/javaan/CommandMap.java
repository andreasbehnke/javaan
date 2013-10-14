package org.javaan;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {
	
	public static final String HELP_COMMANDS = "supported commands:\n";
	
	private final Map<String, Command> commands = new HashMap<String, Command>();
	
	public void addCommand(Command cmd) {
		commands.put(cmd.getName(), cmd);
	}
	
	public Command getCommand(String name) {
		return commands.get(name);
	}
	
	public void printHelp() {
		System.out.println(HELP_COMMANDS);
		for (Command cmd : commands.values()) {
			System.out.println("\t" + cmd.getName());
			System.out.println("\t\t" + cmd.getDescription());
		}
	}
}
