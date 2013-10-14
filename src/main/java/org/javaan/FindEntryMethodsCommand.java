package org.javaan;

public class FindEntryMethodsCommand implements Command {
	
	private final static String NAME = "findEntryMethods";
	
	private final static String DESCRIPTION = "Finds all entry methods of the libraries. An entry method is a method which is not "
			+ "being used within the library.";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public void execute(String[] args) {
		// TODO Auto-generated method stub

	}

}
