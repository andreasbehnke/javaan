package org.javaan;

import java.util.ArrayList;
import java.util.List;

public class CommandMap {
	
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

	public List<Command> getCommands() {
		return commands;
	}
}
