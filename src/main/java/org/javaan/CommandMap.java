package org.javaan;

import java.util.ArrayList;
import java.util.List;

public class CommandMap {
	
	public static final String HELP_COMMANDS = "supported commands:\n";
	
	private final List<Command> commands = new ArrayList<Command>();
	
	public void addCommand(Command cmd) {
		commands.add(cmd);
	}
	
	public Command getCommand(String name) {
		for (Command cmd : commands) {
			if (cmd.getName().equalsIgnoreCase(name)) {
				return cmd;
			}
		}
		return null;
	}
	
	public void printHelp() {
		System.out.println(HELP_COMMANDS);
		for (Command cmd : commands) {
			System.out.println("\t" + cmd.getName());
			System.out.println("\t\t" + cmd.getDescription());
		}
	}
}
