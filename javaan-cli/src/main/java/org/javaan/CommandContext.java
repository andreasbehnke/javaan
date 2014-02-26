package org.javaan;

import org.apache.commons.cli.CommandLine;
import org.javaan.commands.StandardOptions;

/**
 * Provides all information needed to run a specific command.
 */
public class CommandContext {
	
	public enum AdditionalTypeInformation {
		SUPER_CLASSES,
		SPECIALIZATIONS,
		INTERFACES,
		IMPLEMENTATIONS,
		METHODS,
		VIRTUAL_METHODS,
		NONE
	}

	private final CommandLine commandLine;
	
	private final String[] files;
	
	private ReturnCodes returnCode = ReturnCodes.ok;

	public CommandContext(CommandLine commandLine, String[] files) {
		this.commandLine = commandLine;
		this.files = files;
	}
	
	public String[] getLibraryFiles() {
		return files;
	}

	public ReturnCodes getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCodes returnCode) {
		this.returnCode = returnCode;
	}
	
	public AdditionalTypeInformation getAdditionalTypeInformation() {
		if (commandLine.hasOption(StandardOptions.OPT_SUPER)) {
			return AdditionalTypeInformation.SUPER_CLASSES;
		} else if (commandLine.hasOption(StandardOptions.OPT_SPECIALIZATIONS)) {
			return AdditionalTypeInformation.SPECIALIZATIONS;
		} else if (commandLine.hasOption(StandardOptions.OPT_INTERFACES)) {
			return AdditionalTypeInformation.INTERFACES;
		} else if (commandLine.hasOption(StandardOptions.OPT_IMPLEMENTATIONS)) {
			return AdditionalTypeInformation.IMPLEMENTATIONS;
		} else if (commandLine.hasOption(StandardOptions.OPT_METHODS)) {
			return AdditionalTypeInformation.METHODS;
		} else if (commandLine.hasOption(StandardOptions.OPT_VIRTUAL_METHODS)) {
			return AdditionalTypeInformation.VIRTUAL_METHODS;
		} else {
			return AdditionalTypeInformation.NONE;
		}
	}
	
	public boolean hasFilterCriteria() {
		return commandLine.hasOption(StandardOptions.OPT_FILTER);
	}
	
	public String getFilterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_FILTER);
	}

	public String getMethodFilterCriteria() {
		return commandLine.getOptionValue(StandardOptions.OPT_METHOD);
	}
	
	public boolean isResolveDependenciesInClassHierarchy() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY);
	}
	
	public boolean isResolveMethodImplementations() {
		return commandLine.hasOption(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS);
	}

	public boolean isPrintLeaves() {
		return commandLine.hasOption(StandardOptions.OPT_LEAVES);
	}
	
	public boolean isDisplay2dGraph() {
		return commandLine.hasOption(StandardOptions.OPT_DISPLAY_2D_GRAPH);
	}
}
