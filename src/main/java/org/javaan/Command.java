package org.javaan;

public interface Command {

	String getName();
	
	String getDescription();
	
	void execute(String[] args);
}
